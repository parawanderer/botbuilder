package gg.botbuilder.bot.app.handler;

import gg.botbuilder.bot.app.ccmodel.*;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PublicMessageReadEventSubscriber extends GenericEventSubscriber<MessageReceivedEvent> {
    public PublicMessageReadEventSubscriber(@NonNull Factory<GenericEventHandlerContext> contextFactory) {
        super(contextFactory);
    }

    @Override
    protected @NonNull IJsonPathQueryableData buildContext(@NonNull GenericEventHandlerContext context, @NonNull MessageReceivedEvent event) {
        var server = event.getGuild();
        var channel = event.getChannel();
        var chatMessage = event.getMessage();
        var sender = event.getMember();

        assert sender != null;

        var pojoContext = PublicMessageDetailsContextModel.builder()
                .ENV(context.getEnvironmentVariables())
                .server(ServerContextModel.builder()
                        .id(server.getId())
                        .name(server.getName())
                        .banner(server.getBannerUrl())
                        .description(server.getDescription())
                        .image(server.getIconUrl())
                        .link(server.getVanityUrl())
                        .build()
                ).channel(
                    ChannelContextModel.builder()
                            .id(channel.getId())
                            .name(channel.getName())
                            .build()
                ).sender(
                        UserContextModel.builder()
                                .id(sender.getId())
                                .name(sender.getEffectiveName())
                                .avatar(sender.getEffectiveAvatarUrl())
                                .roles(sender.getRoles().stream().map(ISnowflake::getId).toList())
                                .discordJoinDate(sender.getTimeCreated())
                                .serverJoinDate(sender.getTimeJoined())
                                .build()
                ).msg(
                        ChatMessageContextModel.builder()
                                .content(chatMessage.getContentRaw())
                                .timestamp(chatMessage.getTimeCreated())
                                .id(chatMessage.getId())
                                .build()
                ).build();

        return QueryableEvaluationContextFactory.build(pojoContext); // TODO: factory method for queryableeventcontext
    }
}
