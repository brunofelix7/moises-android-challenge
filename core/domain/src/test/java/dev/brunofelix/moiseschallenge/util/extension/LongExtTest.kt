package dev.brunofelix.moiseschallenge.util.extension

import dev.brunofelix.moiseschallenge.util.extension.toFormattedTime
import dev.brunofelix.moiseschallenge.util.extension.toMegabits
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LongExtTest : DescribeSpec({

    describe("toFormattedTime") {

        context("when value is zero or negative") {
            it("should return 0:00") {
                0L.toFormattedTime() shouldBe "0:00"
                (-1L).toFormattedTime() shouldBe "0:00"
                (-1000L).toFormattedTime() shouldBe "0:00"
            }
        }

        context("when value is less than a minute") {
            it("should format seconds correctly") {
                500L.toFormattedTime() shouldBe "0:00"
                1000L.toFormattedTime() shouldBe "0:01"
                1500L.toFormattedTime() shouldBe "0:01"
                30000L.toFormattedTime() shouldBe "0:30"
                59999L.toFormattedTime() shouldBe "0:59"
            }
        }

        context("when value is between one minute and ten minutes") {
            it("should format minutes and seconds correctly") {
                60000L.toFormattedTime() shouldBe "1:00"
                61000L.toFormattedTime() shouldBe "1:01"
                119000L.toFormattedTime() shouldBe "1:59"
                120000L.toFormattedTime() shouldBe "2:00"
            }
        }

        context("when value is ten minutes or more") {
            it("should format with two digits for minutes") {
                600000L.toFormattedTime() shouldBe "10:00"
                900000L.toFormattedTime() shouldBe "15:00"
            }
        }

        context("when value is one hour or more") {
            it("should format in minutes (MM:SS style)") {
                3600000L.toFormattedTime() shouldBe "60:00"
                3661000L.toFormattedTime() shouldBe "61:01"
            }
        }

        context("when it represents remaining time") {
            it("should prepend a minus sign for positive values") {
                1000L.toFormattedTime(isRemaining = true) shouldBe "-0:01"
                60000L.toFormattedTime(isRemaining = true) shouldBe "-1:00"
                152000L.toFormattedTime(isRemaining = true) shouldBe "-2:32"
            }

            it("should return 0:00 without minus sign for zero or negative values") {
                0L.toFormattedTime(isRemaining = true) shouldBe "0:00"
                (-1000L).toFormattedTime(isRemaining = true) shouldBe "0:00"
            }
        }
    }

    describe("toMegabits") {
        it("should convert correctly to bytes") {
            1L.toMegabits() shouldBe 1024 * 1024L
            100L.toMegabits() shouldBe 100 * 1024 * 1024L
            0L.toMegabits() shouldBe 0L
        }
    }
})
