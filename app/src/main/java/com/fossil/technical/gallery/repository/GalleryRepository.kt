package com.fossil.technical.gallery.repository

import com.fossil.technical.gallery.helper.MediaStoreHelper
import com.fossil.technical.gallery.localDataBase.GalleryDbController
import com.fossil.technical.gallery.model.MediaFile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    val galleryDbController: GalleryDbController,
    val mediaStoreHelper: MediaStoreHelper,
    ){


    suspend fun addMediaFileToFavorite(mediaFile: MediaFile) {
        galleryDbController.mediaFileFavoriteDao.insertFavoriteMediaFile(mediaFile)
    }

    fun removeMediaFileFromFavorite(mediaFile: MediaFile) {
        galleryDbController.mediaFileFavoriteDao.deleteFavorite(mediaFile.id)
    }
    suspend fun getFavoriteFile()  : List<MediaFile> {
        return galleryDbController.mediaFileFavoriteDao.getMediaFile()
    }
    suspend fun isExistInFavorite(mediaFile:MediaFile ) : Boolean {
        return galleryDbController.mediaFileFavoriteDao.exists(mediaFile.id)
    }
    suspend fun loadFileFromDevice(): Flow<MediaFile> {
      return  mediaStoreHelper.loadFileFromDevice()
    }

}