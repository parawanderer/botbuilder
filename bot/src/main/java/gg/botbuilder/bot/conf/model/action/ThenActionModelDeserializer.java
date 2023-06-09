package gg.botbuilder.bot.conf.model.action;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;

import java.io.IOException;

public class ThenActionModelDeserializer extends StdDeserializer<IThenActionArguments> {
    protected ThenActionModelDeserializer() {
        super(IThenActionArguments.class);
    }

    @Override
    public IThenActionArguments deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode root = p.getCodec().readTree(p);
        var whatIsit = p.getParsingContext().getParent().getCurrentValue();
        // DiscordThenActionModel parent = (DiscordThenActionModel) p.getParsingContext().getParent().getCurrentValue();

        // somehow get the type
        final DISCORD_ACTION_TYPE type = null; //parent.getActionType();
        Class<? extends IThenActionArguments> castTo = getAction(type);

        return p.getCodec().treeToValue(root, castTo);
    }

    private static Class<? extends IThenActionArguments> getAction(DISCORD_ACTION_TYPE type) {
        return switch(type) {
            case SEND_MESSAGE -> SendMessageActionModel.class;
            case LOG -> LogActionModel.class;
            case ROLE_ADD -> RoleAddActionModel.class;
            case WEBHOOK -> WebhookActionModel.class;
            default -> throw new UnsupportedOperationException("No mapped model for " + type);
        };
    }
}
