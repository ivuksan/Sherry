package hr.ivuksan.sherry

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {

    public fun <S> createService(serviceClass: Class<S>?, baseUrl: String?): S {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(serviceClass)
    }

    inner class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request: Request = chain.request()
            val url: HttpUrl = request.url

            // Log the full URL
            println("Full URL: ${url.toString()}")

            return chain.proceed(request)
        }
    }
}