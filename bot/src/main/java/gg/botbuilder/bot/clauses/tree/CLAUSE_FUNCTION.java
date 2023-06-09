package gg.botbuilder.bot.clauses.tree;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
public enum CLAUSE_FUNCTION {
    EQ,
    CONTAINS,
    STARTS_WITH,
    ENDS_WITH,
    REGEX;

    @JsonValue
    public String toLowerCase() {
        return this.toString().toLowerCase();
    }
}
