package com.example

import java.io.File
import java.util.*
import kotlin.comparisons.compareBy


fun main(args: Array<String>) {

    val homePath = System.getProperty("user.dir")
    val resourcesPath = "/src/main/resources"
    val path = homePath + resourcesPath

    run("$path/kittens.in", "$path/kittens.out")
    run("$path/me_at_the_zoo.in", "$path/me_at_the_zoo.out")
    run("$path/trending_today.in", "$path/trending_today.out")
    run("$path/videos_worth_spreading.in", "$path/videos_worth_spreading.out")
}

fun run(path:String, out:String) {

    var readIndex = 0
    val lines = File(path).readLines()

    val first = lines.get(readIndex)
    readIndex++

    val (videoCountStr, endpointCountStr, requestCountStr, cacheCountStr, cacheSizeStr) = first.split(" ")

    val videoCount = videoCountStr.toInt()
    val endpointCount = endpointCountStr.toInt()
    val requestCount = requestCountStr.toInt()
    val cacheCount = cacheCountStr.toInt()
    val cacheSize = cacheSizeStr.toInt()

    println("videoCount : $videoCount")
    println("endpointCount : $endpointCount")
    println("requestCount : $requestCount")
    println("cacheCount : $cacheCount")


    val videos = lines.get(readIndex).split(" ").mapIndexed { i, s ->  Video(i, s.toInt())}
    readIndex++


    println("before reading endpoints")
    val endpoints = HashMap<Int, Endpoint>(endpointCount)
    for(endpointIndex in 0 until endpointCount) {
        val (latency, connectionCount) = lines.get(readIndex).split(" ")
        readIndex++
//        rest = rest.drop(1)
        var newEndpoint = Endpoint(endpointIndex, latency.toInt())

        for(connectionIndex in 0 until connectionCount.toInt()) {
            val (cacheIndex, cacheLatency) = lines.get(readIndex).split(" ")
            readIndex++
            newEndpoint.cacheLatencies.put(cacheIndex.toInt(), cacheLatency.toInt())

        }
        endpoints[endpointIndex] = newEndpoint
    }

    println("after reading endpoints")
    val requests = ArrayList<Request>(requestCount)

    for(i in 0 until requestCount) {
        val (videoId, endpointId, viewCount) = lines.get( readIndex).split(" ")
        readIndex++
//        rest = rest.drop(1)
        val request = Request(videoId.toInt() , viewCount.toInt(), endpointId.toInt(),videos[videoId.toInt()].size)
        requests.add(request)
    }


    println(videos.size)
    println(endpoints.size)
    println(requests.size)

    var caches = ArrayList<Cache>(cacheCount)

    for(i in 0 until cacheCount) {
        var cache = Cache(i, cacheSize)
        caches.add(cache)
    }

    requests.sort()
    print(requests)
    // ALGO PRINCIPAL
    requests.forEach { r ->

        val endpoint:Endpoint = endpoints[r.endpointId] as Endpoint
        val videoSize = videos[r.videoId].size

        var minCacheId:Int = -1
        var minLatency = Int.MAX_VALUE
        endpoint.cacheLatencies.forEach { id, latency ->
            if(videoSize <= caches[id].freeSize) {
                if( latency < minLatency ) {
                    minLatency = latency
                    minCacheId = id
                }
            }
        }


        if(minCacheId != -1) {
            if( !caches[minCacheId].videoIds.contains(r.videoId)) {
                caches[minCacheId].videoIds.add(r.videoId)
                caches[minCacheId].freeSize -= videoSize
            }
        }

    }



    // SORTIE
    File(out).printWriter().use { out ->
        out.println(caches.size)

        caches.forEach {
            cache -> out.print("${cache.id} ")
            out.println(cache.videoIds.joinToString(" "))
        }
    }

}




