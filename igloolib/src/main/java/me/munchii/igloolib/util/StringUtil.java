package me.munchii.igloolib.util;

public class StringUtil {
    public static String toTitleCase(String s) {
        StringBuilder builder = new StringBuilder(s.length());
        boolean nextUpper = true;

        for (char c : s.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextUpper = true;
            } else if (nextUpper) {
                c = Character.toTitleCase(c);
                nextUpper = false;
            }

            builder.append(c);
        }

        return builder.toString();
    }
}
