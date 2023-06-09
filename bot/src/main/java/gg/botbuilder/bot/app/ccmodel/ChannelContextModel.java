package gg.botbuilder.bot.app.ccmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelContextModel extends GenericNamedEntityContextModel {
    @Jacksonized
    @Builder
    public ChannelContextModel(@NonNull String name, @NonNull String id) {
        super(name, id);
    }

    @JsonProperty("at")
    public String getAt() {
        return String.format("<#%s>", this.getId());
    }
}
