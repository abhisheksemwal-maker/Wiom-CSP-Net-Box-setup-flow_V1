package com.wiom.partner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.wiom.partner.navigation.NavGraph
import com.wiom.partner.state.FlowViewModel
import com.wiom.partner.theme.Bg
import com.wiom.partner.theme.WiomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Bg) {
                    val navController = rememberNavController()
                    val vm: FlowViewModel = viewModel()
                    vm.init(this@MainActivity)
                    NavGraph(navController = navController, vm = vm)
                }
            }
        }
    }
}
