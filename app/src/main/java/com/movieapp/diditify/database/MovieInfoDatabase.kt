package com.movieapp.diditify.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.movieapp.diditify.models.FavMovie

@Database(entities = [FavMovie::class],version = 1,exportSchema = false)
abstract class MovieInfoDatabase :RoomDatabase(){

    abstract fun getMovieInfoDAO(): MovieInfoDAO

    companion object {
        @Volatile
        private var INSTANCE: MovieInfoDatabase? = null

        fun getDatabase(context: Context): MovieInfoDatabase {

            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        MovieInfoDatabase::class.java,
                        "fav_movies_info.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }

}