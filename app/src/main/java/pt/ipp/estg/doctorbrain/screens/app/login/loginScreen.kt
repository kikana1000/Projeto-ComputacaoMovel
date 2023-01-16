package pt.ipp.estg.doctorbrain.screens.app.login

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.R
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.ui.theme.LoginColor
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen


/**
 * Screen que permite fazer o login
 * @param auth autenticação do firebase
 * @param onLogin metodo a realizar a fazer o login
 * @param onSignUp metodo a realizar a fazer o register
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onGuest: ()->Unit
) {
    val fireBaseViewModel: FireBaseViewModel = viewModel()

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .background(LoginColor)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoiconsmall),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
            )
            Text(text = "Doctor Brain", style = TitleofScreen(), color = Color.Black)
        }
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var email by remember { mutableStateOf("") }
            val isEmailValid by derivedStateOf {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
            OutlinedTextField(value = email,
                placeholder = { Text("abc@domain.com") },
                label = { Text("Email Address") },
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = !isEmailValid,
                trailingIcon = {
                    if (email.isNotBlank()) {
                        IconButton(onClick = { email = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear Email"
                            )
                        }
                    }
                }
            )
            var password by remember { mutableStateOf(TextFieldValue()) }
            var isPasswordVisible by remember { mutableStateOf(false) }
            val isPasswordValid by derivedStateOf {
                password.text.length >= 8
            }
            OutlinedTextField(
                value = password,
                label = { Text("Password") },
                onValueChange = { password = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                isError = !isPasswordValid,
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Button(
                    onClick = {
                        fireBaseViewModel.signInUser(email, password.text)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "Logged In Successfully",
                                    Toast.LENGTH_SHORT
                                ).show();
                                onLogin()
                            }.addOnFailureListener {
                                Toast.makeText(context, "Failed to Login", Toast.LENGTH_SHORT)
                                    .show();
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    enabled = isPasswordValid && isEmailValid
                ) {
                    Text(text = "Login", modifier = Modifier.padding(5.dp))
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            color = Color.Black,
                            fontStyle = FontStyle.Italic,
                            text = "Forgotten password?",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { onSignUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Create Account",
                        modifier = Modifier.padding(5.dp),
                        color = Color.White
                    )
                }
                Button(
                    onClick = { onGuest() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Login as Guest",
                        modifier = Modifier.padding(5.dp),
                        color = Color.White
                    )
                }
            }


        }
    }
}