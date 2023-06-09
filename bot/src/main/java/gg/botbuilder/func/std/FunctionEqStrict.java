package gg.botbuilder.func.std;

import gg.botbuilder.func.APP_VARIABLE_TYPE;
import gg.botbuilder.func.AppFunction;
import gg.botbuilder.func.AppFunctionArgument;
import gg.botbuilder.func.AppFunctionEntryPoint;

@AppFunction(
        name = "eqs",
        type = AppFunction.TYPE.DEFAULT,
        description = "Check whether $arg1 and $arg2 are equal to one another. " +
                "This comparison does not allow strings to match numbers, so for example the string \"1\" is not equal to the number 1"
)
public class FunctionEqStrict {
    @AppFunctionEntryPoint(
            returnsDescription = "Returns 1 (= true) if they are equal, 0 (= false) if they are not"
    )
    public int call(
            @AppFunctionArgument(description = "What to compare", type = APP_VARIABLE_TYPE.CONTEXTUAL)
                    Object one,
            @AppFunctionArgument(description = "What to compare it to", type = APP_VARIABLE_TYPE.CONTEXTUAL)
                    Object other)
    {
        return one.equals(other) ? 1 : 0;
    }
}