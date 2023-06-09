package gg.botbuilder.bot.modules.calculator;

import gg.botbuilder.bot.modules.IBotModule;
import lombok.Getter;

import java.util.UUID;

public class CalculatorModule implements IBotModule {
    @Getter
    private final String name = "calculator";
    @Getter
    private final UUID id = UUID.fromString("cb2ace22-5a3b-4aef-9f87-e5319be56a7c");
    @Getter
    private final String version = "1.0.0";
    @Getter
    private final boolean premium = false;

    public int plus(int a, int b) {
        return a + b;
    }

    public int minus(int a, int b) {
        return a - b;
    }
}
