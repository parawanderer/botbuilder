package gg.botbuilder.bot.app.action;

import gg.botbuilder.bot.app.BadBotStateException;
import gg.botbuilder.bot.conf.model.action.IThenActionArguments;
import gg.botbuilder.bot.conf.model.action.SendMessageActionModel;
import gg.botbuilder.bot.template.TemplateParser;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DiscordSendMessageAction extends BaseAction {
    private static final Logger LOGGER = LogManager.getLogger();

    public DiscordSendMessageAction(@NonNull ActionHandlerContext context, @NonNull JDA jda, Event discordEvent) {
        super(context, jda, discordEvent);
    }

    @Override
    public void execute(IThenActionArguments actionConfig) {
        Optional<Event> currentEvent = this.getCurrentEvent();
        if (currentEvent.isEmpty())
            throw new BadBotStateException("Expected to have event associated with current execution, but there was no such event");

        if (actionConfig instanceof SendMessageActionModel sendMessageConfigModel) {
            this.doLogic(sendMessageConfigModel);
        } else {
            throw new UnsupportedOperationException("Unexpected Config model received for action " + this.getClass().getName());
        }
    }

    private void doLogic(SendMessageActionModel sendMessageConfigModel) {
        ActionHandlerContext context = this.getContext();
        String targetGuildId = sendMessageConfigModel.getServer();
        String targetChannelId = sendMessageConfigModel.getChannel();

        // must be within bot scope
        if (!context.isWithinBotScope(targetGuildId)) {
            // TODO: add details about who set up such a config
            throw new IllegalBotActionException("Tried to execute bad command on server outside of configuration scope!");
        }
        JDA jda = this.getJda();

        Guild guild = jda.getGuildById(targetGuildId);
        if (guild == null) {
            LOGGER.warn("Tried to perform " + this.getClass().getName() + " on unavailable guild " + targetGuildId);
            return;
        }

        TextChannel channel = guild.getTextChannelById(targetChannelId);
        if (channel == null) {
            LOGGER.warn("Tried to perform " + this.getClass().getName() + " on unavailable channel " + targetChannelId + " in guild " + targetGuildId);
            return;
        }

        String messageContentForReply = sendMessageConfigModel.getContent();

        // apply variables
        var placer = new TemplateParser(context.getUserQueryableContext());
        messageContentForReply = placer.apply(messageContentForReply);

        // send message to specified location
        channel.sendMessage(messageContentForReply).queue();
    }
}
