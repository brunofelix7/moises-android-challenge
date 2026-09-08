package dev.brunofelix.moiseschallenge.util.extension

import dev.brunofelix.moiseschallenge.util.extension.toMegabits
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class IntExtTest : DescribeSpec({

    describe("toMegabits") {
        it("should convert correctly to bytes") {
            1.toMegabits() shouldBe 1024 * 1024L
            100.toMegabits() shouldBe 100 * 1024 * 1024L
            0.toMegabits() shouldBe 0L
        }
    }
})
