package gg.botbuilder.bot.app.handler;

import gg.botbuilder.bot.conf.model.action.DiscordThenActionModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Builder
@RequiredArgsConstructor
public class GenericEventHandlerContext {
    @Getter
    @NonNull
    private final DiscordThenActionModel thenActionDefinition;
    @Getter
    @NonNull
    private final Map<String, String> environmentVariables;
}
