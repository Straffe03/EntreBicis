package cat.copernic.entrebicis.Core

import cat.copernic.entrebicis.Reward.Data.RewardApiRest
import cat.copernic.entrebicis.Rutas.Data.AuthApiRest
import cat.copernic.entrebicis.Rutas.Data.RouteApiRest
import cat.copernic.entrebicis.Users.Data.UserApiRest
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objecte que proporciona instàncies de les diferents APIs REST utilitzant Retrofit.
 * Configura un client HTTP amb interceptor per registrar peticions i respostes.
 *
 * Aquesta configuració inclou conversors de dades per Scalars i Gson.
 */
object RetrofitInstance {

    /**
     * Interceptor per registrar detalladament les peticions i respostes HTTP al log.
     * Nivell de registre configurat a BODY per obtenir el màxim detall.
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Client HTTP que inclou l'interceptor de registre.
     * Aquest client és utilitzat per Retrofit per fer les peticions.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging) // Afegeix el logger per registrar les peticions i respostes
        .build()

    /**
     * Instància principal de Retrofit configurada amb la URL base del servidor backend.
     * Inclou suport per conversió de dades amb Scalars i Gson.
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // Canvia per l'IP del teu backend si cal
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /**
     * Instància de l'API REST d'usuaris.
     * @return una implementació de la interfície UserApiRest.
     */
    val api: UserApiRest by lazy {
        retrofit.create(UserApiRest::class.java)
    }

    /**
     * Instància de l'API REST de recompenses.
     * @return una implementació de la interfície RewardApiRest.
     */
    val rewardApi: RewardApiRest by lazy {
        retrofit.create(RewardApiRest::class.java)
    }

    /**
     * Instància de l'API REST de rutes.
     * @return una implementació de la interfície RouteApiRest.
     */
    val routeApi: RouteApiRest by lazy {
        retrofit.create(RouteApiRest::class.java)
    }

    /**
     * Instància de l'API REST d'autenticació.
     * @return una implementació de la interfície AuthApiRest.
     */
    val authApi: AuthApiRest by lazy {
        retrofit.create(AuthApiRest::class.java)
    }
}
