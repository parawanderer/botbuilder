package gg.botbuilder.bot.app.action;

import lombok.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseAction implements IAction {
    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final ActionHandlerContext context;

    @Getter(AccessLevel.PROTECTED)
    @NonNull
    private final JDA jda;

    private final Event discordEvent;

    protected Optional<Event> getCurrentEvent() {
        return Optional.ofNullable(this.discordEvent);
    }
}
