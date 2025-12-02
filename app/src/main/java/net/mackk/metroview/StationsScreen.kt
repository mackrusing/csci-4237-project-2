package net.mackk.metroview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object StationsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationsScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    railNetwork: RailNetwork,
) {

    // derived
    val allStations: List<CombinedStation> =
        railNetwork.combinedStations.values.toList().sortedBy { it.name }

    // state
    var query by remember { mutableStateOf("") }
    var resStations by remember { mutableStateOf(allStations) }

    // ui
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = {
                        resStations = allStations.filter { iit ->
                            iit.name.lowercase().contains(query.lowercase())
                        }
                        query = it
                    },
                    onSearch = { },
                    expanded = true,
                    onExpandedChange = { },
                    placeholder = { Text(stringResource(R.string.stations_search)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = true,
            onExpandedChange = { },
        ) {
            LazyColumn {
                items(count = resStations.size) { index ->
                    val station = resStations[index]
                    ListItem(
                        headlineContent = { Text(station.name) },
                        leadingContent = {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (code in station.lineCodes) {
                                    MetroIcon(modifier = Modifier.height(24.dp), lineCode = code)
                                }
                            }
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(
                                    StationRoute(station.getCombinedCode()),
                                )
                            }
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp, vertical = 4.dp,
                            ),
                    )
                }
            }
        }
    }

}
