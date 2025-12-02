package net.mackk.metroview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    user: String,
    logout: () -> Unit,
) {

    // ui
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Logged in as $user", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(12.dp))
        Button(onClick = { logout() }) {
            Text(stringResource(R.string.profile_logout))
        }
    }

}
