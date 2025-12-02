package net.mackk.metroview

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
data class RailIncidents(
    @SerialName("Incidents") val incidents: List<RailIncident>,
)

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
data class RailIncident(
    @SerialName("IncidentID") val id: String,
    @SerialName("IncidentType") val type: String,
    @SerialName("Description") val description: String,
    @SerialName("LinesAffected") val linesAffected: String,
    @SerialName("DateUpdated") val dateUpdated: String,
)

@Serializable
data class RailLines(
    @SerialName("Lines") val lines: List<RailLine>,
)

@Serializable
data class RailLine(
    @SerialName("LineCode") val code: String,
    @SerialName("DisplayName") val name: String,
    @SerialName("StartStationCode") val startStationCode: String,
    @SerialName("EndStationCode") val endStationCode: String,
    @SerialName("InternalDestination1") val internalDestination1: String,
    @SerialName("InternalDestination2") val internalDestination2: String,
)

@Serializable
data class RailStations(
    @SerialName("Stations") val stations: List<RailStation>,
)

@Serializable
data class RailStation(
    @SerialName("Address") val address: Address,
    @SerialName("Code") val code: String,
    @SerialName("Lat") val lat: Double,
    @SerialName("LineCode1") val lineCode1: String,
    @SerialName("LineCode2") val lineCode2: String?,
    @SerialName("LineCode3") val lineCode3: String?,
    @SerialName("LineCode4") val lineCode4: String?,
    @SerialName("Lon") val lon: Double,
    @SerialName("Name") val name: String,
    @SerialName("StationTogether1") val stationTogether1: String?,
    @SerialName("StationTogether2") val stationTogether2: String?,
)

@Serializable
data class Address(
    @SerialName("Street") val street: String,
    @SerialName("City") val city: String,
    @SerialName("State") val state: String,
    @SerialName("Zip") val zip: String,
)

@Serializable
data class RailRoutes(
    @SerialName("StandardRoutes") val routes: List<RailRoute>,
)

@Serializable
data class RailRoute(
    @SerialName("LineCode") val lineCode: String,
    @SerialName("TrackCircuits") val trackCircuits: List<RailTrackCircuit>,
    @SerialName("TrackNum") val trackNum: Int,
)

@Serializable
data class RailTrackCircuit(
    @SerialName("CircuitId") val id: Int,
    @SerialName("SeqNum") val seqNum: Int,
    @SerialName("StationCode") val stationCode: String?,
)

@Serializable
data class NextTrains(
    @SerialName("Trains") val trains: List<NextTrain>,
)

@Serializable
data class NextTrain(
    @SerialName("Car") val cars: String?,
    @SerialName("Destination") val destinationShort: String,
    @SerialName("DestinationCode") val destinationCode: String?,
    @SerialName("DestinationName") val destinationName: String?,
    @SerialName("Group") val group: String,
    @SerialName("Line") val line: String,
    @SerialName("LocationCode") val locationCode: String,
    @SerialName("LocationName") val locationName: String,
    @SerialName("Min") val min: String,
)
