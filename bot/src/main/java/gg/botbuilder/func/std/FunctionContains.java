package gg.botbuilder.func.std;

import gg.botbuilder.func.APP_VARIABLE_TYPE;
import gg.botbuilder.func.AppFunction;
import gg.botbuilder.func.AppFunctionArgument;
import gg.botbuilder.func.AppFunctionEntryPoint;

@AppFunction(
        name = "contains",
        type = AppFunction.TYPE.DEFAULT,
        description = "Check whether the string stringToCheck contains subStringToBeSearchedFor in any position"
)
public class FunctionContains {
    @AppFunctionEntryPoint(
            type = APP_VARIABLE_TYPE.NUMBER,
            returnsDescription = "Returns 1 (= true) if stringToCheck contains subStringToBeSearchedFor in any position, 0 (= false) if it does not"
    )
    public int call(
            @AppFunctionArgument(description = "Which string to check")
                    String stringToCheck,
            @AppFunctionArgument(description = "What to search for in stringToCheck")
                    String subStringToBeSearchedFor)
    {
        return stringToCheck.contains(subStringToBeSearchedFor) ? 1 : 0;
    }
}
