package net.mistwood.FarmingPlugin.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfanityFilter {

    private List<String> badWords;

    private static final String WORDS_URL = "https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/export?format=csv";

    public ProfanityFilter() {
        this.badWords = new ArrayList<>();

        try {
            InputStream stream = new URL(WORDS_URL)
                    .openConnection()
                    .getInputStream();
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    this.badWords.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replaceLeet(String s) {
        return s.replaceAll("1", "i")
                .replaceAll("!", "i")
                .replaceAll("3", "e")
                .replaceAll("@", "a")
                .replaceAll("5", "s")
                .replaceAll("7", "t")
                .replaceAll("0", "o")
                .replaceAll("9", "g");
    }

    private String removeCommonChars(String s) {
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

    private List<String> check(String s) {
        List<String> wordsFound = new ArrayList<>();

        if (s == null || s.isEmpty()) return wordsFound;

        s = s.trim();
        s = replaceLeet(s);
        s = removeCommonChars(s);

        for (String word : s.split(" ")) {
            if (badWords.contains(word)) {
                wordsFound.add(word);
            }
        }

        return wordsFound;
    }

    public String filter(String s) {
        List<String> words = check(s);

        // TODO: Don't think this will work?
        for (String word : words) {
            s = s.replaceAll(word, Collections.nCopies(word.length(), "*").toString());
        }

        return s;
    }

}
