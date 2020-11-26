package com.km.civilian.cpr.di.module

import com.km.civilian.cpr.repository.IMessageRepository
import com.km.civilian.cpr.repository.MessageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class MessageModule {
    @Binds
    abstract fun bindIMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): IMessageRepository
}
