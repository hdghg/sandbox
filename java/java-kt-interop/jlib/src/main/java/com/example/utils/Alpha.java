package com.example.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * jetbrains.annotations
 */
public class Alpha {

    @NotNull
    public static Integer sumLawfulGood(Integer a, Integer b) {
        return a + b;
    }

    // no annotation
    public static Integer sumNeutralGood(Integer a, Integer b) {
        return a + b;
    }

    @Nullable
    public static Integer sumChaoticGood(Integer a, Integer b) {
        return a + b;
    }

    @NotNull
    public static Integer sumLawfulNeutral(Integer a, Integer b) {
        return 0;
    }

    // no annotation
    public static Integer sumTrueNeutral(Integer a, Integer b) {
        return 0;
    }

    @Nullable
    public static Integer sumChaoticNeutral(Integer a, Integer b) {
        return 0;
    }

    @Nullable
    public static Integer sumLawfulEvil(Integer a, Integer b) {
        return null;
    }

    // no ann
    public static Integer sumNeutralEvil(Integer a, Integer b) {
        return null;
    }

    @NotNull
    public static Integer sumChaoticEvil(Integer a, Integer b) {
        return null;
    }

}
