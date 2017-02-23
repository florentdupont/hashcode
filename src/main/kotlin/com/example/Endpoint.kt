package com.example

import java.util.*

data class Endpoint(var i:Int, val datacenterLatency:Int, var cacheLatencies : HashMap<Int, Int> = HashMap<Int, Int>()) {

    var requests = ArrayList<Request>()




}