package com.example.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class AlphaTest {
    @Test
    fun basicAlpha() {
        val res1: Int = Alpha.sumLawfulGood(1, 3)
        val res11: Int? = Alpha.sumLawfulGood(1, 3) // IDE warning
        assertEquals(4, res1)
        assertEquals(4, res11)

        val res2: Int = Alpha.sumNeutralGood(1, 3)
        assertEquals(4, res2)
        val res22: Int? = Alpha.sumNeutralGood(1, 3)
        assertEquals(4, res22)

        // val res3: Int = Alpha.sumChaoticGood(1, 3) // compile error
        val res33: Int? = Alpha.sumChaoticGood(1, 3)
        assertEquals(4, res33)

        val res4: Int = Alpha.sumLawfulNeutral(1, 3)
        val res44: Int? = Alpha.sumLawfulNeutral(1, 3) // IDE warning
        assertEquals(0, res4)
        assertEquals(0, res44)

        val res5: Int = Alpha.sumTrueNeutral(1, 3)
        val res55: Int? = Alpha.sumTrueNeutral(1, 3)
        assertEquals(0, res5)
        assertEquals(0, res55)

        // val res6: Int = Alpha.sumChaoticNeutral(1, 3) // compile error
        val res66: Int? = Alpha.sumChaoticNeutral(1, 3)
        assertEquals(0, res66)

        // val res7: Int = Alpha.sumLawfulEvil(1, 3) // compile error
        val res77: Int? = Alpha.sumLawfulEvil(1, 3)
        assertEquals(null, res77)

        // val res8: Int = Alpha.sumNeutralEvil(1, 3) // runtime error
        val res88: Int? = Alpha.sumNeutralEvil(1, 3)
        // assertEquals(null, res8) // compile error
        assertEquals(null, res88)

//        val res9: Int = Alpha.sumChaoticEvil(1, 3) // runtime error
        val res99: Int? = Alpha.sumChaoticEvil(1, 3) // IDE warning
//        assertEquals(null, res9) // compile error
        assertEquals(null, res99)
    }
}
