package pt.ipp.estg.doctorbrain.screens.app.login


import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.ipp.estg.doctorbrain.R
import pt.ipp.estg.doctorbrain.firestoreFun.FireBaseViewModel
import pt.ipp.estg.doctorbrain.models.User
import pt.ipp.estg.doctorbrain.ui.theme.LoginColor
import pt.ipp.estg.doctorbrain.ui.theme.TitleofScreen

/**
 * Screen que permite fazer o registro
 * @param auth autenticação do firebase
 * @param db base de dados da firebase
 * @param onSignUp metodo a realizar a fazer o register
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(onSignUp: () -> Unit) {
    val fireBaseViewModel: FireBaseViewModel = viewModel()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .background(LoginColor)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
            var name by remember { mutableStateOf("") }
            val isNameValid by derivedStateOf {
                name.length >= 3
            }
            OutlinedTextField(value = name,
                placeholder = { Text(text = "Full Name") },
                label = { Text("Your Name") },
                onValueChange = { name = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = !isNameValid,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )
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
            var contact by remember { mutableStateOf("+351") }
            val isContactValid by derivedStateOf {
                Patterns.PHONE.matcher(contact).matches()
            }
            OutlinedTextField(
                value = contact,
                placeholder = { Text(text = "+351 91XXXXXXX") },
                label = { Text("Phone Number") },
                onValueChange = { contact = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = !isContactValid
            )
            var studentNumber by remember { mutableStateOf("") }
            val isStudentNumberValid by derivedStateOf {
                studentNumber.length ==7
            }
            OutlinedTextField(value = studentNumber,
                placeholder = { Text(text = "Student Number") },
                label = { Text("Your Student Number") },
                onValueChange = { studentNumber = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = !isStudentNumberValid,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.Black)
            )
            var password by remember { mutableStateOf(TextFieldValue("")) }
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


            Button(
                onClick = {
                    fireBaseViewModel.signUpUser(name, email, contact, password.text, studentNumber)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Signed Up Successfully", Toast.LENGTH_SHORT)
                                .show();
                            onSignUp()
                        }.addOnFailureListener {
                        Toast.makeText(context, "Failed to Sign Up", Toast.LENGTH_SHORT).show();
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                enabled = isEmailValid && isPasswordValid && isContactValid && isNameValid
            ) {
                Text(
                    text = "Register",
                    modifier = Modifier.padding(5.dp),
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}
