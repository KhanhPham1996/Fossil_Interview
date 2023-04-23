package com.fossil.technical.gallery.localDataBase

import androidx.room.*
import com.fossil.technical.gallery.model.MediaFile
import com.fossil.technical.gallery.model.MediaFile.Companion.TABLE_NAME


@Dao
interface MediaFileFavoriteDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE `id`= :id")
    suspend fun getMediaFileById(id: Int): List<MediaFile>

    @Update
    fun updateMediaFile(user: MediaFile)

    @Query("SELECT * FROM $TABLE_NAME ")
   suspend fun getMediaFile(): List<MediaFile>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMediaFile(mediaFile: MediaFile)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAllData()

    @Query("DELETE FROM $TABLE_NAME   WHERE `id`= :id")
    fun deleteFavorite(id: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM  $TABLE_NAME WHERE `id` = :id)")
    fun exists(id: Long): Boolean

    companion object{
        const val DEFAULT_LIMIT_PAGINATION = 10
    }
}