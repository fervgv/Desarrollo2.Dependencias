package com.example.desarrollo2.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.desarrollo2.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(auth: FirebaseAuth, navigateToHome: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(){
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "",
                tint = Color.Black,
                modifier = Modifier.padding(vertical = 24.dp).size(24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Text("Correo Electronico", color = Black, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = UnselectedField,
//                focusedContainerColor = SelectedField
//            )
        )
        Spacer(Modifier.height(48.dp))
        Text("Contraseña", color = Black, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = UnselectedField,
//                focusedContainerColor = SelectedField
//            )
        )
        Spacer(Modifier.height(48.dp))
        Button(onClick = {
            //tambien se puede dejar al usuario registrarse anonimamente y dejarle usar la app
            //auth.signInAnonymously()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    //registrado
                    navigateToHome()
                    Log.i("sparkle", "SIGN UP OK")
                } else {
                    //error
                    Log.i("sparkle", "SIGN UP NO")
                }
            }
        }) {
            Text(text = "Registrarse")
        }
    }
}