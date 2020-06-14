package net.mistwood.FarmingPlugin.Utils;

import net.mistwood.FarmingPlugin.FarmingPlugin;
import net.mistwood.FarmingPlugin.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProfanityFilter {

    public final List<String> badWords;
    public final List<String> safeNames;

    private static final String WORDS_URL = "https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/export?format=csv";

    // TODO: How do we handle 2 words being a bad word together?

    public ProfanityFilter() {
        this.badWords = new ArrayList<>();
        this.safeNames = new ArrayList<>();

        try {
            if (Version.test) {
                InputStream stream = new URL(WORDS_URL)
                        .openConnection()
                        .getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(streamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        this.badWords.add(line.trim().toLowerCase().replace(",", ""));
                    }
                }

                this.safeNames.addAll(Stream.of(
                        "SafeName1",
                        "SafeName2",
                        "SafeName3",
                        "SafeName4",
                        "SafeName5"
                ).collect(Collectors.toList()));
            } else {
                String badLine;
                while ((badLine = FarmingPlugin.instance.readResourceFile("badWords.txt").readLine()) != null) {
                    if (!badLine.startsWith("#")) {
                        this.badWords.add(badLine.trim().toLowerCase().replace(",", ""));
                    }
                }

                String safeLine;
                while ((safeLine = FarmingPlugin.instance.readResourceFile("safeNames.txt").readLine()) != null) {
                    if (!safeLine.startsWith("#")) {
                        this.badWords.add(safeLine.trim().toLowerCase().replace(",", ""));
                    }
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

        for (String word : s.split(" ")) {
            if (
                    (badWords.contains(word) && !wordsFound.contains(word)) ||
                    (badWords.contains(removeCommonChars(word)) && !wordsFound.contains(removeCommonChars(word)))
            ) {
                wordsFound.add(word);
            }
        }

        return wordsFound;
    }

    public ProfanityResult filter(String s) {
        List<String> words = check(s);

        return new ProfanityResult(s, words);
    }

    public String randomSafeName() {
        return safeNames.get(new Random().nextInt(safeNames.size()));
    }

}
