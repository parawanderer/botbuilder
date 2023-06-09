package gg.botbuilder.bot.conf.model.action;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleAddActionModel extends DiscordThenActionModel {
    @Getter
    @NonNull
    @ContextualVariableEvaluation
    private final String member;
    @Getter
    @NonNull
    @ContextualVariableEvaluation
    private final String server;
    @Getter
    @NonNull
    @ContextualVariableEvaluation
    private final String role;

    @Jacksonized
    @Builder
    public RoleAddActionModel(DISCORD_ACTION_TYPE actionType, ClauseTreeRoot where, @NonNull String member, @NonNull String server, @NonNull String role) {
        super(actionType, where);
        this.member = member;
        this.server = server;
        this.role = role;
    }
}
