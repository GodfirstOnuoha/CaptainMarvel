package com.godfirst.captainmarvel.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.godfirst.captainmarvel.R
import com.godfirst.captainmarvel.data.ComicsRetriever
import com.godfirst.captainmarvel.model.Comics
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val comicsRetriever = ComicsRetriever()
    private val callback = object : Callback<Comics> {
        override fun onResponse(p0: Call<Comics>, p1: Response<Comics>) {
            p1.isSuccessful.let {
                //val resultList = p1 ?: emptyList()
            }
        }

        override fun onFailure(p0: Call<Comics>, p1: Throwable) {
            Log.e(TAG, "onFailure: Problem calling Marvel API \n${p1.message}", p1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

        if (isNetworkConnected()) {
            retrieveComics()
        } else {
            AlertDialog.Builder(this).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }

    private fun retrieveComics() {
        val mainActivityJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage(throwable.message)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }

        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            val resultList = ComicsRetriever().getComics()
            Log.d(TAG, "retrieveComics: \n$resultList")
            Toast.makeText(
                applicationContext,
                "Retrieved Comics: \n$resultList",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}