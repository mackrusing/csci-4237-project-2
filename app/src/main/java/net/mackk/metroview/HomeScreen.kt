package net.mackk.metroview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
object HomeRoute

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    railNetwork: RailNetwork,
    username: String,
) {

    // context
    val context = LocalContext.current

    // derived
    val incidents = railNetwork.incidents.sortedBy { it.dateUpdated }.reversed()

    // state
    var userFavorites by remember { mutableStateOf(emptyList<String>()) }

    // effects
    LaunchedEffect(Unit) {
        userFavorites = withContext(Dispatchers.IO) { Database.getSavedStations(username) }
    }

    // ui
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.home_status), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(incidents) { IncidentCard(it) }
        }
        Spacer(Modifier.height(24.dp))
        Text(stringResource(R.string.home_saved), style = MaterialTheme.typography.titleLarge)
        LazyColumn {
            items(userFavorites) {
                val station = railNetwork.combinedStations[it]
                if (station != null) {
                    SavedStation(navController, railNetwork, station)
                }
            }
        }
    }

}

@Composable
private fun IncidentCard(incident: RailIncident) {

    // ui
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
        ) {
            Row() {
                val style = MaterialTheme.typography.labelSmall
                val datetime = LocalDateTime.parse(incident.dateUpdated)
                Text(incident.type, style = style, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(4.dp))
                Text("\u00B7", style = style)
                Spacer(Modifier.width(4.dp))
                Text(
                    "last updated ${
                        datetime.toLocalDate()
                            .format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                    } at ${
                        datetime.toLocalTime().truncatedTo(java.time.temporal.ChronoUnit.MINUTES)
                    }", style = style
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(incident.description, style = MaterialTheme.typography.bodyMedium)
        }
    }

}

@Composable
private fun SavedStation(
    navController: NavController,
    railNetwork: RailNetwork,
    station: CombinedStation,
) {

    // ui
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable(enabled = true, onClick = {
                navController.navigate(
                    StationRoute(station.getCombinedCode()),
                )
            })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(station.name, style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (code in station.lineCodes) {
                    MetroIcon(modifier = Modifier.height(24.dp), lineCode = code)
                }
            }
        }

    }

}
