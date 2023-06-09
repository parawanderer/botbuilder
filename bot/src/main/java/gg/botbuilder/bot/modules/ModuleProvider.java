package gg.botbuilder.bot.modules;

import gg.botbuilder.bot.modules.calculator.CalculatorModule;
import gg.botbuilder.bot.modules.dummy.DummyModule;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

public class ModuleProvider implements IModuleProvider {
    private static Map<String, Callable<IBotModule>> factories = Map.of(
            "midjourney", () -> new DummyModule("midjourney"),
            "crypto", () -> new DummyModule("crypto"),
            "chatgpt", () -> new DummyModule("chatgpt"),
            "minecraft-server", () -> new DummyModule("minecraft-server"),
            "authentication", () -> new DummyModule("authentication"),
            "calculator", () -> new CalculatorModule()
    );

    @Override
    public Optional<Callable<IBotModule>> getModule(String moduleName) {
        return Optional.ofNullable(factories.get(moduleName));
    }
}
