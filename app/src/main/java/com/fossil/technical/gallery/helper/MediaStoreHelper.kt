package com.fossil.technical.gallery.helper

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.fossil.technical.gallery.model.MediaFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MediaStoreHelper(val context: Context) {

     suspend fun loadFileFromDevice(): Flow<MediaFile> =
        flow {
            val uri = MediaStore.Files.getContentUri("external")
            val uriInternal = MediaStore.Files.getContentUri("internal")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATA,
            )
            val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " +
                    MediaStore.Files.FileColumns.MEDIA_TYPE + "=?")
            val selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )
            val queryArgs = Bundle().apply {
                putString(ContentResolver.QUERY_ARG_SORT_COLUMNS, MediaStore.Files.FileColumns.DATE_ADDED)
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
                )
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putStringArray(
                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                    selectionArgs
                )
            }
            val externalCursor = context.contentResolver.query(uri, projection, queryArgs, null)
            val internalCursor = context.contentResolver.query(uriInternal, projection, queryArgs, null)
            externalCursor?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val pathColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val mediaTypeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val dateCreatedColumnIndex =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                coroutineScope {
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val path = cursor.getString(pathColumnIndex)
                        val dateCreated = cursor.getString(dateCreatedColumnIndex)
                        val mediaType = cursor.getInt(mediaTypeIndex)

                        val imageUri = ContentUris.withAppendedId(uri, id)
                        val image = MediaFile(
                            id = id,
                            name = name,
                            path = path,
                            dateCreated = dateCreated,
                            fileURI = imageUri,
                            mediaType = when (mediaType) {
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaFile.Companion.MEDIA_TYPE.IMAGE_TYPE
                                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaFile.Companion.MEDIA_TYPE.VIDEO_TYPE
                                else -> {
                                    MediaFile.Companion.MEDIA_TYPE.UN_KNOW
                                }

                            }
                        )
                        emit(image)
                    }
                    cursor.close()
                }


            }
            internalCursor?.use {cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val pathColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val mediaTypeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val dateCreatedColumnIndex =
                    cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                coroutineScope {
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val path = cursor.getString(pathColumnIndex)
                        val dateCreated = cursor.getString(dateCreatedColumnIndex)
                        val mediaType = cursor.getInt(mediaTypeIndex)

                        val imageUri = ContentUris.withAppendedId(uriInternal, id)
                        val image = MediaFile(
                            id = id,
                            name = name,
                            path = path,
                            dateCreated = dateCreated,
                            fileURI = imageUri,
                            mediaType = when (mediaType) {
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaFile.Companion.MEDIA_TYPE.IMAGE_TYPE
                                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaFile.Companion.MEDIA_TYPE.VIDEO_TYPE
                                else -> {
                                    MediaFile.Companion.MEDIA_TYPE.UN_KNOW
                                }

                            }
                        )
                        emit(image)
                    }
                    cursor.close()
                }

            }


        }.flowOn(Dispatchers.IO)

}