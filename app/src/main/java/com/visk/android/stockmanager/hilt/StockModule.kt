package com.visk.android.stockmanager.hilt

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.visk.android.stockmanager.db.StockDatabase
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.stock.StockRetrofit
import com.visk.android.stockmanager.stock.StockSearchRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object StockModule {

    @Provides
    fun provideStockDatabase(@ApplicationContext context :Context) : StockDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            StockDatabase::class.java,
            StockDatabase.DATABASE_NAME
        ).openHelperFactory(FrameworkSQLiteOpenHelperFactory()).addMigrations(
            StockDatabase.MIGRATION_1_2,
            StockDatabase.MIGRATION_2_3,
            StockDatabase.MIGRATION_3_4,
            StockDatabase.MIGRATION_4_5
        ).enableMultiInstanceInvalidation().build()
    }

    @Provides
    fun provideRoomDao(
        stockDatabase: StockDatabase
    ): StockDao {
        return stockDatabase.stockDao()
    }

    @Provides
    fun provideStockRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://polling.finance.naver.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(StockRetrofit::class.java)

    @Provides
    fun provideSearchRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://search.naver.com/")
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build().create(StockSearchRetrofit::class.java)

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
}
