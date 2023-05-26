package com.futureengineerdev.gubugtani.article

import androidx.room.TypeConverter
import com.futureengineerdev.gubugtani.database.ImageList
import com.google.gson.Gson

class ArticleConverter {
    @TypeConverter
    fun convertJSONToStringList(json: String): ImageList = Gson().fromJson(json, ImageList::class.java)
    @TypeConverter
    fun convertStringListToJSON(stringList: ImageList): String = Gson().toJson(stringList)
}