package gg.botbuilder.bot.modules;

import java.util.UUID;

public interface IBotModule {
    String getName();
    UUID getId();
    String getVersion();
    boolean isPremium();
}
