package net.mackk.metroview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mackk.metroview.ui.theme.MetroViewTheme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // call parent onCreate
        super.onCreate(savedInstanceState)

        // setup ui
        enableEdgeToEdge()
        setContent {
            ActivityWrapper { innerPadding ->
                ActivityRoot(innerPadding)
            }
        }
    }

}

@Composable
private fun ActivityRoot(innerPadding: PaddingValues) {

    // context
    val context = LocalContext.current

    // state
    var data by remember { mutableStateOf(emptyList<RailLine>()) }
    var isReloading by remember { mutableStateOf(true) }

    // effect
    LaunchedEffect(isReloading) {
        if (isReloading) {
            val result = withContext(Dispatchers.IO) {
                Api.getRailLines(context)
            }
            data = result
            isReloading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = data.toString())
    }
}

@Preview(showBackground = true)
@Composable
private fun ActivityRootPreview() {
    ActivityWrapper { innerPadding ->
        ActivityRoot(innerPadding)
    }
}
