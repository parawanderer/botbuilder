package gg.botbuilder.bot.modules;

import java.util.Optional;
import java.util.concurrent.Callable;

public interface IModuleProvider {
    Optional<Callable<IBotModule>> getModule(String moduleName);
}
