package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DISCORD_ACTION_TYPE {
    @JsonProperty("send-message")
    SEND_MESSAGE,
    @JsonProperty("log")
    LOG,
    @JsonProperty("role-add")
    ROLE_ADD,
    @JsonProperty("webhook")
    WEBHOOK,
    @JsonProperty("nop")
    NOP;

    public String toLowerCase() {
        return this.toString().toLowerCase();
    }

    public String toPublicFacing() {
        return this.toString().toLowerCase().replaceAll("_", "-");
    }
}
