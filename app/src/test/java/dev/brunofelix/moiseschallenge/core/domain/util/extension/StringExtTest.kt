package dev.brunofelix.moiseschallenge.core.domain.util.extension

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class StringExtTest : DescribeSpec({

    describe("toItunesImageSize") {

        context("when URL matches the standard iTunes image pattern") {
            it("should replace the size with the provided integer") {
                val url = "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/4c/7a/60/4c7a6021-3964-6753-9092-4c28135805f1/196589333158.jpg/100x100bb.jpg"
                val expected = "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/4c/7a/60/4c7a6021-3964-6753-9092-4c28135805f1/196589333158.jpg/600x600bb.jpg"
                
                url.toItunesImageSize(600) shouldBe expected
            }
        }

        context("when URL contains different initial dimensions") {
            it("should still replace it with the new square dimensions") {
                val url = "https://example.com/image/200x200bb.jpg"
                url.toItunesImageSize(400) shouldBe "https://example.com/image/400x400bb.jpg"
            }
        }

        context("when URL does not match the pattern") {
            it("should return the original string") {
                val url = "https://example.com/image/cover.png"
                url.toItunesImageSize(500) shouldBe url
            }
        }
    }
})
