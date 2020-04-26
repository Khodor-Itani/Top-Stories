package com.kdz.topstories.databases.typeconverters

import androidx.room.TypeConverter
import com.kdz.topstories.models.Section

class RoomTypeConverters {
    companion object {
        @JvmStatic
        @TypeConverter
        fun sectionToString(section: Section): String = section.value

        @JvmStatic
        @TypeConverter
        fun stringToSection(string: String): Section =  Section.create(string)
    }
}