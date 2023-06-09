package gg.botbuilder.bot.clauses.tree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gg.botbuilder.bot.clauses.ClauseTreeDeserializer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonDeserialize(using = ClauseTreeDeserializer.class)
public class ClauseTreeRoot {
    @Getter
    @NonNull
    private final IConditionNode root;
}
