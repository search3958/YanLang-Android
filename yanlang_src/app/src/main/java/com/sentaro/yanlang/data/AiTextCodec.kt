package com.sentaro.yanlang.data

import org.json.JSONObject

/** 信頼できないテキストを、プロンプトに埋め込む前に標準的なJSON文字列リテラルとしてシリアライズする。 */
internal object AiTextCodec {
    fun escape(value: String): String = buildString(value.length + 2) {
        append('"')
        value.forEach { character ->
            when (character) {
                '"' -> append("\\\"")
                '\\' -> append("\\\\")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (character.code < 0x20) {
                    append("\\u%04x".format(character.code))
                } else {
                    append(character)
                }
            }
        }
        append('"')
    }

/** JSONObjectはすでに有効なレスポンス文字列をデコード済み。 */
fun unescape(value: String): String = value
}

/** 構文的に有効なJSONオブジェクトのみを受け入れ、JSON風の断片は受け付けない。 */
internal object AiJsonResponse {
    fun extractObject(content: String): String {
        val candidate = unwrapCodeFence(content.trim())
        if (!candidate.startsWith('{')) invalid()
        val end = findObjectEnd(candidate)
        if (candidate.substring(end).isNotBlank()) invalid()
        val json = candidate.substring(0, end)
        try {
            JSONObject(json)
        } catch (_: Exception) {
            invalid()
        }
        return json
    }

    private fun unwrapCodeFence(value: String): String {
        if (!value.startsWith("```")) return value
        val firstLineEnd = value.indexOf('\n')
        if (firstLineEnd < 0) invalid()
        val header = value.substring(0, firstLineEnd).trim()
        if (!header.equals("```", ignoreCase = true) && !header.equals("```json", ignoreCase = true)) {
            invalid()
        }
        if (!value.endsWith("```")) invalid()
        return value.substring(firstLineEnd + 1, value.length - 3).trim()
    }

    private fun findObjectEnd(value: String): Int {
        val stack = ArrayDeque<Char>()
        var inString = false
        var index = 0
        while (index < value.length) {
            val character = value[index]
            if (inString) {
                when {
                    character.code < 0x20 -> invalid()
                    character == '"' -> inString = false
                    character == '\\' -> {
                        if (index + 1 >= value.length) invalid()
                        val escape = value[++index]
                        if (escape == 'u') {
                            if (index + 4 >= value.length ||
                                value.substring(index + 1, index + 5).any { !it.isDigit() && it.lowercaseChar() !in 'a'..'f' }
                            ) {
                                invalid()
                            }
                            index += 4
                        } else if (escape !in "\"\\/bfnrt") {
                            invalid()
                        }
                    }
                }
            } else {
                when (character) {
                    '"' -> inString = true
                    '{' -> stack.addLast('}')
                    '[' -> stack.addLast(']')
                    '}', ']' -> {
                        if (stack.isEmpty() || stack.removeLast() != character) invalid()
                        if (stack.isEmpty()) return index + 1
                    }
                }
            }
            index++
        }
        invalid()
    }

    private fun invalid(): Nothing = throw LearningApiException("AIの応答は有効な単一JSONオブジェクトではありません")
}
