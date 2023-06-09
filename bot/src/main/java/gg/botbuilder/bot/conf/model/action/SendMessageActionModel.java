package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gg.botbuilder.bot.clauses.ClauseTreeDeserializer;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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
    public SendMessageActionModel(DISCORD_ACTION_TYPE actionType, ClauseTreeRoot where, @NonNull String server, @NonNull String channel, @NonNull String content) {
        super(actionType, where);
        this.server = server;
        this.channel = channel;
        this.content = content;
    }
}
