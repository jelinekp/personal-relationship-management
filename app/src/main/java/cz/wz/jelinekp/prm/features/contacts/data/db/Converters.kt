package cz.wz.jelinekp.prm.features.contacts.data.db

import android.util.Log
import androidx.room.TypeConverter
import cz.wz.jelinekp.prm.features.contacts.model.Categories
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { try {
            LocalDateTime.parse(it)
        } catch (t: DateTimeParseException) {
            Log.e("Timestamp conversion",
                "Stored time format corrupted, setting to LocalDateTime.now() - stack trace: ${t.message}")
            LocalDateTime.now()
        } }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?) = date?.toString()

    @TypeConverter // Thank you, ChatGPT :)
    fun categoriesToString(categories: List<Categories>?): String {
        return categories?.joinToString(separator = ";") { it.toString() } ?: "other"
    }

    @TypeConverter
    fun toStringList(string: String?): List<Categories> {
        return string?.split(";")?.map { it.toCategory } ?: listOf(Categories.Other)
    }

}