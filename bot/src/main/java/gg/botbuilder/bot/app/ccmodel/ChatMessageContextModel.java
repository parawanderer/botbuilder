package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;
import java.util.Date;

@Jacksonized
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageContextModel {
    @Getter
    @NonNull
    private final String content;
    @Getter
    @NonNull
    private final String id;
    @Getter
    @NonNull
    private final OffsetDateTime timestamp;
}
