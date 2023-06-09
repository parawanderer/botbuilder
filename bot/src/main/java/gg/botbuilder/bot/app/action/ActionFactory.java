package gg.botbuilder.bot.app.action;

import gg.botbuilder.bot.conf.model.DISCORD_ACTION_TYPE;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;

public class ActionFactory {
    public static IAction getAction(DISCORD_ACTION_TYPE type, @NonNull ActionHandlerContext context, @NonNull JDA jda, Event discordEvent) {
        return switch (type) {
            case SEND_MESSAGE -> new DiscordSendMessageAction(context, jda, discordEvent);
            default -> throw new UnsupportedOperationException();
        };
    }
}
