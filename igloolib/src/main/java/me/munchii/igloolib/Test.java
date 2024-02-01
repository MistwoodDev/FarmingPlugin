package me.munchii.igloolib;

public class Test {
    public static void main(String... args) {
        if (IglooVersion.ENV.get().isDevelopment()) {
            System.out.println("a");
        }

        if (IglooVersion.ENV.get().isRelease()) {

        }
    }
}
