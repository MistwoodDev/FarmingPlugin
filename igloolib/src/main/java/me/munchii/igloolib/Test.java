package me.munchii.igloolib;

import me.munchii.igloolib.profanity.ProfanityFilter;
import me.munchii.igloolib.profanity.ProfanityResult;

public class Test {
    public static void main(String... args) {
        if (IglooVersion.ENV.get().isDevelopment()) {
            System.out.println("a");
            String test = "you are acunt";
            ProfanityResult result = ProfanityFilter.filter(test, true);
            System.out.println(result.getSafeText());
        }

        if (IglooVersion.ENV.get().isRelease()) {

        }
    }
}
