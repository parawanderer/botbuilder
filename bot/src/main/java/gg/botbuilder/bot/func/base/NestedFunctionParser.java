package gg.botbuilder.bot.func.base;

import lombok.NonNull;

import java.util.*;

public class NestedFunctionParser implements IFunctionParser {

    private static boolean isAllowedFunctionNameChar(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
    }

    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    private static ConstantFunctionTreeNode buildNumber(StringBuilder sb) {
        return ConstantFunctionTreeNode.builder()
                .type(CONSTANT_TYPE.NUMBER)
                .value(sb.toString())
                .build();
    }

    private static ConstantFunctionTreeNode buildString(StringBuilder sb) {
        return ConstantFunctionTreeNode.builder()
                .type(CONSTANT_TYPE.STRING)
                .value(sb.toString())
                .build();
    }

    private static VariableFunctionTreeNode buildVariable(StringBuilder sb) {
        return VariableFunctionTreeNode.builder()
                .variable(sb.toString())
                .build();
    }

    private static FunctionFunctionTreeNode buildFunction(String functionName, List<IFunctionTreeNode> args) {
        return FunctionFunctionTreeNode.builder()
                .args(Collections.unmodifiableList(args))
                .name(functionName)
                .build();
    }

    private record StackFrame(List<IFunctionTreeNode> args, String functionName, StringBuilder sb, byte state) {}

