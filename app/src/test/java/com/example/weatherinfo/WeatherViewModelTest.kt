package com.example.weatherinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherinfo.api.Resource
import com.example.weatherinfo.api.Status
import com.example.weatherinfo.models.Main
import com.example.weatherinfo.models.Weather
import com.example.weatherinfo.models.WeatherItem
import com.example.weatherinfo.repository.WeatherRepository
import com.example.weatherinfo.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val instantExecutionRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<WeatherRepository>()

    private lateinit var viewModel: WeatherViewModel

    private val observer = mockk<Observer<Status>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = WeatherViewModel(mockRepository, Dispatchers.IO)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `should notify success`() {
        runTest {
            coEvery { mockRepository.getWeatherReport(any()) } returns Resource.success(getDummyData())
            viewModel.status.observeForever(observer)
            viewModel = WeatherViewModel(mockRepository)
            viewModel.onClickSearch("IRVING")
            assertEquals(Status.Waiting, viewModel.status.value)
        }
    }

    @Test
    fun `should notify success and no data available if response is empty`() {
        runTest {
            coEvery { mockRepository.getValidCity(any()) } returns Resource.success(listOf())
            viewModel.status.observeForever(observer)
            viewModel = WeatherViewModel(mockRepository)
            viewModel.onClickSearch("")
            assertEquals(Status.Waiting, viewModel.status.value)
        }
        val noDataReceived = viewModel.noDataReceived.value
        assertTrue(noDataReceived ?: true)
    }

    private fun getDummyData(): WeatherItem {
        val list = ArrayList<Weather>()
        val weather = Weather(801, "Clouds", "few clouds", "02d")
        list.add(weather)
        val main = Main(80.80, 78.00, 76.88, 82.84, 10, 15)
        return WeatherItem(
            coord = null,
            weather = list,
            base = "stations",
            main = main,
            visibility = 10000,
            wind = null,
            clouds = null,
            dt = 1685713813,
            sys = null,
            timezone = -18000,
            id = 4700168,
            name = "irving",
            cod = 200
        )

    }
}
