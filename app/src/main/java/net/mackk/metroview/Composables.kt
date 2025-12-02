package net.mackk.metroview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MetroIcon(modifier: Modifier = Modifier, lineCode: String) {
    when (lineCode) {
        "BL" -> Image(
            painter = painterResource(id = R.drawable.blue),
            contentDescription = null,
            modifier = modifier
        )

        "RD" -> Image(
            painter = painterResource(id = R.drawable.red),
            contentDescription = null,
            modifier = modifier
        )

        "SV" -> Image(
            painter = painterResource(id = R.drawable.silver),
            contentDescription = null,
            modifier = modifier
        )

        "OR" -> Image(
            painter = painterResource(id = R.drawable.orange),
            contentDescription = null,
            modifier = modifier
        )

        "GR" -> Image(
            painter = painterResource(id = R.drawable.green),
            contentDescription = null,
            modifier = modifier
        )

        "YL" -> Image(
            painter = painterResource(id = R.drawable.yellow),
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Composable
fun IncomingTrain(nextTrain: NextTrain, railNetwork: RailNetwork) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .height(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val station = railNetwork.stations[nextTrain.destinationCode]
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MetroIcon(modifier = Modifier.fillMaxHeight(), lineCode = nextTrain.line)
            Spacer(Modifier.width(12.dp))
            Text(
                station?.name ?: nextTrain.destinationShort,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            nextTrain.min, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium
        )
    }
}
