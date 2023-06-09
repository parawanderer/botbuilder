package gg.botbuilder.bot.app.handler;

import lombok.NonNull;
import net.dv8tion.jda.api.events.Event;

public interface IRegisteredEventSubscriber {
    void call(@NonNull Event event);
}
