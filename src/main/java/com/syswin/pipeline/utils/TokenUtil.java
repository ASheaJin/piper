package com.syswin.pipeline.utils;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by 115477 on 2018/10/18.
 */
public final class TokenUtil {
    private static final int SHUFFLE_THRESHOLD = 5;

    public static String randString(int length) {
        String chars = "ABCDEFGHIJKMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        char[] charArray = chars.toCharArray();
        shuffle(charArray, new Random());

        return new String(Arrays.copyOf(charArray, length));
    }

    private static String shuffle(char[] chars, Random rnd) {
        int size = chars.length;
        for (int i = size; i > 1; i--) {
            swap(chars, i - 1, rnd.nextInt(i));
        }

        return new String(chars);
    }

    private static void swap(char[] cs, int i, int j) {
        char temp = cs[i];
        cs[i] = cs[j];
        cs[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println(randString(8));
        System.out.println(randString(5));
        System.out.println(randString(3));
    }
}
