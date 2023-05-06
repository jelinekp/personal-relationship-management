package cz.wz.jelinekp.prm.features.contacts.data.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}