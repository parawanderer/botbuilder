package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Jacksonized
@SuperBuilder
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryConfigurationModel {
    @Getter
    private final Map<String, String> variables;
    @Getter
    private final DiscordConfigurationModel discord;
}
