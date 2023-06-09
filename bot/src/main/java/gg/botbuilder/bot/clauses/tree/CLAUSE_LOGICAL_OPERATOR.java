package gg.botbuilder.bot.clauses.tree;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
public enum CLAUSE_LOGICAL_OPERATOR {
    AND,
    OR,
    NOT;

    @JsonValue
    public String toLowerCase() {
        return this.toString().toLowerCase();
    }
}
