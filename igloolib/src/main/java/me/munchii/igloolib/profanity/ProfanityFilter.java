package me.munchii.igloolib.profanity;

import me.munchii.igloolib.util.Resources;

import java.util.*;

public enum ProfanityFilter {
    INSTANCE;

    public static final Set<String> BAD_WORDS;
    public static final Set<String> SAFE_WORDS;
    public static final Set<String> SAFE_NAMES;

    static {
        Set<String> badWords = new HashSet<>();
        //Resources.readResourceFile(Igloolib.INSTANCE, "text/badwords.txt", line -> {
        Resources.readProjectResourceFile("text/badwords.txt", line -> {
            if (!line.startsWith("#")) {
                badWords.add(line.trim().toLowerCase(Locale.ROOT));
            }
        });
        BAD_WORDS = Collections.unmodifiableSet(badWords);

        Set<String> safeWords = new HashSet<>();
        //Resources.readResourceFile(Igloolib.INSTANCE, "text/safewords.txt", line -> {
        Resources.readProjectResourceFile("text/safewords.txt", line -> {
            if (!line.startsWith("#")) {
                safeWords.add(line.trim().toLowerCase(Locale.ROOT));
            }
        });
        SAFE_WORDS = Collections.unmodifiableSet(safeWords);

        Set<String> safeNames = new HashSet<>();
        //Resources.readResourceFile(Igloolib.INSTANCE, "text/safenames.txt", line -> {
        Resources.readProjectResourceFile("text/safenames.txt", line -> {
            if (!line.startsWith("#")) {
                safeNames.add(line.trim().toLowerCase(Locale.ROOT));
            }
        });
        SAFE_NAMES = Collections.unmodifiableSet(safeNames);
    }

    private List<String> findProfanity(String message) {
        List<String> profanity = new ArrayList<>();
        // TODO: what if someone does like acunt or cBITCH or something like that
        List<String> messageWords = Arrays.stream(message.split(" ")).map(String::toLowerCase).toList();

        if (message == null || message.isEmpty()) {
            return profanity;
        }

        BAD_WORDS.forEach(word -> {
            if (messageWords.contains(word)) {
                profanity.add(word);
            }
            messageWords.forEach(msg -> {
                if (msg.startsWith(word) || msg.endsWith(word)) {
                    profanity.add(word);
                }
            });
        });

        return profanity;
    }

    public static ProfanityResult filter(String message, boolean extensiveSearch) {
        List<String> profanity = new ArrayList<>(INSTANCE.findProfanity(message));

        if (extensiveSearch) {
            profanity.addAll(INSTANCE.findProfanity(replaceLeet(message)));
            profanity.addAll(INSTANCE.findProfanity(removeCommonChars(message)));
            profanity.addAll(INSTANCE.findProfanity(replaceLeet(removeCommonChars(message))));
        }

        return new ProfanityResult(message, profanity);
    }

    public static String replaceLeet(String s) {
        return s.replaceAll("1", "i")
                .replaceAll("!", "i")
                .replaceAll("3", "e")
                .replaceAll("@", "a")
                .replaceAll("5", "s")
                .replaceAll("7", "t")
                .replaceAll("0", "o")
                .replaceAll("9", "g");
    }

    public static String removeCommonChars(String s) {
        return s.replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll("_", "")
                .replaceAll("-", "")
                .replaceAll("/", "")
                .replaceAll(":", "")
                .replaceAll(";", "")
                .replaceAll("~", "")
                .replaceAll("\\\\", "");
    }
}
