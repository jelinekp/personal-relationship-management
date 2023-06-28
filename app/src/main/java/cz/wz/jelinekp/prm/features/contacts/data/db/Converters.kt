package cz.wz.jelinekp.prm.features.contacts.data.db

import android.util.Log
import androidx.room.TypeConverter
import cz.wz.jelinekp.prm.features.contacts.model.ContactCategory
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

val String.toCategory: ContactCategory
    get() {
        return when (this) {
            "family" -> ContactCategory.Family
            "friends" -> ContactCategory.Friends
            "professional" -> ContactCategory.Professional
            "other", "", " " -> ContactCategory.Other
            else -> ContactCategory.Custom(this)
        }
    }

val String.toCategories: List<ContactCategory>
    get() {
        if (this.isEmpty())
            return listOf(ContactCategory.Other)
        return this.split(";").map { it.toCategory }
    }

val List<ContactCategory>.toString: String
    get() {
        if (this.isEmpty())
            return "other"
        return this.joinToString(separator = ";") { it.toString() }
    }

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
    fun categoriesToString(categories: List<ContactCategory>?): String {
        return categories?.joinToString(separator = ";") { it.toString() } ?: "other"
    }

    @TypeConverter
    fun stringToCategories(string: String?): List<ContactCategory> {
        return string?.split(";")?.map { it.toCategory } ?: listOf(ContactCategory.Other)
    }

}