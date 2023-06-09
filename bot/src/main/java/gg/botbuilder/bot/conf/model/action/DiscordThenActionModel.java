package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gg.botbuilder.bot.clauses.ClauseTreeDeserializer;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "do")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SendMessageActionModel.class, name = "send-message"),
        @JsonSubTypes.Type(value = LogActionModel.class, name = "log"),
        @JsonSubTypes.Type(value = RoleAddActionModel.class, name = "role-add"),
        @JsonSubTypes.Type(value = WebhookActionModel.class, name = "webhook"),
})
@RequiredArgsConstructor
public abstract class DiscordThenActionModel implements IThenActionArguments {
    @Getter
    @JsonProperty("do")
    private final DISCORD_ACTION_TYPE actionType;

    @JsonDeserialize(using = ClauseTreeDeserializer.class)
    private final ClauseTreeRoot where;

    public Optional<ClauseTreeRoot> getWhere() {
        return Optional.ofNullable(this.where);
    }
}
