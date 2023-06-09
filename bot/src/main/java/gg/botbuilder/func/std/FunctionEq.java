package gg.botbuilder.func.std;

import gg.botbuilder.func.*;

import java.math.BigInteger;

@AppFunction(
        name = "eq",
        type = AppFunction.TYPE.DEFAULT,
        description = """
                Check whether $arg1 and $arg2 are equal to one another. 
                Non-strict, so that when comparing a string to a number we will interpret both sides as a number. 
                E.g the string "1" will be equal to the number 1.
                
                Use `eqs` for a strict behaviour."""
)
public class FunctionEq {
    @AppFunctionEntryPoint(
            returnsDescription = "Returns 1 (= true) if they are equal, 0 (= false) if they are not"
    )
    public int call(
            @AppFunctionArgument(description = "What to compare", type = APP_VARIABLE_TYPE.CONTEXTUAL)
            Object one,
            @AppFunctionArgument(description = "What to compare it to", type = APP_VARIABLE_TYPE.CONTEXTUAL)
            Object other)
    {
        if (VariableUtilities.isNumber(one) || VariableUtilities.isNumber(other)) {
            // try to do a number comparison if we can...
            var oneConverted = tryConvert(one);
            var otherConverted = tryConvert(other);
            if (oneConverted != null && otherConverted != null) {
                return oneConverted.equals(otherConverted) ? 1 : 0;
            }
        }
        return one.equals(other) ? 1 : 0;
    }

    private static BigInteger tryConvert(Object obj) {
        try {
            return new BigInteger(obj.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
