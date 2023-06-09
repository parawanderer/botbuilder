package gg.botbuilder.bot.conf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gg.botbuilder.bot.clauses.ClauseTreeDeserializer;
import gg.botbuilder.bot.clauses.tree.ClauseTreeRoot;
import gg.botbuilder.bot.conf.model.action.DiscordThenActionModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

@Jacksonized
@SuperBuilder
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordEventConfigModel {
    @Getter
    private final DISCORD_EVENT_TYPE on;

    @JsonDeserialize(using = ClauseTreeDeserializer.class)
    private final ClauseTreeRoot where;

    @Getter
    @NonNull
    private final List<DiscordThenActionModel> then;

    public Optional<ClauseTreeRoot> getWhere() {
        return Optional.ofNullable(this.where);
    }
}
