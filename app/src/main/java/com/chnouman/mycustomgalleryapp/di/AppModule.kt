package com.chnouman.mycustomgalleryapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chnouman.imagevideogallery.ImageVideoGallery
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.mycustomgalleryapp.data.repository.GalleryRepository
import com.chnouman.mycustomgalleryapp.data.repository.GalleryRepositoryImpl
import com.chnouman.mycustomgalleryapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePictureGet(app: Application): PictureGet {
        return ImageVideoGallery.withPictureContext(app)
    }

    @Provides
    @Singleton
    fun provideVideoGet(app: Application): VideoGet {
        return ImageVideoGallery.withVideoContext(app)
    }

    @Provides
    @Singleton
    fun provideGalleryRepository(pictureGet: PictureGet, videoGet: VideoGet,sharedPreferences: SharedPreferences): GalleryRepository {
        return GalleryRepositoryImpl(pictureGet, videoGet,sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(app: Application): SharedPreferences {
        return app.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE)
    }
}