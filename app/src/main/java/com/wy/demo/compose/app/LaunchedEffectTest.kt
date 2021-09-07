package com.wy.demo.compose.app

import android.util.Log
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Created by Arthur on 2021/8/22.
 */

@Composable
fun LaunchedEffectApp() {

//    MyScreen()

//    MoviesScreen()

//    LandingScreen {
//
//        Log.e("wy", "time out")
//
//    }
//
//    BackHandler(backDispatcher = ) {
//
//    }
}

@Composable
fun MyScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
//    当 LaunchedEffect 进入组合时，它会启动一个协程，并将代码块作为参数传递
//    。如果 LaunchedEffect 退出组合，协程将取消。如果使用不同的键重组
//    LaunchedEffect（请参阅下方的重启效应部分），系统将取消现有协程，并在新的协程中启动新的挂起函数。

//    key1 如果变化，luanchedeffect 将会重启
    LaunchedEffect(key1 =scaffoldState.snackbarHostState ) {

        scaffoldState.snackbarHostState.showSnackbar(message = "test",actionLabel = "retry")
    }

    Scaffold(scaffoldState = scaffoldState) {
        /* ... */
    }
    
}

//由于 LaunchedEffect 是可组合函数，因此只能在其他可组合函数中使用
//。为了在可组合项外启动协程，但存在作用域限制，以便协程在退出组合后自动取消，
//请使用 rememberCoroutineScope。 此外，如果您需要手动控制一个或多个协程的生命周期，
//请使用 rememberCoroutineScope，例如在用户事件发生时取消动画。

//rememberCoroutineScope 是一个可组合函数，会返回一个 CoroutineScope，
//该 CoroutineScope 绑定到调用它的组合点。调用退出组合后，作用域将取消。


@Composable
fun MoviesScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {

    val scope = rememberCoroutineScope()


    Scaffold(scaffoldState = scaffoldState) {

        Column() {
            Button(onClick = {

                scope.launch {

                    scaffoldState.snackbarHostState
                        .showSnackbar(message = "test")

                }

            }) {

                Text(text = "press me")
            }

        }
    }
}
//rememberUpdatedState：在效应中引用某个值，该效应在值改变时不应重启
//当其中一个键参数发生变化时，LaunchedEffect 会重启。不过，
//在某些情况下，您可能希望在效应中捕获某个值，但如果该值发生变化
//，您不希望效应重启。为此，需要使用 rememberUpdatedState
//来创建对可捕获和更新的该值的引用。这种方法对于包含长期操作的效应十分有用
//，因为重新创建和重启这些操作可能代价高昂或令人望而却步。

//为创建与调用点的生命周期相匹配的效应，永不发生变化的常量（如 Unit 或 true）
//将作为参数传递。在以上代码中，使用 LaunchedEffect(true)
//。为了确保 onTimeout lambda 始终包含重组 LandingScreen
//时使用的最新值，onTimeout 需使用 rememberUpdatedState 函数封装。
//效应中应使用代码中返回的 State、currentOnTimeout。

//警告：LaunchedEffect(true) 与 while(true) 一样可疑。
//尽管 LaunchedEffect(true) 有一些有效用例，但请始终暂停使用这些用例，并确保那是您需要的内容。

@Composable
fun LandingScreen(onTimeout: () -> Unit)  {

    val currentOntimeout by rememberUpdatedState(newValue = onTimeout)

    LaunchedEffect(key1 = true) {

        delay(2000)

        currentOntimeout()
    }
}

//DisposableEffect：需要清理的效应
//对于需要在键发生变化或可组合项退出组合后进行清理的附带效应，
//请使用 DisposableEffect。如果 DisposableEffect 键发生变化，
//可组合项需要处理（执行清理操作）其当前效应，并通过再次调用效应进行重置。
//在上面的代码中，效应将记住的 backCallback 添加到 backDispatcher。如果 backDispatcher 发生变化，系统会处理并再次重启效应。
//
//DisposableEffect 必须在其代码块中添加 onDispose 子句作为最终语句。否则，IDE 将显示构建时错误。


