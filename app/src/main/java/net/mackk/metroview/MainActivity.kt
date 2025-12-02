package net.mackk.metroview

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mackk.metroview.ui.theme.MetroViewTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // call parent onCreate
        super.onCreate(savedInstanceState)

        // setup ui
        enableEdgeToEdge()
        setContent {
            ActivityRoot()
        }
    }

}

private enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {

    HOME(route = "home", label = "Home", icon = Icons.Default.Home),

//    LINES(route = "lines", label = "Lines", icon = Icons.Default.LocationOn),

    STATIONS(route = "stations", label = "Stations", icon = Icons.Default.Info),

}

@Composable
private fun ActivityRoot() {

    // context
    val context = LocalContext.current
    val activity = when (context) {
        is Activity -> context
        else -> null
    }

    // config
    val startDestination = Destination.HOME

    // values from prev activity
    val username = activity?.intent?.getStringExtra("username")!!

    // state
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    // shared state
    var railNetwork by remember { mutableStateOf(RailNetwork()) }

    // effects
    LaunchedEffect(Unit) {
        railNetwork = withContext(Dispatchers.IO) { RailNetwork.init(context) }
    }

    // ui
    MetroViewTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    Destination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedDestination == index,
                            onClick = {
                                navController.navigate(route = destination.route)
                                selectedDestination = index
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(destination.label) },
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = startDestination.route) {
                Destination.entries.forEach { destination ->
                    composable(destination.route) {
                        when (destination) {
                            Destination.HOME -> HomeScreen(innerPadding, navController, railNetwork, username)
                            Destination.STATIONS -> StationsScreen(
                                innerPadding, navController, railNetwork
                            )
                        }
                    }
                }
                composable<StationRoute> {
                    val args = it.toRoute<StationRoute>()
                    StationScreen(
                        innerPadding = innerPadding,
                        railNetwork = railNetwork,
                        stationCode = args.stationCode,
                        username = username,
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ActivityRootPreview() {
    ActivityRoot()
}
