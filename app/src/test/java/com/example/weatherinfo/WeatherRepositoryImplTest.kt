package com.example.weatherinfo

import com.example.weatherinfo.api.ApiService
import com.example.weatherinfo.api.Status
import com.example.weatherinfo.models.WeatherItem
import com.example.weatherinfo.repository.WeatherRepository
import com.example.weatherinfo.repository.WeatherRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryImplTest {

    private val mockService = mockk<ApiService>(relaxed = true)
    private lateinit var repository: WeatherRepository


    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(mockService)
    }

    @Test
    fun `should return Resource as success with data when api successfully retrieves the list`() =
        runTest {
            val mockResponse = mockk<WeatherItem>()
            coEvery {
                mockService.getWeatherData(any(), any(), any())
            } returns Response.success(mockResponse)
            val repository = WeatherRepositoryImpl(mockService)
            val response = repository.getWeatherReport("IRVING")
            assertNotNull(response)
            assertEquals(Status.SUCCESS, response.status)
            assertNotNull(response.data)
        }

    @Test
    fun `should return Resource as Error with weather response when Api call is failed`() {
        coEvery {
            mockService.getWeatherData(any(), any(), any())
        } returns Response.error(
            400,
            ResponseBody.create(MediaType.parse("UTF-8"), "Failed to get Response")
        )
        runTest {
            val response = repository.getWeatherReport("Test")
            assertNotNull(response)
            assertEquals(Status.ERROR, response.status)
        }

    }
}