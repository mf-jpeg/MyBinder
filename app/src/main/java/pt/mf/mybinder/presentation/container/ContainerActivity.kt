package pt.mf.mybinder.presentation.container

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.mf.mybinder.R
import pt.mf.mybinder.presentation.theme.MyBinderTheme
import pt.mf.mybinder.presentation.theme.Theme
import pt.mf.mybinder.utils.Navigation
import pt.mf.mybinder.utils.Screen
import pt.mf.mybinder.utils.Utils

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class ContainerActivity : ComponentActivity() {
    companion object {
        const val TAG = "ContainerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBinderTheme {
                val viewModel = viewModel<ContainerViewModel>()
                SetupLayout(rememberNavController(), viewModel)
            }
        }
    }
}

@Composable
fun SetupLayout(navController: NavHostController, viewModel: ContainerViewModel) {
    val curBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = curBackStackEntry?.destination?.route ?: Screen.DeckSelectScreen.route

    Scaffold(
        topBar = { TopBar(getTitleForRoute(currentDestination)) },
        bottomBar = { BottomBar(navController, viewModel) },
        modifier = Modifier.fillMaxSize(),
        content = { Content(padding = it, navController) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Theme.getPrimary(),
            titleContentColor = Color.White
        ),
        title = { Text(title) }
    )
}

fun getTitleForRoute(route: String): String {
    return when (route) {
        Screen.DeckSelectScreen.route -> Utils.getString(R.string.top_bar_deck)
        Screen.BinderScreen.route -> Utils.getString(R.string.top_bar_binder)
        Screen.CardSearchScreen.route -> Utils.getString(R.string.top_bar_search)
        Screen.SettingsScreen.route -> Utils.getString(R.string.top_bar_settings)
        else -> Utils.getString(R.string.app_name)
    }
}

@Composable
fun Content(padding: PaddingValues, navController: NavHostController) {
    Navigation(padding, navController)
}

@Composable
fun BottomBar(navController: NavController, viewModel: ContainerViewModel) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(Icons.Default.Edit, modifier = Modifier.weight(1f)) {
                viewModel.handleDeckOption(navController)
            }

            BottomNavItem(Icons.Default.Menu, modifier = Modifier.weight(1f)) {
                viewModel.handleBinderOption(navController)
            }

            BottomNavItem(Icons.Default.Search, modifier = Modifier.weight(1f)) {
                viewModel.handleSearchOption(navController)
            }

            BottomNavItem(Icons.Default.Settings, modifier = Modifier.weight(1f)) {
                viewModel.handleSettingsOption(navController)
            }
        }
    }
}

@Composable
fun BottomNavItem(image: ImageVector, modifier: Modifier, action: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
            .fillMaxSize()
            .clickable { action.invoke() }
    ) {
        Icon(imageVector = image, contentDescription = null)
    }
}