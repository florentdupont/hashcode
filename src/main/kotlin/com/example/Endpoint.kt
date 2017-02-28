package com.example

import java.util.*

data class Endpoint(var i:Int, val datacenterLatency:Int, var cacheLatencies : HashMap<Int, Int> = HashMap<Int, Int>()) {

    var requests = ArrayList<Request>()


    fun addRequest(request: Request) {
        requests.add(request)
    }

    fun removeRequest(request: Request) {
        requests.remove(request)
    }

    fun removeCache(idCache: Int) {
        cacheLatencies.remove(idCache)
    }

    fun getRequestWithMostLatencyGainWithCacheId() : Pair<Request, Int> {
        var latencyGain = 0;
        var cacheWithLessLatency = getCacheWithLessLatency()
        var lessLatency = cacheWithLessLatency.first
        var requestWithMostLatencyGain : Request = Request(-1,-1,-1,-1)
        requests.forEach { request ->
            var latencyGainWithCache = (request.viewCount * datacenterLatency) - (lessLatency * request.viewCount)
            if (latencyGainWithCache > latencyGain) {
                latencyGain = latencyGainWithCache
                requestWithMostLatencyGain = request
            }
        }
        return Pair(first=requestWithMostLatencyGain, second=cacheWithLessLatency.second)


    }

    fun getCacheWithLessLatency() :  Pair<Int, Int> {
        var minLatency = Int.MAX_VALUE
        var minLatencyCacheId = -1
        cacheLatencies.forEach { cacheId, latency ->
            if (latency < minLatency) {
                minLatency = latency
                minLatencyCacheId = cacheId
            }
        }
        var res : Pair<Int, Int> = Pair(first=minLatency, second=minLatencyCacheId)

        return res
    }

}