package gg.botbuilder.func;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@RequiredArgsConstructor
public class AvailableFunctionDefinition {
    @Getter
    @NonNull
    private final String name;

    @Getter
    @NonNull
    private final String description;

    @NonNull
    @Getter
    private final List<AvailableFunctionParam> arguments;

    @NonNull
    @Getter
    private final AvailableFunctionReturnValue returns;

    @Getter
    @JsonIgnore
    private final AppFunction.TYPE type;
}
