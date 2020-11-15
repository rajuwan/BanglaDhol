package com.ebs.banglalinkbangladhol.revamp.api

import android.content.Context
import androidx.annotation.NonNull
import com.ebs.banglalinkbangladhol.others.HTTPGateway.BASE_URL
import com.ebs.banglalinkbangladhol.revamp.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient(appContext : Context) {
    private val retrofit: Retrofit
    val aPi: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
// .connectTimeout(30, TimeUnit.SECONDS)
// .readTimeout(30, TimeUnit.SECONDS)
// .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
//.baseUrl(Constant.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        private var instance: RetrofitClient? = null

        @Synchronized
        fun getInstance(@NonNull mContext: Context): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient(mContext)
            }
            return instance as RetrofitClient
        }
    }


}