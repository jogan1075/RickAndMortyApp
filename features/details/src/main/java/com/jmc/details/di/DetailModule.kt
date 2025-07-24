package com.jmc.details.di

import com.jmc.core.BuildConfig
import com.jmc.core.network.RetrofitWebServiceFactory
import com.jmc.details.data.DataRepository
import com.jmc.details.data.remote.RemoteImpl
import com.jmc.details.data.remote.services.DetailApi
import com.jmc.details.data.repository.Remote
import com.jmc.details.data.source.Factory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailModule {

    @Provides
    @Singleton
    fun providerBranchesAPI(): DetailApi =
        RetrofitWebServiceFactory<DetailApi>().create(
            isDebug = BuildConfig.DEBUG,
            tClass = DetailApi::class.java,
            baseUrl = "https://rickandmortyapi.com/api/"
        )

    @Provides
    fun providerRemoteImpl(remoteImpl: RemoteImpl): Remote = remoteImpl


    @Provides
    internal fun bindDataRepository(factory: Factory) = DataRepository(factory)

    @Provides
    internal fun bindSourceFactory(remote: Remote, /*cache: Cache*/) = Factory(remote = remote, /*cache = cache*/)

}