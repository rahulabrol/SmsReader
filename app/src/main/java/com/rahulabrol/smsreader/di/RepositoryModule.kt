package com.rahulabrol.smsreader.di

import com.rahulabrol.smsreader.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

/**
 * Created by Rahul Abrol on 14/3/21.
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideHomeRepository(): HomeRepository {
        return HomeRepository()
    }
}