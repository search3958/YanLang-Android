package com.sentaro.yanlang

import com.sentaro.yanlang.data.AiTextCodec
import org.junit.Assert.assertEquals
import org.junit.Test

class AiTextCodecTest {
    @Test
    fun escape_producesOneValidJsonString() {
        assertEquals(
            "\"A,B{C}! + 😀 \\\\ path\"",
            AiTextCodec.escape("A,B{C}! + 😀 \\ path"),
        )
    }

    @Test
    fun unescape_preservesJsonDecodedTextIncludingBackslashes() {
        val original = "JSON: {\"key\": [a,b]} / ¥ 😀 \\"

        assertEquals(original, AiTextCodec.unescape(original))
    }

    @Test
    fun unescape_leavesBackslashesUntouched() {
        assertEquals("line\\ntext", AiTextCodec.unescape("line\\ntext"))
    }
}
