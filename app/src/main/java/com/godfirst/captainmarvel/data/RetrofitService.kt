package com.godfirst.captainmarvel.data

import com.godfirst.captainmarvel.model.Comics
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {
    @GET("v1/public/comics?ts=1&apikey=36207763cf24e274b4acae01f633db3b&hash=5d16415f025d72ac34369a7a1abb5459")
    suspend fun retrieveComics(): Comics

    @GET
    suspend fun searchComics(): Comics
}