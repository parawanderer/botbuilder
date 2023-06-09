package gg.botbuilder.bot.app.action;

import gg.botbuilder.bot.conf.model.action.DiscordThenActionModel;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Builder
@RequiredArgsConstructor
public class ActionHandlerContext {
    @Getter
    @NonNull
    private final DiscordThenActionModel thenActionDefinition;
    @Getter
    @NonNull
    private final Map<String, String> environmentVariables;
    @Getter
    @NonNull
    private final IJsonPathQueryableData userQueryableContext;

    public boolean isWithinBotScope(String guildId) {
        return true;
    }
}
