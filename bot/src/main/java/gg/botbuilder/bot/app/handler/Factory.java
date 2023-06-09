package gg.botbuilder.bot.app.handler;

import lombok.NonNull;

public interface Factory<T> {
    @NonNull
    T call();
}
