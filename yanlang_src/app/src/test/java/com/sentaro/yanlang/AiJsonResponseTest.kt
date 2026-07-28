package com.sentaro.yanlang

import com.sentaro.yanlang.data.AiJsonResponse
import com.sentaro.yanlang.data.LearningApiException
import org.junit.Assert.assertEquals
import org.junit.Test

class AiJsonResponseTest {
    @Test
    fun acceptsBracesAndEscapedSymbolsInsideJsonStrings() {
        val json = """{"source":"function\\{a\\,b\\}","items":["\\["]}"""

        assertEquals(json, AiJsonResponse.extractObject(json))
    }

    @Test(expected = LearningApiException::class)
    fun rejectsTextBeforeOrAfterJson() {
        AiJsonResponse.extractObject("説明: {\"tokens\":[]}")
    }

    @Test(expected = LearningApiException::class)
    fun rejectsInvalidJsonEscapes() {
        AiJsonResponse.extractObject("""{"source":"\,"}""")
    }
}
