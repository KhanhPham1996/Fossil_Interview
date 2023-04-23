package com.fossil.technical.gallery.localDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fossil.technical.gallery.model.MediaFile
import dagger.hilt.android.qualifiers.ApplicationContext


@Database(
    entities = [
        MediaFile::class],
    version = VERSION_1,
    exportSchema = false
)
abstract class GalleryDBManager  : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "GalleryDB"
        fun createDatabase(@ApplicationContext appContext: Context) =
            Room.databaseBuilder(appContext, GalleryDBManager::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(*ALL_MIGRATIONS)
                .build()

        private val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)
    }
    abstract fun createGalleyDao(): MediaFileFavoriteDao

}


private const val VERSION_1 = 1

private val MIGRATION_1_2 = object : Migration(VERSION_1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}