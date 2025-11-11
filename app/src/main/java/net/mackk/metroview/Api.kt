package net.mackk.metroview

import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

object Api {

    val client: OkHttpClient

    init {
        // create builder
        val builder = OkHttpClient.Builder()

        // create logger
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        // set client
        client = builder.build()
    }

    fun getRailIncidents(context: Context): List<RailIncident> {
        // create request
        val request = Request.Builder()
            .url("https://api.wmata.com/Incidents.svc/json/Incidents")
            .header("api_key", context.getString(R.string.wmata_api_key))
            .get()
            .build()

        // make req
        val response: Response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // ensure body exists
        if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
            Log.e("ApiLogging", "Api call failed")
            return emptyList()
        }

        // parse req
        return Json.decodeFromString<RailIncidents>(responseBody).incidents
    }

    fun getRailLines(context: Context): List<RailLine> {
        // create request
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jLines")
            .header("api_key", context.getString(R.string.wmata_api_key))
            .get()
            .build()

        // make req
        val response: Response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // ensure body exists
        if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
            Log.e("ApiLogging", "Api call failed")
            return emptyList()
        }

        // parse req
        return Json.decodeFromString<RailLines>(responseBody).lines
    }

    fun getAllRailStations(context: Context): List<RailStation> {
        // create request
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jStations")
            .header("api_key", context.getString(R.string.wmata_api_key))
            .get()
            .build()

        // make req
        val response: Response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // ensure body exists
        if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
            Log.e("ApiLogging", "Api call failed")
            return emptyList()
        }

        // parse req
        return Json.decodeFromString<RailStations>(responseBody).stations
    }

    fun getRailStation(context: Context, stationCode: String): RailStation? {
        // create request
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jStationInfo?StationCode=${stationCode}")
            .header("api_key", context.getString(R.string.wmata_api_key))
            .get()
            .build()

        // make req
        val response: Response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // ensure body exists
        if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
            Log.e("ApiLogging", "Api call failed")
            return null
        }

        // parse req
        return Json.decodeFromString<RailStation>(responseBody)
    }

    fun getRoutes(context: Context): List<RailRoute> {
        // create request
        val request = Request.Builder()
            .url("https://api.wmata.com/TrainPositions/StandardRoutes?contentType=json")
            .header("api_key", context.getString(R.string.wmata_api_key))
            .get()
            .build()

        // make req
        val response: Response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        // ensure body exists
        if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
            Log.e("ApiLogging", "Api call failed")
            return emptyList()
        }

        // parse req
        return Json.decodeFromString<RailRoutes>(responseBody).routes
    }

}
