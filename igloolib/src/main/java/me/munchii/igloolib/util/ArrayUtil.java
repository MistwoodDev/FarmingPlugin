package me.munchii.igloolib.util;

import java.lang.reflect.Array;

public class ArrayUtil {
    public static <T> T[] copyArrayOfRange(T[] original, int end) {
        int start = 1;
        if (original.length >= start) {
            if (start <= end) {
                int length = end - start;
                int copyLength = Math.min(length, original.length - start);
                //noinspection unchecked
                T[] copy = (T[]) Array.newInstance(original.getClass().getComponentType(), length);
                System.arraycopy(original, start, copy, 0, copyLength);

                return copy;
            }
            throw new IllegalArgumentException();
        }
        throw new ArrayIndexOutOfBoundsException();
    }
}
