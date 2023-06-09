package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@SuperBuilder
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordConfigurationModel {
    @Getter
    private final List<DiscordEventConfigModel> events;
}
