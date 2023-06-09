package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookActionModel extends DiscordThenActionModel {
    @Getter
    @NonNull
    private final String method;
    @Getter
    @NonNull
    private final String target;
    @Getter
    @ContextualVariableEvaluation
    private final Map<String, String> headers;
    @Getter
    @ContextualVariableEvaluation
    private final String content;

    @Jacksonized
    @Builder
    public WebhookActionModel(DISCORD_ACTION_TYPE actionType, ClauseTreeRoot where, @NonNull String method, @NonNull String target, Map<String, String> headers, String content) {
        super(actionType, where);
        this.method = method;
        this.target = target;
        this.headers = headers;
        this.content = content;
    }
}
