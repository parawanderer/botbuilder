package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gg.botbuilder.bot.clauses.ClauseTreeDeserializer;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "do",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SendMessageActionModel.class, name = "send-message"),
        @JsonSubTypes.Type(value = LogActionModel.class, name = "log"),
        @JsonSubTypes.Type(value = RoleManageActionModel.class, name = "role-add"),
        @JsonSubTypes.Type(value = WebhookActionModel.class, name = "webhook"),
        @JsonSubTypes.Type(value = NopActionModel.class, name = "nop")
})
public abstract class DiscordThenActionModel implements IThenActionArguments {
    @Getter
    @JsonProperty("do")
    @NonNull
    private final DISCORD_ACTION_TYPE actionType;

    @Getter
    private final String id; // nullable

    @JsonDeserialize(using = ClauseTreeDeserializer.class)
    private final ClauseTreeRoot where;

    @Getter
    @NonNull
    private final Map<String, String> variables;

    @Getter
    @NonNull
    private final List<String> require;

    public DiscordThenActionModel(@JsonProperty("do") @NonNull DISCORD_ACTION_TYPE actionType, String id, ClauseTreeRoot where, Map<String, String> variables, List<String> require) {
        this.actionType = actionType;
        this.id = id;
        this.where = where;
        this.variables = Optional.ofNullable(variables).orElseGet(Collections::emptyMap);
        this.require = Optional.ofNullable(require).orElseGet(Collections::emptyList);
    }

    public Optional<ClauseTreeRoot> getWhere() {
        return Optional.ofNullable(this.where);
    }
}
