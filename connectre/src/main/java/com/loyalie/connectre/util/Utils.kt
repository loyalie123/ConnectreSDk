package com.loyalie.connectre.util

import android.graphics.Color
import java.util.*

class Utils {

    companion object {

        fun darkenColor(color: Int, factor: Float): Int {
            val a = Color.alpha(color)
            val r = Math.round(Color.red(color) * factor)
            val g = Math.round(Color.green(color) * factor)
            val b = Math.round(Color.blue(color) * factor)
            return Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255))
        }


        fun formatAsTimeAgo(createdDate: Date, timeDifference: Long): String {
            (timeDifference / 1000).let {
                return if (it < 2) {
                    "Just now"
                } else if (it < 60) {
                    "$it seconds ago"
                } else if (it < 60 * 2) {
                    "${it / 60} minute ago"
                } else if (it < 60 * 60) {
                    "${it / 60} minutes ago"
                } else if ((createdDate.getDateAsNumber() == Date().getDateAsNumber()) && it < 60 * 60 * 2) {
                    "${it / (60 * 60)} hour ago"
                } else if ((createdDate.getDateAsNumber() == Date().getDateAsNumber())) {
                    "${it / (60 * 60)} hours ago"
                } else if (createdDate.getDateAsNumber() == (Date().getDateAsNumber() - 1)) {
                    "Yesterday at " + createdDate.getTimeFormatInhmma()
                } else createdDate.convertToMMMDDYYY()
            }
        }
    }
}