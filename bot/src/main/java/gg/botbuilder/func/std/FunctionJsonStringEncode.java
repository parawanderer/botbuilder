package gg.botbuilder.func.std;

import gg.botbuilder.func.AppFunction;
import gg.botbuilder.func.AppFunctionArgument;
import gg.botbuilder.func.AppFunctionEntryPoint;

@AppFunction(
        name = "json_str_encode",
        description = "Encodes string $arg0 for the purposes of being placed into JSON as a string variable",
        type = AppFunction.TYPE.DEFAULT
)
public class FunctionJsonStringEncode {
    @AppFunctionEntryPoint(returnsDescription = """
            A string contained in quotes that can be placed into json. An example is the input string
            ```This is an "escaped" json string!```
            Will be converted into:
            ```"This is an \\"escaped\\" json string!"```
            """)
    public String get(@AppFunctionArgument(description = "The string to encode") String inputString)
    {
        return String.format("\"%s\"", inputString.replaceAll("\"", "\\\""));
    }
}
