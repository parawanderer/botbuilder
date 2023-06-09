package gg.botbuilder.bot.jsonpath;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonPathChecker {
    // very basic limited implementation of JsonPath queries filtering (since you can do wayy more with JsonPath queries than this, normally!)
    public final static String JSON_PATH_ALLOWED = "\\$(?:\\.(?:[a-zA-Z_][a-zA-Z0-9_]*|\\[\"[a-zA-Z0-9_]+\"]|\\['[a-zA-Z0-9_]+']|\\[[0-9]+]))+";
    private final static Pattern FILTER_PATTERN_SOLO = Pattern.compile("^" + JSON_PATH_ALLOWED + "$");
    private final static Pattern FILTER_PATTERN_IN_STR = Pattern.compile("(" + JSON_PATH_ALLOWED + ")");

    public static boolean isLimitedJsonPath(String jsonPathQuery) {
        return FILTER_PATTERN_SOLO.matcher(jsonPathQuery).matches();
    }

    public static Matcher getRegexMatches(String stringContainingJsonPath) {
        return FILTER_PATTERN_IN_STR.matcher(stringContainingJsonPath);
    }
}
