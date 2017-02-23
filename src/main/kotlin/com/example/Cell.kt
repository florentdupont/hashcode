package com.example;


data class Cell(val row:Int, val col:Int, val type:String = "") {

    var isInSlice: Boolean = false
//    init {
//        isInSlice = false
//    }

    fun setInSlice() {
        isInSlice = true
    }
}