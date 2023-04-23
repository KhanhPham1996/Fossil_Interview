package com.fossil.technical.gallery.di

import android.content.Context
import com.fossil.technical.gallery.localDataBase.GalleryDBManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDBModule {

    @Singleton
    @Provides
    fun provideLocalDB(@ApplicationContext app: Context) = GalleryDBManager.createDatabase(app)

    @Singleton
    @Provides
    fun provideUserDao(db: GalleryDBManager) = db.createGalleyDao()
}