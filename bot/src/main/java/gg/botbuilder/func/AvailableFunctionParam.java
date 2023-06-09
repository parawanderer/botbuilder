package gg.botbuilder.func;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@RequiredArgsConstructor
public class AvailableFunctionParam {
    @Getter
    @NonNull
    private final String argumentName;

    @Getter
    @NonNull
    private final String description;

    @Getter
    private final FN_VARIABLE_TYPE type;
}
