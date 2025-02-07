package pt.mf.mybinder.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.mf.mybinder.presentation.theme.MyBinderTheme

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MyBinderTheme { SetupLayout() } }
    }
}

@Composable
fun SetupLayout() {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        SetupContent(padding)
    }
}

@Composable
fun SetupContent(padding: PaddingValues) {
    Greet(modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}

@Composable
fun Greet(modifier: Modifier = Modifier) {
    Text(text = "Hello, world!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyBinderTheme { SetupLayout() }
}