package net.mistwood.FarmingPlugin.Utils;

import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Version;

import java.util.Collections;
import java.util.List;

public class ProfanityResult {

    public final String message;
    public final String fixedMessage;
    public final List<String> badWords;
    public final boolean hasBadWords;

    public ProfanityResult(String s, List<String> badWords) {
        this.message = s;

        String fixed = message;
        for (String word : badWords)
            fixed = fixed.replaceAll(word, String.join("", Collections.nCopies(word.length(), "*")));
        this.fixedMessage = fixed;

        this.badWords = badWords;
        this.hasBadWords = !badWords.isEmpty();
    }

    public String getSafeName() {
        if (hasBadWords) {
            if (Version.release) {
                return FarmingPlugin.instance.filter.randomSafeName();
            } else {
                return new ProfanityFilter().randomSafeName();
            }
        } else {
            return message;
        }
    }

    @Override
    public String toString() {
        return fixedMessage;
    }

}
