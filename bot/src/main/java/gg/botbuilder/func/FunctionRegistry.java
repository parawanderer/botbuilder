package gg.botbuilder.func;

import lombok.NonNull;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class FunctionRegistry implements IFunctionRegistry {
    // allowed functions go here:
    // parameters must be specified to...
    private final Map<String, FunctionData> allowedFunctions = new HashMap<>();

    public FunctionRegistry() {
        var defaultFunctions = localFunctionFinder();
        for (var fn: defaultFunctions) {
            allowedFunctions.put(fn.getDefinition().getName(), fn);
        }
    }

    public void registerFunction(final @NonNull FunctionData functionData) {
        this.allowedFunctions.put(functionData.getDefinition().getName(), functionData);
    }

    @Override
    public Optional<FunctionData> getFunction(@NonNull String functionName) {
        return Optional.ofNullable(this.allowedFunctions.get(functionName));
    }

    @Override
    public @NonNull Map<String, FunctionData> getAllFunctions() {
        return Collections.unmodifiableMap(this.allowedFunctions);
    }

    private static FN_VARIABLE_TYPE getVariableType(final Class<?> variableClass, APP_VARIABLE_TYPE type, final Class<?> clazz) {
        if (type == APP_VARIABLE_TYPE.NUMBER) {
            return FN_VARIABLE_TYPE.NUMBER;
        }
        if (type == APP_VARIABLE_TYPE.STRING) {
            return FN_VARIABLE_TYPE.STRING;
        }
        if (type == APP_VARIABLE_TYPE.IMPLICIT) {
            if (VariableUtilities.isNumber(variableClass)) {
                return FN_VARIABLE_TYPE.NUMBER;
            }
            if (VariableUtilities.isString(variableClass)) {
                return FN_VARIABLE_TYPE.STRING;
            }
        }
        if (type == APP_VARIABLE_TYPE.CONTEXTUAL) {
            return FN_VARIABLE_TYPE.NUMBER_OR_STRING;
        }
        throw new AppFunctionAnnotationProcessingException("Cannot be return type that is not NUMBER or STRING for class " + clazz.getName());
    }

    private List<FunctionData> localFunctionFinder() {
        final String packageName = this.getClass().getPackageName();
        Reflections reflections = new Reflections(packageName);

        Set<Class<?>> annotated = reflections.get(SubTypes.of(TypesAnnotated.with(AppFunction.class)).asClass());

        List<FunctionData> fnList = new ArrayList<>();
        for (Class<?> clazz : annotated) {
            // validate that it can be registered (meets the requirements)

            // must have no args constructor
            try {
                if (Arrays.stream(clazz.getConstructors()).noneMatch(constructor -> constructor.getParameterCount() == 0))
                    throw new AppFunctionAnnotationProcessingException("No args constructor did not exist for class " + clazz.getName());

                List<Method> annotatedMethods = Arrays.stream(clazz.getDeclaredMethods())
                        .filter(m -> m.isAnnotationPresent(AppFunctionEntryPoint.class))
                        .collect(Collectors.toList());

                if (annotatedMethods.isEmpty())
                    throw new AppFunctionAnnotationProcessingException("Required method annotation " + AppFunctionEntryPoint.class.getName() + " was not present for class " + clazz.getName());
                if (annotatedMethods.size() > 1)
                    throw new AppFunctionAnnotationProcessingException("A class marked as an " + AppFunction.class.getName() + " may only have one method marked as the " + AppFunctionEntryPoint.class.getName() + " for class " + clazz.getName());

                Method entryPoint = annotatedMethods.get(0);

                // all parameters must be annotated
                Parameter[] methodParameters = entryPoint.getParameters();
                if (!Arrays.stream(methodParameters).allMatch(p -> p.isAnnotationPresent(AppFunctionArgument.class))) {
                    throw new RuntimeException("Method in an " + AppFunction.class.getName() + " class marked as the " + AppFunctionEntryPoint.class.getName() + " must annotate every parameter with " + AppFunctionEntryPoint.class.getName());
                }

                AppFunctionEntryPoint entryPointAnnotation = entryPoint.getAnnotation(AppFunctionEntryPoint.class);
                var appFnAnnotation = clazz.getAnnotation(AppFunction.class);

                final AppFunction.TYPE fnType = appFnAnnotation.type();
                final String fnDesc = appFnAnnotation.description();
                final String fnName = appFnAnnotation.name();

                final FN_VARIABLE_TYPE returnType = getVariableType(entryPoint.getReturnType(), entryPointAnnotation.type(), clazz);
                final String returnsDesc = entryPointAnnotation.returnsDescription();

                List<AvailableFunctionParam> paramDefs = new ArrayList<>(methodParameters.length);
                for (var methodParam : methodParameters) {
                    final AppFunctionArgument argumentAnnotation = methodParam.getAnnotation(AppFunctionArgument.class);
                    final String description = argumentAnnotation.description();
                    final FN_VARIABLE_TYPE type = getVariableType(methodParam.getType(), argumentAnnotation.type(), clazz);

                    final AvailableFunctionParam paramDef = AvailableFunctionParam
                            .builder()
                            .type(type)
                            .description(description)
                            .argumentName(methodParam.getName())
                            .build();

                    paramDefs.add(paramDef);
                }

                AvailableFunctionReturnValue returnValueDef = AvailableFunctionReturnValue
                        .builder()
                        .description(returnsDesc)
                        .type(returnType)
                        .build();

                AvailableFunctionDefinition definitionDef = AvailableFunctionDefinition.builder()
                        .name(fnName)
                        .description(fnDesc)
                        .type(fnType)
                        .returns(returnValueDef)
                        .arguments(paramDefs)
                        .build();

                FunctionData data = FunctionData.builder()
                        .definition(definitionDef)
                        .implementation(clazz)
                        .entryPoint(entryPoint)
                        .build();

                fnList.add(data);

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return fnList;
    }
}
