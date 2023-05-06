package com.github.yohannestz.fastcall.di

import android.content.Context
import androidx.room.Room
import com.github.yohannestz.fastcall.data.local.ContactDatabase
import com.github.yohannestz.fastcall.data.local.dao.ContactDao
import com.github.yohannestz.fastcall.ui.adapters.ContactsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContactsDatabase(@ApplicationContext context: Context): ContactDatabase {
        return Room.databaseBuilder(context, ContactDatabase::class.java, "Contacts")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideContactsDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.contactDao()
    }

    @Provides
    fun provideContactAdapter(): ContactsAdapter {
        return ContactsAdapter()
    }
}