package net.mackk.metroview

import android.content.Context

class Network(
    context: Context,
    var stations: MutableMap<String, RailStation> = HashMap(),
) {

    // setup object
    init {
        for (line in Api.getAllRailStations(context)) {
            stations.put(line.code, line)
        }
    }

}
