package net.mackk.metroview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.mackk.metroview.ui.theme.MetroViewTheme

@Composable
fun ActivityWrapper(root: @Composable ((PaddingValues) -> Unit)) {
    MetroViewTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            root(innerPadding)
        }
    }
}
