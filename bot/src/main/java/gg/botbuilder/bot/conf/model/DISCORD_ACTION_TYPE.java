package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DISCORD_ACTION_TYPE {
    REPLY,
    SEND_MESSAGE,
    LOG,
    ROLE_ADD,
    WEBHOOK;

    public String toLowerCase() {
        return this.toString().toLowerCase();
    }

    @JsonValue
    public String toPublicFacing() {
        return this.toString().toLowerCase().replaceAll("_", "-");
    }
}
