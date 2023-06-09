package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GenericNamedEntityContextModel {
    @Getter
    @NonNull
    private final String name;
    @Getter
    @NonNull
    private final String id;
}
