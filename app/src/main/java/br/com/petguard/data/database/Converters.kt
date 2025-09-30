package br.com.petguard.data.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun fromDate(value: Date?): String? {
        return value?.let { formatter.format(it) }
    }

    @TypeConverter
    fun toDate(value: String?): Date? {
        return value?.let { formatter.parse(it) }
    }
}