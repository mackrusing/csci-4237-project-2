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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
//    val username = activity?.intent?.getStringExtra("username")!!
    var user: String? by remember { mutableStateOf(null) }

    // state
    val navController = rememberNavController()
//    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    var currentNavBarScreen by rememberSaveable { mutableStateOf("home") }

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
                if (user != null) {
                    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                        NavigationBarItem(
                            selected = currentNavBarScreen == "home",
                            onClick = {
                                navController.navigate(HomeRoute)
                                currentNavBarScreen = "home"
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(R.string.nav_home)) },
                        )
                        NavigationBarItem(
                            selected = currentNavBarScreen == "stations",
                            onClick = {
                                navController.navigate(StationsRoute)
                                currentNavBarScreen = "stations"
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(R.string.nav_stations)) },
                        )
                        NavigationBarItem(
                            selected = currentNavBarScreen == "profile",
                            onClick = {
                                navController.navigate(ProfileRoute)
                                currentNavBarScreen = "profile"
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(stringResource(R.string.nav_profile)) },
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = LoginRoute) {
                composable<LoginRoute> {
                    LoginScreen(innerPadding, navController, {
                        user = it
                        currentNavBarScreen = "home"
                        navController.navigate(HomeRoute)
                    })
                }
                composable<RegisterRoute> {
                    RegisterScreen(innerPadding, {
                        user = it
                        currentNavBarScreen = "home"
                        navController.navigate(HomeRoute)
                    })
                }
                composable<HomeRoute> {
                    HomeScreen(innerPadding, navController, railNetwork, user ?: "")
                }
                composable<StationsRoute> {
                    StationsScreen(innerPadding, navController, railNetwork)
                }
                composable<ProfileRoute> {
                    ProfileScreen(innerPadding, user ?: "", {
                        user = null
                        navController.navigate(LoginRoute)
                    })
                }
                composable<StationRoute> {
                    val args = it.toRoute<StationRoute>()
                    StationScreen(
                        innerPadding = innerPadding,
                        railNetwork = railNetwork,
                        stationCode = args.stationCode,
                        username = user ?: "",
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