//@Composable
//fun BackHandler(backDispatcher: OnBackPressedDispatcher, onBack: () -> Unit) {
//
//    val currentOnBack by rememberUpdatedState(newValue = onBack)
//
//
//    val backCallback = remember {
//        object : OnBackPressedDispatcher(true) {
//            override fun handleOnBackPressed() {
//                currentOnBack()
//            }
//
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher) {
//
//        backDispatcher.addCallback(backCallback)
//
//
//        onDispose {
//注意：在 onDispose 中放置空块并不是最佳做法。始终再想想是否存在某种更适合您使用场景的效应
//            backCallback.remove()
//        }
//    }
//
//
//}
//SideEffect：将 Compose 状态发布为非 Compose 代码
//如需与非 Compose 管理的对象共享 Compose 状态，请使用 SideEffect 可组合项
// ，因为每次成功重组时都会调用该可组合项。
//
//以之前的 BackHandler 代码为例，如需传达是否应启用回调，请使用 SideEffect 更新其值。
//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher,
//    enabled: Boolean = true, // Whether back events should be intercepted or not
//    onBack: () -> Unit
//) {
//    /* ... */
//    val backCallback = remember { /* ... */ }
//
//    // On every successful composition, update the callback with the `enabled` value
//    // to tell `backCallback` whether back events should be intercepted or not
//    SideEffect {
//        backCallback.isEnabled = enabled
//    }
//
//    /* Rest of the code */
//}

//produceState：将非 Compose 状态转换为 Compose 状态， 感觉类似鱼callbackflow
//produceState 会启动一个协程，该协程将作用域限定为可将值推送到返回的 State 的组合。使用此协程将非 Compose 状态转换为 Compose 状态，例如将外部订阅驱动的状态（如 Flow、LiveData 或 RxJava）引入组合。
//
//该制作工具在 produceState 进入组合时启动，在其退出组合时取消。返回的 State 冲突；设置相同的值不会触发重组。
//
//即使 produceState 创建了一个协程，它也可用于观察非挂起的数据源。如需移除对该数据源的订阅，请使用 awaitDispose 函数。
//
//以下示例展示了如何使用 produceState 从网络加载图像。loadNetworkImage 可组合函数会返回可以在其他可组合项中使用的 State。

//即使 produceState 创建了一个协程，它也可用于观察非挂起的数据源。如需移除对该数据源的订阅，请使用 awaitDispose 函数。
@Composable
fun loadNetworkImage(url: String): State<Result<String>> {

    //类似callback flow
    return produceState(initialValue = Result.failure(Exception())) {

        val s = ""

//        value = if (s == null) {
////            Result.failure(Exception()
//
//        }
//        注意：您应采用常规 Kotlin 函数命名方式命名含返回值类型的可组合项，以小写字母开头。
        Result.success(s)

//        即使 produceState 创建了一个协程，它也可用于观察非挂起的数据源。如需移除对该数据源的订阅，请使用 awaitDispose 函数。

        awaitDispose {

            //移除监听或者订阅， 类似 callackflow awaitclose
        }

//        要点：produceState 在后台充分利用其他效应！它使用 remember { mutableStateOf(initialValue) } 保留 result 变量
    //        ，并在 LaunchedEffect 中触发 producer 块。每当 producer 块中的 value 更新时，result 状态都会更新为新值。
//
//        您可以基于现有 API 轻松创建自己的效应。
    }

}

//derivedStateOf：将一个或多个状态对象转换为其他状态
//如果某个状态是从其他状态对象计算或派生得出的
//，请使用 derivedStateOf。使用此函数可确保仅当计算中使用的状态之一发生变化时才会进行计算。
//以下示例展示了基本的“待办事项”列表，其中具有用户定义的高优先级关键字的任务将首先显示：

@Composable
fun TodoList(
    highPriorityKeywords: List<String> = listOf("Review", "Unblock", "Compose")
) {
    val todoTasks = remember { mutableStateListOf<String>() }
//    在以上代码中，derivedStateOf 保证每当 todoTasks 或 highPriorityKeywords 发生变化时，
//    系统都会执行 highPriorityTasks 计算，并相应地更新界面。
//    由于执行过滤以计算 highPriorityTasks 的成本很高，因此应仅在任何列表更改时执行，而不是在每次重组时执行。
//
//    此外，更新 derivedStateOf 生成的状态不会导致可组合项在声明它的位置重组，
//    Compose 仅对其返回状态为已读的可组合项（在本例中，指 LazyColumn 中的可组合项）进行重组。



    //如果有状态后计算的值， 可以用
//    val highPriorityTasks by remember(todoTasks, highPriorityKeywords) {
//        derivedStateOf {
//            todoTasks.filter {
//                it.contains(highPriorityKeywords)
//            }
//        }
//    }
//
//    Box(Modifier.fillMaxSize()) {
//        LazyColumn {
//            items(highPriorityTasks) { /* ... */ }
//            items(todoTasks) { /* ... */ }
//        }
//        /* Rest of the UI where users can add elements to the list */
//    }
}

//snapshotFlow：将 Compose 的 State 转换为 Flow
//使用 snapshotFlow 将 State<T> 对象转换为冷 Flow。
// snapshotFlow 会在收集到块时运行该块，并发出从块中读取的 State 对象的结果。
// 当在 snapshotFlow 块中读取的 State 对象之一发生变化时，如果新值与之前发出的值不相等，
// Flow 会向其收集器发出新值（此行为类似于 Flow.distinctUntilChanged 的行为）。
//
//下列示例显示了一项附带效应，是系统在用户滚动经过要分析的列表的首个项目时记录下来的：
//
@Composable
fun snapshotFlowTest() {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        // ...
    }

    //可以把state转换成flow
//    LaunchedEffect(listState) {
//        snapshotFlow { listState.firstVisibleItemIndex }
//            .map { index -> index > 0 }
//            .distinctUntilChanged()
//            .filter { it == true }
//            .collect {
////                MyAnalyticsService.sendScrolledPastFirstItemEvent()
//            }
//    }


}

//重启效应
//Compose 中有一些效应（如 LaunchedEffect、produceState
// 或 DisposableEffect）会采用可变数量的参数和键来取消运行效应，并使用新的键启动一个新的效应。
//由于此行为的细微差别，如果用于重启效应的参数不是适当的参数，可能会出现问题：
//
//如果重启效应次数不够，可能会导致应用出现错误。
//如果重启效应次数过多，效率可能不高。
//一般来说，效应代码块中使用的可变和不可变变量应作为参数添加到效应可组合项中。
//除此之外，您还可以添加更多参数，以便强制重启效应。
//如果更改变量不应导致效应重启，
//则应将该变量封装在 rememberUpdatedState 中
//。如果由于变量封装在一个不含键的 remember 中使之没有发生变化，则无需将变量作为键传递给效应。

//在上面显示的 DisposableEffect 代码中，效应将其块中使用的 backDispatcher 作为参数，因为对它们的任何更改都会导致效应重启。
//
//使用常量作为键
//您可以使用 true 等常量作为效应键
// ，使其遵循调用点的生命周期。它实际上具有有效的用例，
// 如上面所示的 LaunchedEffect 示例。但在这样做之前，请审慎考虑，并确保您确实需要这么做。

