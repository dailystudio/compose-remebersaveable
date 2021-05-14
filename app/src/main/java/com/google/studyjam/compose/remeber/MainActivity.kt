package com.google.studyjam.compose.remeber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Home() }
    }

}

class NavOption(val route: String,
                val label: String) {
}

class CountsViewModel : ViewModel() {

    val counts = mapOf(
        "0" to MutableLiveData(0),
        "1" to MutableLiveData(0),
        "2" to MutableLiveData(0),
        "3" to MutableLiveData(0)
    )

    fun onCountsChanged(route: String, newCount: Int) {
        val count = counts[route] ?: return

        count.value = newCount
    }
}

@Composable
fun Home() {
    val countsViewModel: CountsViewModel = viewModel()

    val options = mutableListOf<NavOption>().apply {
        for (route in countsViewModel.counts.keys) {
            add(NavOption(route, "Tab $route"))
        }
    }

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                for (opt in options) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(opt.label) },
                        selected = currentRoute == opt.route,
                        onClick = {
                            navController.navigate(opt.route)
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = options[0].route) {
            for (o in options) {
                composable(o.route) {
                    val count by countsViewModel.counts[o.route]!!.observeAsState()

                    PreviewScreen(navOption = o,
                        count!!
                    ) {
                        countsViewModel.onCountsChanged(o.route,
                            it + 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreviewScreen(navOption: NavOption,
                  count: Int,
                  countUpdate: (Int) -> Unit
) {
    Column {
        Text(text = navOption.label)
        Text(text = count.toString())
        Button(onClick = { countUpdate(count) }) {
            Text("Add 1")
        }
    }

}