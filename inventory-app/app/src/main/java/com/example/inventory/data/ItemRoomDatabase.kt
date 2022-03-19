package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            // Multiple threads can potentially ask for a database instance at the same time,
            // resulting in two databases instead of one.
            // Wrapping the code to get the database inside a synchronized block means
            // that only one thread of execution at a time can enter this block of code,
            // which makes sure the database only gets initialized once.
            // Pass in this the companion object, that you want to be locked inside the function block.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    // Allows Room to destructively recreate database tables if Migrations
                    // that would migrate old database schemas to the latest schema version are not found.
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }

    }
}