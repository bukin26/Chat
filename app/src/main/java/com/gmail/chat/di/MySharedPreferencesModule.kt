package com.gmail.chat.di

import com.gmail.chat.utils.MySharedPreferences
import com.gmail.chat.utils.MySharedPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface MySharedPreferencesModule {

    @Binds
    @Singleton
    fun provideMySharedPreferences(
        mySharedPreferencesImpl: MySharedPreferencesImpl
    ): MySharedPreferences
}