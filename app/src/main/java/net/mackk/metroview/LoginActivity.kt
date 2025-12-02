package net.mackk.metroview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.google.firebase.FirebaseApp
import net.mackk.metroview.ui.theme.MetroViewTheme

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // call parent onCreate
        super.onCreate(savedInstanceState)

        // setup firebase
        FirebaseApp.initializeApp(this)
        val app = FirebaseApp.getInstance()
        Log.d("FirebaseLogging", "Firebase initialized: ${app.name}")

        // setup ui
        enableEdgeToEdge()
        setContent {
            ActivityRoot()
        }
    }

}

@Composable
private fun ActivityRoot() {

    // ui
    MetroViewTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Login()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ActivityRootPreview() {
    ActivityRoot()
}

@Composable
private fun Login() {

    // context
    val context = LocalContext.current

    // state
    val prefs = remember { context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE) }
    var username by remember {
        mutableStateOf(prefs.getString("username", "") ?: "Enter your username")
    }
    var password by remember { mutableStateOf("") }
    var loginRequested by remember { mutableStateOf(false) }
    var registerRequested by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // derived state
    val requestInProgress = loginRequested || registerRequested
    val validUsername = username.matches("^(\\S)+@(\\S)+\$".toRegex())
    val validPassword = password.length >= 6
    val validInput = validUsername && validPassword

    // helpers
    fun clean(str: String): String {
        return str.filter({ it -> !it.isWhitespace() })
    }

    // handlers
    fun onSuccessfulAuth() {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("username", username)
        context.startActivity(intent)
    }

    // effects
    LaunchedEffect(requestInProgress) {
        if (loginRequested) {
            try {
                Auth.login(username, password)
                onSuccessfulAuth()
            } catch (e: Exception) {
                error = e.message
            } finally {
                password = ""
                loginRequested = false
            }
        } else if (registerRequested) {
            try {
                Auth.register(username, password)
                onSuccessfulAuth()
            } catch (e: Exception) {
                error = e.message
            } finally {
                password = ""
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

        Spacer(Modifier.height(12.dp))

        // buttons
        Column(modifier = Modifier.width(IntrinsicSize.Min)) {
            // login button
            Button(
                onClick = {
                    loginRequested = true
                    prefs.edit { putString("username", username) }
                }, modifier = Modifier.fillMaxWidth(), enabled = !requestInProgress && validInput
            ) {
                Text(stringResource(R.string.login_login))
            }

            Spacer(Modifier.height(6.dp))

            // register button
            Button(
                onClick = {
                    registerRequested = true
                }, modifier = Modifier.fillMaxWidth(), enabled = !requestInProgress && validInput
            ) {
                Text(stringResource(R.string.login_register))
            }
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
