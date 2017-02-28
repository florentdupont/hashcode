package com.example

import java.util.*

data class Cache(var id:Int, var freeSize:Int,var endpoints :ArrayList<Endpoint> = ArrayList<Endpoint>(), var videoIds :ArrayList<Int> = ArrayList<Int>()) {


    override fun toString(): String {
        return "${id} ${freeSize}"
    }
}