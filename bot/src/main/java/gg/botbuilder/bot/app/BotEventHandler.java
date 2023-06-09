package gg.botbuilder.bot.app;

import gg.botbuilder.bot.app.handler.Factory;
import gg.botbuilder.bot.app.handler.GenericEventHandlerContext;
import gg.botbuilder.bot.app.handler.IRegisteredEventSubscriber;
import gg.botbuilder.bot.app.handler.PublicMessageReadEventSubscriber;
import gg.botbuilder.bot.conf.model.ConfigContainerModel;
import gg.botbuilder.bot.conf.model.DISCORD_EVENT_TYPE;
import gg.botbuilder.bot.conf.model.DiscordEventConfigModel;
import gg.botbuilder.bot.conf.model.action.DiscordThenActionModel;
import gg.botbuilder.bot.repo.IConfigRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@RequiredArgsConstructor
public class BotEventHandler extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();

    @Setter
    private JDA jda;

    @NonNull
    private final IConfigRepository configRepository;

    private Map<String, ConfigContainerModel> configs;

    private final Map<String, Map<Class<? extends Event>, List<IRegisteredEventSubscriber>>> EVENT_MAPPINGS = new HashMap<>(); // Map<GuildId, Map<EventTypeClass, List<EventSubscriber>>>

    private final static Map<DISCORD_EVENT_TYPE, Class<? extends Event>> CLASS_TO_EVENT_MAP = Map.of(
            DISCORD_EVENT_TYPE.MESSAGE_READ, MessageReceivedEvent.class
    );

    private static IRegisteredEventSubscriber mapEventToHandler(DISCORD_EVENT_TYPE eventType, Factory<GenericEventHandlerContext> contextFactory) {
        return switch (eventType) {
            case MESSAGE_READ -> new PublicMessageReadEventSubscriber(contextFactory);
            default -> throw new UnsupportedOperationException("not implemented");
        };
    }

    @Override
    public void onReady(ReadyEvent event) {
        LOGGER.info("Ready Event Received");
        this.fillEventMappings();
    }

    private void fillEventMappings() {
        List<String> guildIds = jda.getGuilds().stream().map(ISnowflake::getId).toList();
        this.configs = configRepository.pullConfigs(guildIds);

        for (String guildId : this.configs.keySet()) { // scoped to this guild in particular!!!
            var map = EVENT_MAPPINGS.putIfAbsent(guildId, new HashMap<>());
            ConfigContainerModel config = this.configs.get(guildId);
            List<DiscordEventConfigModel> events = config.getConfig().getDiscord().getEvents();

            // get the class for the event
            for (DiscordEventConfigModel eventRule: events) {
                DISCORD_EVENT_TYPE eventType = eventRule.getOn();
                Class<? extends Event> associatedEventClass = CLASS_TO_EVENT_MAP.get(eventType);
                if (associatedEventClass == null) {
                    LOGGER.warn("Encountered unregistered event type " + eventType);
                    continue;
                }

                var consumers = map.putIfAbsent(associatedEventClass, new ArrayList<>());
                for (DiscordThenActionModel eventReaction: eventRule.getThen()) {
                    // get handler class
                    // TODO: what happens when we update the config for this guild?
                    IRegisteredEventSubscriber handler = mapEventToHandler(
                            eventType, () -> GenericEventHandlerContext.builder().environmentVariables(config.getConfig().getVariables()).thenActionDefinition(eventReaction).build()
                    );
                    consumers.add(handler);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOGGER.info("Message Received Event Received");
        if (!event.isFromGuild()) return; // TODO: how do we want to handle this case?

        // for each registered event that we will execute
        final String guildId = event.getGuild().getId();
        final Class<? extends MessageReceivedEvent> eventType = event.getClass();
        Map<Class<? extends Event>, List<IRegisteredEventSubscriber>> actionMapScopedForGuild = EVENT_MAPPINGS.get(guildId);
        List<IRegisteredEventSubscriber> actionsForThisEvent = actionMapScopedForGuild.get(eventType);

        if (actionMapScopedForGuild == null) {
            return;
        }

        for (IRegisteredEventSubscriber subscriber : actionsForThisEvent) {
            try {
                subscriber.call(event);
            } catch (Exception e) {
                LOGGER.error("Exception while executing action", e);
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        LOGGER.info("received slash command event");
        if (!event.isFromGuild()) return; // TODO: how do we want to handle this case?

        if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true)
                    .flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time))
                    .queue();
        }
    }

    private ConfigContainerModel getConfigForGuild(String guildId) {
        if (this.configs == null)
            throw new BadBotStateException("Cannot pull configs for guilds before guilds configs have been initialised");

        return this.configs.get(guildId);
    }
}
