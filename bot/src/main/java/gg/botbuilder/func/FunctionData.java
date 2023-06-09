package gg.botbuilder.func;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@Builder
@RequiredArgsConstructor
public class FunctionData {
    @Getter
    @NonNull
    private final AvailableFunctionDefinition definition;

    @Getter
    @NonNull
    private final Class<?> implementation;

    @Getter
    @NonNull
    private final Method entryPoint;
}
