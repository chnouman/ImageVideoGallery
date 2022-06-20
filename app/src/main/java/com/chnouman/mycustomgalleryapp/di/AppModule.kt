package com.chnouman.mycustomgalleryapp.di

import android.app.Application
import android.content.Context
import com.chnouman.imagevideogallery.ImageVideoGallery
import com.chnouman.imagevideogallery.PictureGet
import com.chnouman.imagevideogallery.VideoGet
import com.chnouman.mycustomgalleryapp.App
import com.chnouman.mycustomgalleryapp.repository.GalleryRepositoryImpl
import com.chnouman.mycustomgalleryapp.repository.GalleryRepository
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
    fun providePictureGet(app:Application):PictureGet{
       return ImageVideoGallery.withPictureContext(app)
    }

    @Provides
    @Singleton
    fun provideVideoGet(app:Application):VideoGet{
       return ImageVideoGallery.withVideoContext(app)
    }

    @Provides
    @Singleton
    fun provideGalleryRepository(pictureGet: PictureGet,videoGet: VideoGet):GalleryRepository{
       return GalleryRepositoryImpl(pictureGet,videoGet)
    }
}