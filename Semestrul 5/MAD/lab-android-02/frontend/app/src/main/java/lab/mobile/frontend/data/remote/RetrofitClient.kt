package lab.mobile.frontend.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    lateinit var dataStore: DataStore<Preferences>

    fun init(ds: DataStore<Preferences>) {
        dataStore = ds
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val token = runBlocking {
                try {
                    dataStore.data.first()[TOKEN_KEY]
                } catch (e: Exception) {
                    null
                }
            }

            android.util.Log.d("DEBUG_RACE", "Interceptor: Sending token '$token'")

            val requestBuilder = original.newBuilder()
            token?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

            chain.proceed(requestBuilder.build())
        }
        .build()

    val api: BookApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }

    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthApi::class.java)
    }
}