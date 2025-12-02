package net.mackk.metroview

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import kotlinx.serialization.Serializable

@Serializable
object RegisterRoute

@Composable
fun RegisterScreen(
    innerPadding: PaddingValues,
    login: (String?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Register(login)
    }
}

@Composable
private fun Register(login: (String?) -> Unit) {

    // context
    val context = LocalContext.current

    // state
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var registerRequested by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // derived state
    val requestInProgress = registerRequested
    val validUsername = username.matches("^(\\S)+@(\\S)+\$".toRegex())
    val validPassword = password.length >= 6
    val validMatch = password == password2
    val validInput = validUsername && validPassword && validMatch

    // helpers
    fun clean(str: String): String {
        return str.filter({ it -> !it.isWhitespace() })
    }

    // handlers
    fun onSuccessfulAuth() {
        login(username)
    }

    // effects
    LaunchedEffect(requestInProgress) {
        if (registerRequested) {
            try {
                Auth.register(username, password)
                onSuccessfulAuth()
            } catch (e: Exception) {
                error = e.message
            } finally {
                password = ""
                password2 = ""
                registerRequested = false
            }
        }
    }

    // ui
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(48.dp))

        // username
        Column {
            OutlinedTextField(
                value = username,
                onValueChange = { username = clean(it); error = null },
                label = { Text(stringResource(R.string.login_email)) },
                placeholder = { Text("jane.doe@example.com") },
                singleLine = true
            )
            Text(
                text = if (username.isNotBlank() && !validUsername) stringResource(R.string.login_email_error) else "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(Modifier.height(6.dp))

        // password
        Column {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = null },
                label = { Text(stringResource(R.string.login_password)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = if (password.isNotBlank() && !validPassword) stringResource(R.string.login_password_error) else "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(Modifier.height(6.dp))

        // password 2
        Column {
            OutlinedTextField(
                value = password2,
                onValueChange = { password2 = it; error = null },
                label = { Text(stringResource(R.string.register_password_confirm)) },
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = if (!validMatch) stringResource(R.string.register_confirm_error) else "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(Modifier.height(12.dp))

        // signup button
        Button(
            onClick = {
                registerRequested = true
            }, enabled = !requestInProgress && validInput
        ) {
            Text(stringResource(R.string.register_signup))
        }

        Spacer(Modifier.height(24.dp))

        // error text
        Text(
            error ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )

    }

}
