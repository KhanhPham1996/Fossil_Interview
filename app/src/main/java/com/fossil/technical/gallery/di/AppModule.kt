package com.fossil.technical.gallery.di

import android.content.Context
import com.fossil.technical.gallery.helper.MediaStoreHelper
import com.fossil.technical.gallery.localDataBase.GalleryDBManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideMediaStoreHelper(@ApplicationContext context: Context) = MediaStoreHelper(context)
}