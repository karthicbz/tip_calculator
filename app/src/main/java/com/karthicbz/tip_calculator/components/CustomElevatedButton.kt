package com.karthicbz.tip_calculator.components

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomElevatedButton(onClick: () -> Unit, icon: ImageVector, contentDescription: String) {
    ElevatedButton(onClick) {
        Icon(icon, contentDescription = contentDescription)
    }
}