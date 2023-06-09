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
@Deprecated
public class ReplyArgumentsModel extends DiscordThenActionModel {
    @Getter
    @NonNull
    private final String content;

    @Jacksonized
    @Builder
    public ReplyArgumentsModel(DISCORD_ACTION_TYPE actionType, ClauseTreeRoot where, @NonNull String content) {
        super(actionType, where);
        this.content = content;
    }
}
