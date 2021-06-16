package com.example.employees.network

import com.example.employees.network.Employee
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASEURL = "http://10.0.1.85:8081"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASEURL)
    .build()

interface EmployeeApiService {
    @GET("employees/{id}")
    suspend fun getEmployee(@Path("id") id: Int): Employee

    @GET("employees")
    suspend fun getEmployees(): List<Employee>

    @POST("employees/create")
    suspend fun postEmployee(@Body emp: Employee)

    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id: Int)
}

// make this a call to a singleton
object EmployeeApi {
    val retrofitService: EmployeeApiService by lazy {
        retrofit.create(EmployeeApiService::class.java)
    }
}