package com.willywonka.model.repository.api

import com.willywonka.model.data.OompaListResponse
import com.willywonka.model.data.OompaProfile
import com.willywonka.util.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class WillyWonkaAPI {

    private val timeout = 120
    lateinit var retrofit: Retrofit

    private var onlineInterceptor: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            return response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }

    fun initApiService() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        okHttpClient.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
        okHttpClient.addInterceptor(interceptor)
        okHttpClient.addNetworkInterceptor(onlineInterceptor)

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.Service_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    fun getApi(): WillyWonkaServiceApi {
        return retrofit.create(WillyWonkaServiceApi::class.java)
    }
    
    // Services
    interface WillyWonkaServiceApi {
        /**
         * GET OOMPA LOOMPAS LIST BY PAGE
         */
        @GET("oompa-loompas")
        suspend fun getOompaWorkersList(@Query("page") page: Int?): OompaListResponse

        /**
         * GET OOMPA LOOMPA BY ID
         */
        @GET("oompa-loompas/{id}")
        suspend fun getOompaWorker(@Path("id") id: Int?): OompaProfile
    }

    companion object {
        private var instance: WillyWonkaAPI? = null
        fun getInstance(): WillyWonkaAPI {
            if (instance == null) {
                instance = WillyWonkaAPI()
            }
            return instance!!
        }
    }
}