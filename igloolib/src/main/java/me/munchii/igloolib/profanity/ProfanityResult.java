package me.munchii.igloolib.profanity;

import java.util.Collections;
import java.util.List;

public class ProfanityResult {
    private final String rawText;
    private final List<String> profanity;

    public ProfanityResult(String rawText, List<String> profanity) {
        this.rawText = rawText;
        this.profanity = profanity;
    }

    public String getSafeText() {
        String safeText = rawText;

        for (String word : profanity) {
            // TODO: get random safe word
            safeText = safeText.replaceAll(word, "");
        }

        return safeText;
    }

    public String getSafeText(String replacement) {
        String safeText = rawText;

        for (String word : profanity) {
            safeText = safeText.replaceAll(word, String.join("", Collections.nCopies(word.length(), replacement)));
        }

        return safeText;
    }

    public String getRawText() {
        return rawText;
    }

    public List<String> getProfanity() {
        return profanity;
    }

    public boolean hasProfanity() {
        return !profanity.isEmpty();
    }
}
