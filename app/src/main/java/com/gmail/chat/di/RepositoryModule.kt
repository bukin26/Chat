package com.gmail.chat.di

import com.gmail.data.Repository
import com.gmail.data.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideUserRepository(repositoryImpl: RepositoryImpl): Repository
}