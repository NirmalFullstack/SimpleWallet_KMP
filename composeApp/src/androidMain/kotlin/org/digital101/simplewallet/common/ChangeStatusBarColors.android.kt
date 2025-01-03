package org.digital101.simplewallet.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
actual fun ChangeStatusBarColors(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color)
}