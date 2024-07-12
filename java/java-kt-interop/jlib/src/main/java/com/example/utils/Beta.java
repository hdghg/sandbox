package com.example.utils;


import javax.annotation.Nonnull;
import javax.annotation.meta.When;

/**
 * jsr305 annotations
 */
public class Beta {

    @Nonnull(when = When.ALWAYS)
    public static Integer sumLawfulGood(Integer a, Integer b) {
        return a + b;
    }

    @Nonnull(when = When.UNKNOWN)
    public static Integer sumNeutralGood(Integer a, Integer b) {
        return a + b;
    }

    @Nonnull(when = When.NEVER)
    // same as @Nonnull(when = When.MAYBE)
    public static Integer sumChaoticGood(Integer a, Integer b) {
        return a + b;
    }

    @Nonnull(when = When.ALWAYS)
    public static Integer sumLawfulNeutral(Integer a, Integer b) {
        return 0;
    }

    @Nonnull(when = When.UNKNOWN)
    public static Integer sumTrueNeutral(Integer a, Integer b) {
        return 0;
    }

    @Nonnull(when = When.NEVER)
    public static Integer sumChaoticNeutral(Integer a, Integer b) {
        return 0;
    }

    @Nonnull(when = When.NEVER)
    public static Integer sumLawfulEvil(Integer a, Integer b) {
        return null;
    }

    @Nonnull(when = When.UNKNOWN)
    public static Integer sumNeutralEvil(Integer a, Integer b) {
        return null;
    }

    @Nonnull(when = When.ALWAYS)
    public static Integer sumChaoticEvil(Integer a, Integer b) {
        return null;
    }

}
