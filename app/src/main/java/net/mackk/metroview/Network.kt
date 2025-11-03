package net.mackk.metroview

import android.content.Context

class Network {

    val stations: HashMap<String, RailStation> = HashMap()
    val lines: HashMap<String, RailLine> = HashMap()
    val routes: HashMap<String, RailRoute> = HashMap()

    fun refresh(context: Context) {
        Api.getRailLines(context)
    }

}
