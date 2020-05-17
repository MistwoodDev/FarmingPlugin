package net.mistwood.FarmingPlugin;

import net.mistwood.FarmingPlugin.Utils.Lores.LoreParser;
import net.mistwood.FarmingPlugin.Utils.Lores.Option;

import static java.util.Arrays.asList;

public class Test {

    public static void main(String[] args) {
        Option<Integer> amount = new Option<>("Amount");
        Option<Boolean> isCool = new Option<>("Is Cool");

        LoreParser.parse(asList("- amount: 2", "- is cool: true"), amount, isCool);

        System.out.println(amount); // => 2
        System.out.println(isCool); // => true
    }

}
