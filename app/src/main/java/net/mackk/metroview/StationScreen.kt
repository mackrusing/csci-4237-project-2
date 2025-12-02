package net.mackk.metroview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class StationRoute(val stationCode: String)

@Composable
fun StationScreen(
    innerPadding: PaddingValues,
    railNetwork: RailNetwork,
    stationCode: String,
    username: String,
) {

    // context
    val context = LocalContext.current

    // derived
    val station = railNetwork.combinedStations[stationCode]!!

    // state
    var trainTimes by remember { mutableStateOf<List<NextTrain>>(listOf()) }
    var fav by remember { mutableStateOf(false) }

    // effects
    LaunchedEffect(Unit) {
        while (true) {
            trainTimes = withContext(Dispatchers.IO) { Api.getNextTrains(context, station.codes) }
            delay(5_000)
        }
    }
    LaunchedEffect(fav) {
        if (fav) {
            withContext(Dispatchers.IO) {
                Database.toggleStation(
                    username,
                    station.getCombinedCode()
                )
            }
            fav = false
        }
    }

    // ui
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        StationHeader(station)
        Spacer(Modifier.height(24.dp))
        Button(onClick = { fav = true }) { Text(stringResource(R.string.station_favorite)) }
        Spacer(Modifier.height(24.dp))
        Text(
            stringResource(R.string.station_next),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(trainTimes) {
                IncomingTrain(it, railNetwork)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            stringResource(R.string.station_note),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )
    }

}

@Composable
private fun StationHeader(station: CombinedStation) {

    // ui
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            station.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.height(36.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (code in station.lineCodes) {
                MetroIcon(modifier = Modifier.fillMaxHeight(), lineCode = code)
            }
        }

    }

}
