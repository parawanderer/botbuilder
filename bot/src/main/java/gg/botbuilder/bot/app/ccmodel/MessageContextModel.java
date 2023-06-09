package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContextModel extends BaseContextModel {
    @Getter
    @NonNull
    private final PublicMessageDetailsContextModel message;

    @Jacksonized
    @Builder
    public MessageContextModel(@NonNull Map<String, String> ENV, @NonNull PublicMessageDetailsContextModel message) {
        super(ENV);
        this.message = message;
    }
}
