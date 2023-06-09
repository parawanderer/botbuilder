package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @SuperBuilder
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomisationModel {
    @Getter
    private final String name;
    @Getter
    private final String profilePictureUrl;
    @Getter
    private final String status;
}
