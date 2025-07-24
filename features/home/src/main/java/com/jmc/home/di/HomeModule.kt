package com.jmc.home.di

import com.jmc.core.BuildConfig
import com.jmc.core.network.RetrofitWebServiceFactory
import com.jmc.home.data.DataRepository
import com.jmc.home.data.remote.RemoteImpl
import com.jmc.home.data.remote.service.HomeApi
import com.jmc.home.data.repository.Remote
import com.jmc.home.data.source.Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providerBranchesAPI(): HomeApi =
        RetrofitWebServiceFactory<HomeApi>().create(
            isDebug = BuildConfig.DEBUG,
            tClass = HomeApi::class.java,
            baseUrl = "https://rickandmortyapi.com/api/"
        )

    @Provides
    fun providerRemoteImpl(remoteImpl: RemoteImpl): Remote = remoteImpl


    @Provides
    fun bindDataRepository(factory: Factory) = DataRepository(factory)

    @Provides
    fun bindSourceFactory(remote: Remote, /*cache: Cache*/) = Factory(remote = remote, /*cache = cache*/)


//    @Provides
//    fun bindDataRepository(dataRepository: DataRepository): DataRepository =
//        dataRepository


}