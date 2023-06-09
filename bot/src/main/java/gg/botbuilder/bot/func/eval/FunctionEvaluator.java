package gg.botbuilder.bot.func.eval;

import gg.botbuilder.bot.func.base.*;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import gg.botbuilder.func.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FunctionEvaluator implements IFunctionEvaluator, IFunctionTreeNodeVisitor {
    @NonNull
    @Getter(AccessLevel.PRIVATE)
    private final IFunctionRegistry registry;

    @Getter(AccessLevel.PRIVATE)
    private IJsonPathQueryableData ctx;

    @Override
    public Object evaluate(@NonNull IFunctionTreeNode node, @NonNull IJsonPathQueryableData ctx) {
        this.ctx = ctx;
        return node.accept(this);
    }

    private static boolean isConform(final AvailableFunctionParam param, Object evaluationResult) {
        final FN_VARIABLE_TYPE paramType = param.getType();
        return switch (paramType) {
            case STRING -> VariableUtilities.isString(evaluationResult);
            case NUMBER -> VariableUtilities.isNumber(evaluationResult);
            case NUMBER_OR_STRING -> VariableUtilities.isNumberOrString(evaluationResult);
            default -> throw new AppFunctionExecutionException("Unsupported variable type " + paramType);
        };
    }

    private static boolean isConform(final AvailableFunctionReturnValue returnValue, Object evaluationResult) {
        final FN_VARIABLE_TYPE paramType = returnValue.getType();
        return switch (paramType) {
            case STRING -> VariableUtilities.isString(evaluationResult);
            case NUMBER -> VariableUtilities.isNumber(evaluationResult);
            case NUMBER_OR_STRING -> VariableUtilities.isNumberOrString(evaluationResult);
            default -> throw new AppFunctionExecutionException("Unsupported variable type " + paramType);
        };
    }

    private List<Object> evaluateResults(final List<AvailableFunctionParam> expectedArguments, final List<IFunctionTreeNode> actualArguments) {
        List<Object> evaluationResults = new ArrayList<>(expectedArguments.size());
        for (int i = 0; i < actualArguments.size(); ++i) {
            final AvailableFunctionParam paramDef = expectedArguments.get(i);
            final IFunctionTreeNode actualNodeDef = actualArguments.get(i);

            Object actualNodeValue = actualNodeDef.accept(this);

            if (!isConform(paramDef, actualNodeValue))
                throw new AppFunctionExecutionException("Function parameter " + paramDef.getArgumentName() +
                        " asked for type " + paramDef.getType() + " but got a value which was not this type: '"
                        + actualNodeValue.toString() + "'");

            evaluationResults.add(actualNodeValue);
        }

        return evaluationResults;
    }

    private static BigInteger asBigInteger(Object num) {
        try {
            return new BigInteger(num.toString());
        } catch (NumberFormatException e) {
            return BigInteger.ZERO;
        }
    }

    private static Object doNumberConversion(Class<?> type, BigInteger bigInteger) {
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return bigInteger.intValue();
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return bigInteger.longValue();
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            return bigInteger.byteValue();
        }
        throw new AppFunctionExecutionException("Bad number type " + type);
    }

    private static Object doConversion(Parameter param, Object value, AvailableFunctionParam def) {
        final Class<?> type = param.getType();
        final FN_VARIABLE_TYPE definedType = def.getType();
        if (definedType == FN_VARIABLE_TYPE.NUMBER && VariableUtilities.isNumber(type)) {
            if (value instanceof BigInteger bigInteger) {
                return doNumberConversion(type, bigInteger);
            } else {
                return doNumberConversion(type, asBigInteger(value));
            }
        }
        if (definedType == FN_VARIABLE_TYPE.STRING && VariableUtilities.isString(type) && value instanceof String s) {
            return s;
        }

        if (def.getType() == FN_VARIABLE_TYPE.NUMBER_OR_STRING && param.getType().equals(Object.class)) {
            // param must be "object" in this case
            return value;
        }

        throw new AppFunctionExecutionException("Cannot cast value " + value
                + " to expected parameter type of param '" + param.getName() + "' type " + param.getType().toString());
    }

    @Override
    public Object visit(FunctionFunctionTreeNode functionFunctionTreeNode) {
        // assert function is allowed
        Optional<FunctionData> fnOpt = this.getRegistry().getFunction(functionFunctionTreeNode.getName());
        if (fnOpt.isEmpty())
            throw new AppFunctionExecutionException("Function '" + functionFunctionTreeNode.getName() + "' does not exist in " + functionFunctionTreeNode);

        final FunctionData fnData = fnOpt.get();
        final List<AvailableFunctionParam> expectedArguments = fnData.getDefinition().getArguments();
        final List<IFunctionTreeNode> actualArguments = functionFunctionTreeNode.getArgs();
        if (expectedArguments.size() != actualArguments.size())
            throw new AppFunctionExecutionException("Function '" + fnData.getDefinition().getName()
                    + "' expects " + expectedArguments.size() + " arguments, but got " + actualArguments.size() + " in " + functionFunctionTreeNode);

        List<Object> evaluationResults = this.evaluateResults(expectedArguments, actualArguments);

        // then evaluate this function! (assuming valid arguments for it)
        try {
            var constructor = fnData.getImplementation().getConstructor();
            var functionExecutor = constructor.newInstance();
            var entryPoint = fnData.getEntryPoint();

            if (entryPoint.getParameters().length != expectedArguments.size())
                throw new AppFunctionExecutionException("Expected arguments did not match to true available arguments in " + functionFunctionTreeNode + "!!");

            Object[] invokeArgs = new Object[evaluationResults.size()];
            Parameter[] param = entryPoint.getParameters();
            for (int i = 0; i < invokeArgs.length; ++i) {
                invokeArgs[i] = doConversion(param[i], evaluationResults.get(i), expectedArguments.get(i));
            }

            Object returnValue = fnData.getEntryPoint().invoke(functionExecutor, invokeArgs);

            // returnValue needs to be converted to conform
            if (!isConform(fnData.getDefinition().getReturns(), returnValue))
                throw new AppFunctionExecutionException("Bad return value at call to " + functionFunctionTreeNode);

            return returnValue;

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new AppFunctionExecutionException("Reflection exception occurred", e);
        }
    }

    @Override
    public Object visit(ConstantFunctionTreeNode constantFunctionTreeNode) {
        final CONSTANT_TYPE type = constantFunctionTreeNode.getType();
        return switch (type) {
            case STRING -> constantFunctionTreeNode.getValue();
            case NUMBER -> new BigInteger(constantFunctionTreeNode.getValue()); // TODO: no current support for float...
            default -> throw new UnsupportedOperationException("Constant Type " + constantFunctionTreeNode + " is not currently supported");
        };
    }

    @Override
    public Object visit(VariableFunctionTreeNode variableFunctionTreeNode) {
        assert this.ctx != null;
        return this.ctx.queryOne(variableFunctionTreeNode.getVariable())
                .orElseThrow(() -> new AppFunctionExecutionException("Variable '" + variableFunctionTreeNode.getVariable() + "' does not return a single value"));
    }
}
