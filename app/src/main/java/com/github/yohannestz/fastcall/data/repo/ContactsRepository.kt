package com.github.yohannestz.fastcall.data.repo

import com.github.yohannestz.fastcall.data.local.dao.ContactDao
import com.github.yohannestz.fastcall.data.model.db.Contact
import com.github.yohannestz.fastcall.data.model.generic.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepository @Inject constructor(
    private val contactDao: ContactDao
) {
    suspend fun getAllContacts(): Flow<Result<List<Contact>>> = flow {
        emit(Result.Loading())
        try {
            val contacts = contactDao.getAllContacts()
            emit(Result.Success(contacts))
        } catch (ex: Exception) {
            emit(Result.Error(ex.message ?: "Error occurred!"))
        }
    }

    suspend fun getFavorites(): Flow<Result<List<Contact>>> = flow {
        emit(Result.Loading())
        try {
            val contacts = contactDao.getAllFavorites()
            emit(Result.Success(contacts))
        } catch (ex: Exception) {
            emit(Result.Error(ex.message ?: "Error occurred!"))
        }
    }

    suspend fun insertContact(contact: Contact): Flow<Result<Boolean>> = flow {
        emit(Result.Loading())
        try {
            contactDao.insertAll(contact)
            emit(Result.Success(true))
        } catch (ex: Exception) {
            emit(Result.Error(ex.message ?: "Error occurred!"))
        }
    }

    suspend fun deleteContact(contact: Contact): Flow<Result<Boolean>> = flow {
        emit(Result.Loading())
        try {
            contactDao.deleteContact(contact)
            emit(Result.Success(true))
        } catch (ex: Exception) {
            emit(Result.Error(ex.message ?: "Error occurred!"))
        }
    }
}