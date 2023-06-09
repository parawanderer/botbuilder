package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendMessageActionModel extends DiscordThenActionModel {
    @Getter
    @NonNull
    @ContextualVariableEvaluation
    private final String server;
    @Getter
    @NonNull
    @ContextualVariableEvaluation
    private final String channel;
    @Getter
    @NonNull
    @ContextualVariableEvaluation
    private final String content;

    @Jacksonized
    @Builder
    public SendMessageActionModel(@JsonProperty("do") @NonNull DISCORD_ACTION_TYPE actionType, String id, ClauseTreeRoot where, Map<String, String> variables, List<String> require, @NonNull String server, @NonNull String channel, @NonNull String content) {
        super(actionType, id, where, variables, require);
        this.server = server;
        this.channel = channel;
        this.content = content;
    }
}