    @Override
    public FunctionFunctionTreeNode parse(@NonNull String expression) throws FunctionExpressionParsingException {
        final byte BAD = 127;
        final byte FINISH = 11;
        final byte POST_RECURSIVE = 7;
        byte state = 0;

        List<IFunctionTreeNode> args = new ArrayList<>();
        String functionName = null;
        StringBuilder sb = new StringBuilder();
        Stack<StackFrame> stack = new Stack<>();
        stack.push(new StackFrame(args, functionName, sb, state));

        int i = 0;
        while (i < expression.length() && state != BAD && !stack.isEmpty()) {
            StackFrame frame = stack.pop();
            args = frame.args();
            sb = frame.sb();
            functionName = frame.functionName();
            state = frame.state();

            final char c = expression.charAt(i);

            switch (state) {
                case 0:
                    if (isAllowedFunctionNameChar(c)) {
                        state = 1;
                        sb.append(c);
                    } else if (c == ' ') {
                        state = 0;
                    } else {
                        state = BAD;
                    }
                    break;
                case 1:
                    if (isAllowedFunctionNameChar(c)) {
                        state = 1;
                        sb.append(c);
                    } else if (c == '(') {
                        // function name is known
                        functionName = sb.toString();
                        sb.setLength(0); // reset
                        state = 2;
                    } else {
                        state = BAD;
                    }
                    break;
                case 2:
                    if (c == ' ') {
                        state = 2;
                    } else if (c == '$') {
                        sb.append(c);
                        state = 12;
                    } else if (c == '-') {
                        sb.append(c);
                        state = 3;
                    } else if (c == '"') {
                        state = 5;
                    } else if (c == ')') {
                        state = FINISH;
                    } else if (isDigit(c)) {
                        sb.append(c);
                        state = 4;
                    } else if (isAllowedFunctionNameChar(c)) {
                        // special: start "recursive" inner function evaluation
                        // remember outer container (we will return to evaluating this one later)
                        stack.push(new StackFrame(args, functionName, sb, POST_RECURSIVE));
                        // setup nested function level and start evaluating that
                        args = new ArrayList<>();
                        functionName = null;
                        sb = new StringBuilder();
                        sb.append(c);
                        state = 1;
                    } else {
                        state = BAD;
                    }
                    break;
                case 3:
                    if (c == ' ') {
                        state = 3;
                    } else if (isDigit(c)) {
                        sb.append(c);
                        state = 4;
                    } else {
                        state = BAD;
                    }
                    break;
                case 4:
                    if (isDigit(c)) {
                        sb.append(c);
                        state = 4;
                    } else if (c == ' ') {
                        // finished number if valid
                        args.add(buildNumber(sb));
                        sb.setLength(0);
                        state = 10;
                    } else if (c == ',') {
                        args.add(buildNumber(sb));
                        sb.setLength(0);
                        state = 9;
                    } else if (c == ')') {
                        args.add(buildNumber(sb));
                        sb.setLength(0);
                        state = FINISH;
                    } else {
                        state = BAD;
                    }
                    break;
                case 5:
                    if (c == '\\') {
                        sb.append(c);
                        state = 6;
                    } else if (c == '"') {
                        // end of string!
                        args.add(buildString(sb));
                        sb.setLength(0);
                        state = 8;
                    } else {
                        sb.append(c);
                        state = 5;
                    }
                    break;
                case 6:
                    sb.append(c);
                    state = 5;
                    break;
                case POST_RECURSIVE:
                    if (c == ' ') {
                        state = POST_RECURSIVE;
                    } else if (c == ',') {
                        state = 9;
                    } else if (c == ')') {
                        state = 11;
                    } else {
                        state = BAD;
                    }
                    break;
                case 8:
                    if (c == ' ') {
                        state = 8;
                    } else if (c == ',') {
                        state = 9;
                    } else if (c == ')') {
                        state = 11;
                    } else {
                        state = BAD;
                    }
                    break;
                case 9:
                    if (c == ' ') {
                        state = 9;
                    } else if (c == '-') {
                        sb.append(c);
                        state = 3;
                    } else if (isDigit(c)) {
                        sb.append(c);
                        state = 4;
                    } else if (c == '$') {
                        sb.append(c);
                        state = 12;
                    } else if (c == '"') {
                        state = 5;
                    } else if (isAllowedFunctionNameChar(c)) {
                        // special: start "recursive" inner function evaluation
                        // remember outer container (we will return to evaluating this one later)
                        stack.push(new StackFrame(args, functionName, sb, POST_RECURSIVE));
                        // setup nested function level and start evaluating that
                        args = new ArrayList<>();
                        functionName = null;
                        sb = new StringBuilder();
                        sb.append(c);
                        state = 1;
                    } else {
                        state = BAD;
                    }
                    break;
                case 10:
                    if (c == ' ') {
                        state = 10;
                    } else if (c == ')') {
                        state = FINISH;
                    } else if (c == ',') {
                        state = 9;
                    } else {
                        state = BAD;
                    }
                    break;
                case FINISH:
                    if (c == ' ') {
                        state = FINISH;
                    } else {
                        state = BAD;
                    }
                    break;
                case 12:
                    if (c == '.') {
                        sb.append(c);
                        state = 13;
                    } else {
                        state = BAD;
                    }
                    break;
                case 13:
                    if (isAllowedFunctionNameChar(c)) {
                        sb.append(c);
                        state = 14;
                    } else {
                        state = BAD;
                    }
                    break;
                case 14:
                    if (isAllowedFunctionNameChar(c)) {
                        sb.append(c);
                        state = 14;
                    } else if (c == '.') {
                        sb.append(c);
                        state = 13;
                    } else if (c == ' ') {
                        // end of variable
                        args.add(buildVariable(sb));
                        sb.setLength(0);
                        state = 15;
                    } else if (c == ',') {
                        // end of variable
                        args.add(buildVariable(sb));
                        sb.setLength(0);
                        state = 9;
                    } else if (c == ')') {
                        // end of variable
                        args.add(buildVariable(sb));
                        sb.setLength(0);
                        state = FINISH;
                    } else {
                        state = BAD;
                    }
                    break;
                case 15:
                    if (c == ' ') {
                        state = 15;
                    } else if (c == ',') {
                        state = 9;
                    } else if (c == ')') {
                        state = FINISH;
                    } else {
                        state = BAD;
                    }
            }
            i++;
            if (state != FINISH) {
                stack.push(new StackFrame(args, functionName, sb, state));
            } else {
                if (!stack.isEmpty()) {
                    stack.peek().args().add(buildFunction(functionName, args));
                }
            }
        }

        if (state == FINISH && i == expression.length() && stack.isEmpty()) {
            return buildFunction(functionName, args);
        }
        throw new RuntimeException("Bad function '" + expression + "'");
    }
}
