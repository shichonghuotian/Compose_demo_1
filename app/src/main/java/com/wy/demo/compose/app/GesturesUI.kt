package com.wy.demo.compose.app

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Created by Arthur on 2021/8/22.
 */
@ExperimentalMaterialApi
@Composable
fun GesturesApp() {

//    ClickableSample()

//    ScrollBoxes()

//    ScrollableSample()
//    AutoNestedScrollSample()


//    DraggableSample()
//    DraggableSample2()

//    SwipeableSample()

    TransformableSample()
}


@Composable
fun ClickableSample() {

    val count = remember {
        mutableStateOf(0)
    }

    Text(
        text = count.value.toString(),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {

                count.value += 1
            },
        textAlign = TextAlign.Center
    )


}

@Composable
fun ScrollBoxes() {

    val state = rememberScrollState()
//    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    val scope = rememberCoroutineScope()


    Column(
        Modifier
            .background(Color.Red)
            .size(100.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(state)
    ) {


        repeat(10) {

            Text(text = "Item $it", modifier = Modifier
                .padding(2.dp)
                .clickable {

                    scope.launch {
                        state.animateScrollTo(100)

                    }

                })
        }
    }
}

@Composable
fun ScrollableSample() {
//
//    rememberScrollableState
//    scrollable ???????????????????????????????????????????????? scrollable ???????????????????????????????????????????????????
    var offset by remember {
        mutableStateOf(0f)
    }

    Box(
        modifier = Modifier
            .size(150.dp)
            .scrollable(orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    offset += delta

                    delta
                })
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(offset.toString())


    }

}

//??????????????????
//??????????????????????????????????????????????????????????????????????????? nestedScroll ????????????????????????????????????????????????????????????
@Composable
fun AutoNestedScrollSample() {
    val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)

    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
    ) {
        Column {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Scroll here",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .padding(24.dp)
                            .height(150.dp)
                    )
                }
            }
        }

    }

}

@Composable
fun DraggableSample() {

    var offsetX by remember {
        mutableStateOf(0f)
    }

    Text(text = "Drag me",
        Modifier
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .draggable
                (
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->

                    offsetX += delta

                }
            ))
}


//??????pointerInput
@Composable
fun DraggableSample2() {

    var offsetX by remember {
        mutableStateOf(0f)
    }
    var offsetY by remember {
        mutableStateOf(0f)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .offset {
                    IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                }
                .size(50.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {

                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()//????????????
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y


                    }

                }

        ) {
        }


    }

}

//?????? swipeable ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
@ExperimentalMaterialApi
@Composable
fun SwipeableSample() {

    val width = 96.dp
    val squareSize = 48.dp
    val swipeableState = rememberSwipeableState(initialValue = 0)

    val sizePx = with(LocalDensity.current) {

        squareSize.toPx()
    }

    //??????   Maps anchor points (in px) to states
    val anchors = mapOf(0f to 0, sizePx to 1)


    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ ->
                    FractionalThreshold(0.3f)
                },
                orientation = Orientation.Horizontal
            )
            .background(Color.LightGray)
    ) {

        Box(
            Modifier
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .size(squareSize)
                .background(Color.Blue)) {

        }

    }

}

//????????????????????????????????????????????????????????????????????????????????? transformable ????????????????????????????????????????????????????????????????????????
@Composable
fun TransformableSample() {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }


    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange

    }

    Box(
        Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y

            )
            .transformable(state = state)
            .background(Color.Blue)
            .fillMaxSize()
    ) {


    }


}




