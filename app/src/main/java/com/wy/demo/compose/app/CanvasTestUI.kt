package com.wy.demo.compose.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp

/**
 * Created by Arthur on 2021/8/22.
 */
@Composable
fun CanvasApp() {
    Box(modifier = Modifier.fillMaxSize()) {
        CanvasSample1()
        CanvasSample2()

    }


}

@Composable
fun CanvasSample1() {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val w = size.width
        val h = size.height
        drawLine(start = Offset.Zero,
            end = Offset(w, h),
            color = Color.Blue,
            strokeWidth = 5f
            )
    }

}

@Composable
fun CanvasSample2() {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val w = size.width
        val h = size.height

        drawCircle(color = Color.Red,
            center = Offset(x = w /2 , y = h / 2),
            radius = size.minDimension / 4
            )
//
//        inset(50f, 50f) {
//
//            drawRect(color = Color.Blue, size = size / 2f )
//
//        }
//        rotate(degrees = 45F) {
//            drawRect(
//                color = Color.Gray,
//                topLeft = Offset(x = w / 3F, y = h / 3F),
//                size = size / 3F
//            )
//        }

        withTransform({
            translate(left = w / 5F)
            rotate(degrees = 45F)
        }) {
            drawRect(
                color = Color.Gray,
                topLeft = Offset(x = w / 3F, y = h / 3F),
                size = size / 3F
            )

            drawRect(
                color = Color.Blue,
                topLeft = Offset(x = w / 2F, y = h / 3F),
                size = size / 3F
            )
        }

    }

}