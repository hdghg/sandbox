package com.example.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class BetaTest {
    @Test
    fun basicBeta() {
        val res1: Int = Beta.sumLawfulGood(1, 3)
        val res11: Int? = Beta.sumLawfulGood(1, 3) // IDE warning
        assertEquals(4, res1)
        assertEquals(4, res11)

        val res2: Int = Beta.sumNeutralGood(1, 3)
        assertEquals(4, res2)
        val res22: Int = Beta.sumNeutralGood(1, 3)
        assertEquals(4, res22)

        // val res3: Int = Beta.sumChaoticGood(1, 3) // compile error
        val res33: Int? = Beta.sumChaoticGood(1, 3)
        assertEquals(4, res33)

        val res4: Int = Beta.sumLawfulNeutral(1, 3)
        val res44: Int? = Beta.sumLawfulNeutral(1, 3) // IDE warning
        assertEquals(0, res4)
        assertEquals(0, res44)

        val res5: Int = Beta.sumTrueNeutral(1, 3)
        val res55: Int? = Beta.sumTrueNeutral(1, 3)
        assertEquals(0, res5)
        assertEquals(0, res55)

        // val res6: Int = Beta.sumChaoticNeutral(1, 3) // compile error
        val res66: Int? = Beta.sumChaoticNeutral(1, 3)
        assertEquals(0, res66)

        // val res7: Int = Beta.sumLawfulEvil(1, 3) // compile error
        val res77: Int? = Beta.sumLawfulEvil(1, 3)
        assertEquals(null, res77)

        // val res8: Int = Beta.sumNeutralEvil(1, 3) // runtime error
        val res88: Int? = Beta.sumNeutralEvil(1, 3)
        // assertEquals(null, res8) // compile error
        assertEquals(null, res88)

//        val res9: Int = Beta.sumChaoticEvil(1, 3) // runtime error
        val res99: Int? = Beta.sumChaoticEvil(1, 3) // IDE warning
//        assertEquals(null, res9) // compile error
        assertEquals(null, res99)
    }
}
