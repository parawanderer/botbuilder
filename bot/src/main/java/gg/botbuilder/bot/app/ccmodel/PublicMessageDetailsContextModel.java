package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicMessageDetailsContextModel extends BaseContextModel {
    @Getter
    @NonNull
    private final ChannelContextModel channel;
    @Getter
    @NonNull
    private final ServerContextModel server;
    @Getter
    @NonNull
    private final UserContextModel sender;
    @Getter
    @NonNull
    private final ChatMessageContextModel msg;

    @Jacksonized
    @Builder
    public PublicMessageDetailsContextModel(@NonNull Map<String, String> ENV, @NonNull ChannelContextModel channel, @NonNull ServerContextModel server, @NonNull UserContextModel sender, @NonNull ChatMessageContextModel msg) {
        super(ENV);
        this.channel = channel;
        this.server = server;
        this.sender = sender;
        this.msg = msg;
    }
}
