package com.wy.demo.compose.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Arthur on 2021/8/1.
 */
class MainViewModel: ViewModel() {

    val name = MutableStateFlow("test")

    val list = MutableStateFlow(listOf("1", "2"))

    fun updateName(name: String) {

//        this.name.value = name

        list.value = listOf("1", "2", "3", "4")
    }
}