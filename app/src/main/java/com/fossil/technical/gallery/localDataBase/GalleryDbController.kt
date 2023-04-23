package com.fossil.technical.gallery.localDataBase

import com.fossil.technical.gallery.localDataBase.MediaFileFavoriteDao
import javax.inject.Inject

class GalleryDbController  @Inject constructor(private val mediaFileFavoriteDao: MediaFileFavoriteDao) {
    suspend fun cleanDBData() {
        try {
            mediaFileFavoriteDao.deleteAllData()

        } catch (er: Exception) {
            er.printStackTrace()
        }
    }
}