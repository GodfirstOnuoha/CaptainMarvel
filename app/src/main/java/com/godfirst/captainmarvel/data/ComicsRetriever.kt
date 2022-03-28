package com.godfirst.captainmarvel.data

import com.godfirst.captainmarvel.model.Comics
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ComicsRetriever {
    private val service: RetrofitService

    companion object {
        const val BASE_URL = "http://gateway.marvel.com/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }

    suspend fun getComics(): Comics {
        return service.retrieveComics()
    }
}