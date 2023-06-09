package gg.botbuilder.func.std;

import gg.botbuilder.func.AppFunction;
import gg.botbuilder.func.AppFunctionArgument;
import gg.botbuilder.func.AppFunctionEntryPoint;

import java.util.Random;

@AppFunction(
        name = "random",
        description = "Generates a random number (integer) between $arg1 and $arg2 (exclusive)",
        type = AppFunction.TYPE.DEFAULT
)
public class FunctionRandom {
    @AppFunctionEntryPoint(returnsDescription = "A random integer in the range [start, end]")
    public int get(
            @AppFunctionArgument(description = "start of random number range (inclusive)")
            int start,
            @AppFunctionArgument(description = "end of random number range (exclusive)")
            int end)
    {
        var rand = new Random(System.currentTimeMillis());
        return rand.nextInt(start, end);
    }
}
