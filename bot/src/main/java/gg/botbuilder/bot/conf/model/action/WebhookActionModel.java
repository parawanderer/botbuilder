package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
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
    public WebhookActionModel(@JsonProperty("do") @NonNull DISCORD_ACTION_TYPE actionType, String id, ClauseTreeRoot where, Map<String, String> variables, List<String> require, @NonNull String method, @NonNull String target, Map<String, String> headers, String content) {
        super(actionType, id, where, variables, require);
        this.method = method;
        this.target = target;
        this.headers = headers;
        this.content = content;
    }
}
