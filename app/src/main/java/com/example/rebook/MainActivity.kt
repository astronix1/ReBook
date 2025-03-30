package com.example.rebook

import android.R.attr.contentDescription
import android.graphics.Color
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.rebook.ui.theme.ReBookTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // From google-services.json
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        enableEdgeToEdge()
        setContent {
            ReBookTheme {
//                val navController = rememberNavController()
//                val auth = FirebaseAuth.getInstance()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(Modifier.padding(innerPadding), ::signInWithGoogle)
                }
            }
        }
    }
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(Exception::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("GoogleSignIn", "Success: ${auth.currentUser?.displayName}")
                        } else {
                            Log.e("GoogleSignIn", "Error: ${task.exception}")
                        }
                    }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Sign-in failed", e)
            }
        }
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}

@Composable
fun LoginScreen(modifier: Modifier = Modifier, signInWithGoogle: ()-> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            Text(text = "ReBook",
                modifier= Modifier.padding(top=10.dp),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize * 1.2f, // Increase size
                    fontWeight = FontWeight.Bold, // Make it bold
                    fontFamily = FontFamily.SansSerif// Default Roboto, or use explicit Roboto
                )

                )
            Image1()
            Spacer(modifier= Modifier.height(50.dp))
            GoogleBtn(signInWithGoogle)


        }

    }
}


@Composable
fun Image1(){
    Box(modifier=Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.bgg),
            contentDescription = "Background Image",
            modifier =Modifier.size(240.dp)
        )
        Image(
            painter = painterResource(R.drawable.top),
            contentDescription = "Top Image",
            modifier= Modifier.size(199.dp)
        )

    }
}
@Composable
fun Image2(
    modifier: Modifier= Modifier.fillMaxWidth()
        .height(200.dp),
    alignment: Alignment= Alignment.BottomCenter
){
    Image(
        painter = painterResource(id = R.drawable.bottom),
        contentDescription="bototm img"
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleBtn(signInWithGoogle: () -> Unit){
    var clicked by remember { mutableStateOf(false) }
    Surface(onClick = { clicked = true
        signInWithGoogle()},
        shape= RoundedCornerShape(4.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(
                start = 12.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "G button",
                tint = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            if(clicked){
                Text(
                    text = "Logging you in..."
                )
            }
            else{
                Text(
                    text = "Continue with Google"
                )
            }
            if(clicked){
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary

                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable

fun LoginScreenPreview() {
    ReBookTheme {
        LoginScreen(signInWithGoogle={})

    }
}
