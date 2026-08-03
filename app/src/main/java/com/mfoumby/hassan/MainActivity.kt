package com.mfoumby.hassan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mfoumby.hassan.common.ui.theme.HassanTheme
import com.mfoumby.hassan.ui.NavigationHost
import com.mfoumby.hassan.ui.NavigationHostViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val navigationViewModel: NavigationHostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            HassanTheme {
                NavigationHost(viewModel = navigationViewModel)
            }
        }
    }
}