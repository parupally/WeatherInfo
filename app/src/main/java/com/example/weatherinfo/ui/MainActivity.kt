package com.example.weatherinfo.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherinfo.R
import com.example.weatherinfo.api.RetrofitProvider
import com.example.weatherinfo.databinding.ActivityMainBinding
import com.example.weatherinfo.models.WeatherItem
import com.example.weatherinfo.repository.WeatherRepository
import com.example.weatherinfo.repository.WeatherRepositoryImpl
import com.example.weatherinfo.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers.IO
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherInfoAdapter = WeatherAdapter(this)
    private lateinit var viewModel: WeatherViewModel
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository: WeatherRepository =
            WeatherRepositoryImpl(RetrofitProvider.client)
        viewModel = WeatherViewModel(repository, IO)
        binding.lifecycleOwner = this
        binding.weatherViewModel = viewModel
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPref = getSharedPreferences(MY_PREF, MODE_PRIVATE)
        editor = sharedPref.edit()
        initializeRv()
        setObservers()
        getLocation()
    }

    private fun setObservers() {
        viewModel.weatherInfoList.observe(this) {
            with(weatherInfoAdapter) {
                addData(it as MutableList<WeatherItem>)
            }
        }
        viewModel.status.observe(this) {
            hideKeyBoard()
        }
        viewModel.noDataReceived.observe(this) {
            if (it) {
                Toast.makeText(this, R.string.city_error, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.savedCity.observe(this) {
            editor.putString(LAST_CITY, it).apply()
        }
    }

    private fun hideKeyBoard() {
        val inputManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun initializeRv() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = weatherInfoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    /**
     * Fetches the current location
     */
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: MutableList<Address>? =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val savedLocation = sharedPref.getString(LAST_CITY, null)
                        list?.get(0)?.locality?.let { viewModel.onClickSearch((if (!savedLocation.isNullOrEmpty()) savedLocation else it)) }
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    /**
     * Verifies the location is Enabled or Not
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Check the location permission is Enabled or Not
     */
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * Requests location permission
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    /**
     * Requests location permission Results
     */
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    companion object {
        const val LAST_CITY = "LastCity"
        const val MY_PREF = "myPref"
    }
}