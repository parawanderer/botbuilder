package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
public enum DISCORD_EVENT_TYPE {
    MESSAGE_READ,
    USER_JOIN,
    USER_LEAVE;

    public String toLowerCase() {
        return this.toString().toLowerCase();
    }

    @JsonValue
    public String toPretty() {
        return this.toLowerCase().replaceAll("_", "-");
    }
}
