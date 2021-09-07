package com.wy.demo.compose.app

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by Arthur on 2021/8/16.
 */

data class Elevations(val card: Dp = 0.dp, val default: Dp = 0.dp)

val LocalElevations = compositionLocalOf { Elevations() }