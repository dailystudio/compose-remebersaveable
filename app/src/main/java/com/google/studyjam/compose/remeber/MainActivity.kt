package com.dailystudio.compose.notebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Home() }
    }

}

class NavOption(val route: String, val label: String) {
}

@Composable
fun Home() {
    val options = mutableListOf<NavOption>().apply {
        for (i in 0 until 4) {
            add(NavOption("route$i", "Tab $i"))
        }
    }

    val navController = rememberNavController()
    val (navIndex, setNavIndex) = remember { mutableStateOf(0)}
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                for ((i, opt) in options.withIndex()) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(opt.label) },
                        selected = currentRoute == opt.route,
                        onClick = {
//                            navController.navigate(naviOption.route)
                            setNavIndex(i)
                        }
                    )
                }
            }
        }
    ) {
        PreviewScreen(navOption = options[navIndex])

        /*NavHost(navController, startDestination = options[0].route) {
            for (o in options) {
                composable(o.route) { PreviewScreen(navOption = o)}
            }
        }*/
    }
}


@Composable
fun PreviewScreen(navOption: NavOption) {
    val (count, setCount) = rememberSaveable(navOption.route) {
        mutableStateOf(0)
    }

    Column {
        Text(text = navOption.label)
        Text(text = count.toString())
        Button(onClick = { setCount(count + 1) }) {
            Text("Plus 1")
        }
    }

}