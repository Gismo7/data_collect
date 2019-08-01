package net.test.android.core.util

import java.text.SimpleDateFormat
import java.util.*


class FormatUtils {

    companion object {

        val DATE_TIME_PATTERN_WITH_SECONDS = "yyyy-MM-dd HH:mm:ss"

        @JvmStatic
        fun formatDate(date: Date?, pattern: String): String {
            if (date == null) {
                return ""
            }
            val simpleDate = SimpleDateFormat(pattern, Locale.getDefault())
            return simpleDate.format(date)
        }
    }
}