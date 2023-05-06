package com.github.yohannestz.fastcall.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.yohannestz.fastcall.data.model.db.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact")
    suspend fun getAllContacts(): List<Contact>

    @Query("SELECT * FROM Contact WHERE is_fav = 1")
    suspend fun getAllFavorites(): List<Contact>

    @Insert
    suspend fun insertAll(vararg contacts: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)
}