package com.movieapp.diditify.di

import android.content.Context
import com.movieapp.diditify.database.MovieInfoDAO
import com.movieapp.diditify.database.MovieInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoviesInfoDAO(@ApplicationContext app: Context): MovieInfoDAO {
        return MovieInfoDatabase.getDatabase(app).getMovieInfoDAO()
    }
}