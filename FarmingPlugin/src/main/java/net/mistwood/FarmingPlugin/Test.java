package net.mistwood.FarmingPlugin;

import net.mistwood.FarmingPlugin.Utils.ProfanityFilter;

public class Test {

    public static void main(String[] args) {
        ProfanityFilter filter = new ProfanityFilter();
        System.out.println(filter.filter("fuck you"));
        System.out.println(filter.filter("you are a twat").getSafeName());
    }

}
