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
public class RoleManageActionModel extends DiscordThenActionModel {
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
    public RoleManageActionModel(@JsonProperty("do") @NonNull DISCORD_ACTION_TYPE actionType, String id, ClauseTreeRoot where, Map<String, String> variables, List<String> require, @NonNull String member, @NonNull String server, @NonNull String role) {
        super(actionType, id, where, variables, require);
        this.member = member;
        this.server = server;
        this.role = role;
    }
}
