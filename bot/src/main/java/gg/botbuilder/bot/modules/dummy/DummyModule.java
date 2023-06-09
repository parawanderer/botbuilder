package gg.botbuilder.bot.modules.dummy;

import gg.botbuilder.bot.modules.IBotModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DummyModule implements IBotModule {
    @Getter
    private final String name;
    @Getter
    private final UUID id = UUID.randomUUID();
    @Getter
    private final String version = "1.0.0";
    @Getter
    private final boolean premium = false;
}
