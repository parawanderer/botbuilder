package gg.botbuilder.bot.template;

import gg.botbuilder.bot.jsonpath.JsonPathQueryException;
import gg.botbuilder.bot.jsonpath.IJsonPathQueryableData;
import gg.botbuilder.bot.jsonpath.JsonPathChecker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.*;
import java.util.regex.Matcher;

@RequiredArgsConstructor
public class TemplateParser {
    @NonNull
    private final IJsonPathQueryableData context;

    public String apply(String sourceString) {
        // https://stackoverflow.com/questions/1326682/java-replacing-multiple-different-substring-in-a-string-at-once-or-in-the-most

        // find all "variables" and replace them
        Matcher matcher = JsonPathChecker.getRegexMatches(sourceString);

        final StringBuilder sb = new StringBuilder( sourceString.length() << 1 );
        final Trie.TrieBuilder builder = Trie.builder()
                .onlyWholeWords()
                .ignoreOverlaps();

        Map<String, String> replacements = new HashMap<>();
        while (matcher.find()) {
            final String match = matcher.group();
            if (replacements.containsKey(match)) continue;

            Optional<String> result = this.evaluateForContext(match);
            if (result.isEmpty()) continue;

            replacements.put(match, result.get());
            builder.addKeyword(match);
        }

        final Trie trie = builder.build();
        final Collection<Emit> emits = trie.parseText(sourceString);

        int prevIndex = 0;
        for( final Emit emit : emits ) {
            final int matchIndex = emit.getStart();
            sb.append(sourceString.substring(prevIndex, matchIndex));
            sb.append(replacements.get(emit.getKeyword()));
            prevIndex = emit.getEnd() + 1;
        }

        sb.append(sourceString.substring(prevIndex));

        return sb.toString();
    }

    private Optional<String> evaluateForContext(String expression) {
        try {
            List<String> result = this.context.query(expression);
            return result.stream().findFirst();
        } catch (JsonPathQueryException e) {
        }
        return Optional.empty();
    }
}
