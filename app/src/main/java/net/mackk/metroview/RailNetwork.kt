package net.mackk.metroview

import android.content.Context

data class RailNetwork(
    val stations: Map<String, RailStation> = emptyMap(),
    val combinedStations: Map<String, CombinedStation> = emptyMap(),
    val incidents: List<RailIncident> = emptyList(),
) {

    companion object {

        fun init(context: Context): RailNetwork {
            // create stations
            val stations = mutableMapOf<String, RailStation>()
            for (line in Api.getAllRailStations(context)) {
                stations[line.code] = line
            }

            // create combined stations
            val combinedStations = mutableMapOf<String, CombinedStation>()
            for (station in stations.values) {
                val address = station.address
                val codes = mutableListOf(station.code)
                val lat = station.lat
                val lon = station.lon
                val lineCodes = mutableListOf(station.lineCode1)
                val name = station.name

                if (station.lineCode2 != null) lineCodes.add(station.lineCode2)
                if (station.lineCode3 != null) lineCodes.add(station.lineCode3)
                if (station.lineCode4 != null) lineCodes.add(station.lineCode4)

                if (!station.stationTogether1.isNullOrEmpty()) {
                    val st = stations[station.stationTogether1]!!
                    codes.add(st.code)
                    lineCodes.add(st.lineCode1)
                    if (st.lineCode2 != null) lineCodes.add(st.lineCode2)
                    if (st.lineCode3 != null) lineCodes.add(st.lineCode3)
                    if (st.lineCode4 != null) lineCodes.add(st.lineCode4)
                }

                if (!station.stationTogether2.isNullOrEmpty()) {
                    println(station.stationTogether2)
                    val st = stations[station.stationTogether2]!!
                    codes.add(st.code)
                    lineCodes.add(st.lineCode1)
                    if (st.lineCode2 != null) lineCodes.add(st.lineCode2)
                    if (st.lineCode3 != null) lineCodes.add(st.lineCode3)
                    if (st.lineCode4 != null) lineCodes.add(st.lineCode4)
                }

                // create value
                val combinedStation = CombinedStation(address, codes, lat, lon, lineCodes, name)
                combinedStations[combinedStation.getCombinedCode()] = combinedStation
            }

            // get incidents
            val incidents = Api.getRailIncidents(context)

            // construct value
            return RailNetwork(
                stations,
                combinedStations,
                incidents,
            )
        }
    }
}

data class CombinedStation(
    val address: Address,
    val codes: List<String>,
    val lat: Double,
    val lon: Double,
    val lineCodes: List<String>,
    val name: String,
) {

    fun getCombinedCode(): String {
        return codes.sorted().joinToString("-")
    }
}
