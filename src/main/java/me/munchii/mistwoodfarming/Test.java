package me.munchii.mistwoodfarming;

import me.munchii.igloolib.util.ListenerManager;

public class Test {
    public static class Yeet implements ListenerManager.Something
    {
        public Yeet() {
            System.out.println("hello");
        }
    }

    public static void main(String[] args) {
        ListenerManager lm = new ListenerManager();
        lm.register(Yeet::new);
        System.out.println(lm.isEnabled(Yeet.class));
        lm.disable(Yeet.class);
        System.out.println(lm.isEnabled(Yeet.class));
    }
}
