package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
public enum DISCORD_EVENT_SOURCE {
    PRIVATE,
    PUBLIC;

    @JsonValue
    public String toLowerCase() {
        return this.toString().toLowerCase();
    }
}