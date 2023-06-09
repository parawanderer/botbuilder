package gg.botbuilder.func.std;

import gg.botbuilder.func.APP_VARIABLE_TYPE;
import gg.botbuilder.func.AppFunction;
import gg.botbuilder.func.AppFunctionArgument;
import gg.botbuilder.func.AppFunctionEntryPoint;

@AppFunction(
        name = "if",
        type = AppFunction.TYPE.DEFAULT,
        description = """
                If $arg0 evaluates true, return $arg1, else return $arg2
                """
)
public class FunctionIf {
    @AppFunctionEntryPoint(
            returnsDescription = "The returned value will be $arg1 if the condition is true, else it will be $arg2",
            type = APP_VARIABLE_TYPE.CONTEXTUAL
    )
    public Object call(
            @AppFunctionArgument(description = "An expression that returns a boolean")
                    int condition,
            @AppFunctionArgument(description = "The value that will be returned if condition is true", type = APP_VARIABLE_TYPE.CONTEXTUAL)
                    Object ifTrue,
            @AppFunctionArgument(description = "The value that will be returned if condition is false", type = APP_VARIABLE_TYPE.CONTEXTUAL)
                    Object ifFalse)
    {
        if (condition == 0) {
            return ifFalse;
        }
        return ifTrue;
    }
}