package com.bobbyesp.edgeflow.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bobbyesp.edgeflow.R
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Time {
    @Composable
    fun formatLocalizedTimestamp(
        context: Context = LocalContext.current,
        timestamp: Long
    ): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val dayFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
        val yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

        val day = dateTime.toJavaLocalDateTime().format(dayFormatter)
        val month = dateTime.toJavaLocalDateTime().format(monthFormatter)
        val year = dateTime.toJavaLocalDateTime().format(yearFormatter)
        val time = dateTime.toJavaLocalDateTime().format(timeFormatter)


        return context.getString(R.string.creation_date_string, day, month, year, time)
    }

}