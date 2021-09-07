package com.wy.demo.compose.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.wy.demo.compose.app.ui.theme.Compose_Demo_1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            TestApp()

//            CompositionLocalExample()

//            GesturesApp()

//            CanvasApp()

//            LaunchedEffectApp()

//            LaunchedEffectApp()

            ExperimentalAnimationNav()
        }
    }
}


@Composable
fun BackHandler(backDispatcher: OnBackPressedDispatcher, onBack: () -> Unit) {

    val currentOnBack by rememberUpdatedState(newValue = onBack)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }


    DisposableEffect(backDispatcher) {

        backDispatcher.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

@Composable
fun TestApp() {

    Compose_Demo_1Theme {

        // A surface container using the 'background' color from the theme
        Column(
            modifier = Modifier
                .fillMaxSize()
//                        .verticalScroll(rememberScrollState()),
//                    color = MaterialTheme.colors.background
        ) {

        }
    }
}

@Composable
fun MyCompositionLocalExample() {
    val elevations = if (!isSystemInDarkTheme()) {
        Elevations(1.dp, 1.dp)
    }else {
        Elevations(0.dp, 0.dp)
    }

    CompositionLocalProvider(LocalElevations provides elevations) {

        SomeComposable()
    }

}

@Composable
fun SomeComposable() {
    // Access the globally defined LocalElevations variable to get the
    // current Elevations in this part of the Composition
    Card(elevation = LocalElevations.current.card, backgroundColor = Color.Red) {

        Text(text = "teadfdsfadfadft")

    }
}

@Composable
fun CompositionLocalExample() {

    MaterialTheme {

        Column {
            Text("Uses MaterialTheme's provided alpha")

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("Medium value provided for LocalContentAlpha")
                Text("This Text also uses the medium value")

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    DescendantExample()

                }


            }

        }
    }

}

@Composable
fun DescendantExample() {
    // CompositionLocalProviders also work across composable functions
    Text("This Text uses the disabled alpha now")
}


//



@ExperimentalMaterialApi
@Composable
fun BottomSheetTest() {


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(text = "Hello from sheet")
            }
        }, sheetPeekHeight = 0.dp
    ) {
        Button(onClick = {
            coroutineScope.launch {

                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                } else {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        }) {
            Text(text = "Expand/Collapse Bottom Sheet")
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NavTest() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "first"
    ) {
        composable(
            route = "first",
           arguments = emptyList(),
            emptyList(),
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "second/{name}" ->
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = { _, target ->
                when (target.destination.route) {
                    "second/{name}" ->
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "second/{name}" ->
                        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = { _, target ->
                when (target.destination.route) {
                    "second/{name}" ->
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            }
        ) {
            FirstPage(navController)
        }

        composable(
            route = "second/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            ),
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "first" ->
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = { _, target ->
                when (target.destination.route) {
                    "first" ->
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "first" ->
                        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = { _, target ->
                when (target.destination.route) {
                    "first" ->
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            }

        ) {

            val name = it.arguments?.getString("name")

//            TestGrid()
            SecondPage(navController,name ?: "null")
        }
    }

}

@Composable
fun FirstPage(navController: NavHostController) {

    Column(Modifier.fillMaxSize().background(Color.Yellow)) {
        OutlinedButton(
            onClick = {
                navController.navigate("second/123")


            },
            border = BorderStroke(1.dp, Color.Gray),
            modifier = Modifier
                .width(200.dp)
                .padding(8.dp)

        ) {

            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(text = "first")

        }
    }


}

@Composable
fun SecondPage(navController: NavHostController, name: String) {

    Column(Modifier.fillMaxSize().background(Color.Red)) {
        Button(onClick = { navController.popBackStack() }) {


            Text(text = "second $name")

        }
    }


}


@Composable
fun Test_1() {

    val viewModel: MainViewModel = viewModel()

    viewModel.name.collectAsState()

    val name: String by viewModel.name.collectAsState()

    val list: List<String> by viewModel.list.collectAsState()
    ButtonTest(name) {
        viewModel.updateName("click")
    }

    (1..30).forEach { _ ->
        TextFieldTest("123")
    }

}

@Composable
fun TextFieldTest(value: String) {

    var text: TextFieldValue by remember {
        mutableStateOf(TextFieldValue(value))
    }

    TextFieldValue
    TextField(value = text, onValueChange = {
        text = it
    })

}

@Composable
fun ButtonTest(name: String, onClick: () -> Unit) {

    Column() {
        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {

            Button(onClick = onClick) {


                Text(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),text = name)

            }


        }

    }



}

@Composable
fun TestList(messages: List<String>) {

    LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {

        items(messages) { msg ->

            Greeting(name = msg)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestGrid() {

    LazyVerticalGrid(
        cells = GridCells.Fixed(10)

    ) {

        items(200) { index ->
            Greeting(name = "index: $index")

        }
    }

}


@Composable
fun BoxTest() {

    Box {

        Greeting(name = "1")
        Greeting(name = "2")
        Greeting(name = "3")
        Greeting(name = "4")

    }
}


@Composable
fun Greeting(name: String) {
    var text: String by remember(calculation = {
        mutableStateOf("123")
    })

    var color: Long by remember {
        mutableStateOf(0xff00ffff)
    }

    var show by remember {
        mutableStateOf(true)
    }
    if (show) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(Color(color))

                .clickable {

                    show = false
                    color = 0xffff00ff
                },
            text = "Hello cc aa rr  $name! $text",
            fontSize = 12.sp
        )
    }


}


private fun Int.textDp(density: Density): TextUnit = with(density) {
    this@textDp.dp.toSp()
}


val Int.textDp: TextUnit
    @Composable get() = this.textDp(density = LocalDensity.current)
