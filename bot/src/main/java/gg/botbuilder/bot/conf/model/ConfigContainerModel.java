package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Jacksonized @Builder
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigContainerModel {
    @Getter
    private final String version;
    @Getter
    private final CustomisationModel customisation;
    @Getter
    private final ModulesModel modules;
    @Getter
    private final PrimaryConfigurationModel config;
}
