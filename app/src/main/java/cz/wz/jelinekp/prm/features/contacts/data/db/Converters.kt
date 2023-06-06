package cz.wz.jelinekp.prm.features.contacts.data.db

import android.util.Log
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { try {
            LocalDateTime.parse(it)
        } catch (t: DateTimeParseException) {
            Log.e("Timestamp conversion", "Stored time format corrupted, setting to LocalDateTime.now() - ${t.message}")
            LocalDateTime.now()
        } }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}