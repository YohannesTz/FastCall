package com.github.yohannestz.fastcall.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.yohannestz.fastcall.data.local.dao.ContactDao
import com.github.yohannestz.fastcall.data.model.db.Contact

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
}