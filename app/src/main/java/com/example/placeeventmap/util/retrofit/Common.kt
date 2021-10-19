package com.example.placeeventmap.util.retrofit

object Common {
    private val BASE_URL = "https://geocode-maps.yandex.ru/"
    val retrofitService: YandexApi
        get() = RetrofitClient.getClient(BASE_URL).create(YandexApi::class.java)
}