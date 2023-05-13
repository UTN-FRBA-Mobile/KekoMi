package com.app.kekomi.storage

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDate(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }
}