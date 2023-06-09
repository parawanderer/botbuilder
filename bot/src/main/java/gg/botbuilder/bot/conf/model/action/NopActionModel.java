package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NopActionModel extends DiscordThenActionModel {

    @Jacksonized
    @Builder
    public NopActionModel(@JsonProperty("do") @NonNull DISCORD_ACTION_TYPE actionType, String id, ClauseTreeRoot where, Map<String, String> variables, List<String> require) {
        super(actionType, id, where, variables, require);
    }
}
