package gg.botbuilder.func;

import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

public interface IFunctionRegistry {
    void registerFunction(final @NonNull FunctionData functionData);
    Optional<FunctionData> getFunction(@NonNull String functionName);
    @NonNull
    Map<String, FunctionData> getAllFunctions();
}
