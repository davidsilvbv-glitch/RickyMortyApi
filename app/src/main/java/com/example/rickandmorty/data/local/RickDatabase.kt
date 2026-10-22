package com.example.rickandmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoritoEntity::class, PersonajeCacheEntity::class], version = 2, exportSchema = false)
abstract class RickDatabase : RoomDatabase() {
    abstract fun favoritoDao(): FavoritoDao
    abstract fun personajeCacheDao(): PersonajeCacheDao

    companion object {
        @Volatile
        private var instancia: RickDatabase? = null

        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `personaje_cache` (" +
                        "`id` INTEGER NOT NULL, " +
                        "`nombre` TEXT NOT NULL, " +
                        "`status` TEXT NOT NULL, " +
                        "`species` TEXT NOT NULL, " +
                        "`image` TEXT NOT NULL, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }

        fun obtener(context: Context): RickDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    RickDatabase::class.java,
                    "rick_and_morty.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instancia = it }
            }
    }
}
