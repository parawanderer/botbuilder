package gg.botbuilder.bot.app.action;

import gg.botbuilder.bot.app.BadBotStateException;
import gg.botbuilder.bot.conf.model.action.IThenActionArguments;
import gg.botbuilder.bot.conf.model.action.ReplyArgumentsModel;
import gg.botbuilder.bot.template.TemplateParser;
import lombok.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import java.util.Optional;

public class DiscordReplyAction extends BaseAction {

    public DiscordReplyAction(@NonNull ActionHandlerContext context, @NonNull JDA jda, Event discordEvent) {
        super(context, jda, discordEvent);
    }

    @Override
    public void execute(IThenActionArguments actionConfig) {
        Optional<Event> currentEvent = this.getCurrentEvent();
        if (currentEvent.isEmpty())
            throw new BadBotStateException("Expected to have event associated with current execution, but there was no such event");

        Event uncastedEvent = currentEvent.get();
        if (uncastedEvent instanceof GenericMessageEvent event) {
            if (actionConfig instanceof ReplyArgumentsModel replyConfigModel) {

                ActionHandlerContext context = this.getContext();
                String messageContentForReply = replyConfigModel.getContent();

                // apply variables
                var placer = new TemplateParser(context.getUserQueryableContext());
                messageContentForReply = placer.apply(messageContentForReply);

                // reply to message
                event.getChannel().sendMessage(messageContentForReply).queue();

            } else {
                throw new UnsupportedOperationException("Unexpected Config model received for action " + this.getClass().getName());
            }
        } else {
            throw new BadBotStateException("Expected Event to be of type " + GenericMessageEvent.class.getName() + " but was " + uncastedEvent.getClass().getName());
        }
    }
}
