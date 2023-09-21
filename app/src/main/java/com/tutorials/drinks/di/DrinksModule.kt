package com.tutorials.drinks.di

import android.content.Context
import androidx.room.Room
import com.tutorials.drinks.domain.DrinksRepository
import com.tutorials.drinks.data.DrinksRepositoryImpl
import com.tutorials.drinks.db.DrinksDao
import com.tutorials.drinks.db.DrinksDatabase
import com.tutorials.drinks.domain.util.BASE_URL
import com.tutorials.drinks.domain.util.DATABASE_NAME
import com.tutorials.drinks.domain.network.DrinksApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DrinksModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            DrinksDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun getOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder().addInterceptor(logger).build()
    }

    @Singleton
    @Provides
    fun getMainRepImpl(api: DrinksApiService, db: DrinksDatabase): DrinksRepository = DrinksRepositoryImpl(api,db)



    @Singleton
    @Provides
    fun getRetrofit(http: OkHttpClient): DrinksApiService =
        Retrofit.Builder()
            .client(http)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DrinksApiService::class.java)

}