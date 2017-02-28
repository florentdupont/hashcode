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
        endpoints.get(endpointId.toInt())!!.addRequest(request)
    }

    var caches = ArrayList<Cache>(cacheCount)

    for(i in 0 until cacheCount) {
        var cache = Cache(i, cacheSize,getEndpointsFromCacheId(i,endpoints))
        caches.add(cache)
    }
    println(videos.size)
    println(endpoints.size)
    println(requests.size)



    requests.sort()
    print(requests)
    // ALGO PRINCIPAL
    /*caches.forEach { cache ->
        val numberOfSameVideoOfRequestLinkedToTheSameCache :HashMap<Int,Int> = HashMap()
        cache.endpoints.forEach {
            endpoint -> endpoint.requests.forEach {
                request -> if (numberOfSameVideoOfRequestLinkedToTheSameCache.keys.contains(request.videoId)) {
            numberOfSameVideoOfRequestLinkedToTheSameCache.put(request.videoId,(numberOfSameVideoOfRequestLinkedToTheSameCache.get(request.videoId)!! +1))
        } else {
            numberOfSameVideoOfRequestLinkedToTheSameCache.put(request.videoId,1)

        }
            }
        }
        numberOfSameVideoOfRequestLinkedToTheSameCache.forEach { key, value ->
            if (value >= 4 && videos[key].size <= cache.freeSize) {
                cache.videoIds.add(key)
                cache.freeSize -= videos[key].size
            }
        }

    }*/
   /* requests.forEach { r ->

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
            if( !caches[minCacheId].videoIds.contains(r.videoId) && !isVideoIsInCacheWithSameEndpoint(r, caches[minCacheId])) {
                caches[minCacheId].videoIds.add(r.videoId)
                caches[minCacheId].freeSize -= videoSize
            }
        }

    }*/


    endpoints.forEach { e ->

        while (!e.value.requests.isEmpty() && !e.value.cacheLatencies.isEmpty()) {
            //On recupere l'id du cache du endoint qui a la plus petite latence
            var minCacheId: Int = e.value.getRequestWithMostLatencyGainWithCacheId().second
            var requestWithMostLatencyGainWithCacheId = e.value.getRequestWithMostLatencyGainWithCacheId()
            //On recupere la requete qui a le plus de gain de latence
            var requestWithMostLatencyGain: Request = requestWithMostLatencyGainWithCacheId.first
            val videoSize = videos[requestWithMostLatencyGain.videoId].size
            if (minCacheId != -1) {
                if (videoSize <= caches[minCacheId].freeSize && !caches[minCacheId].videoIds.contains(requestWithMostLatencyGain.videoId)) {

                    caches[minCacheId].videoIds.add(requestWithMostLatencyGain.videoId)
                    caches[minCacheId].freeSize -= videoSize
                    //on supprime la requete du endpoint
                    e.value.removeRequest(requestWithMostLatencyGain)
                } else {/*if (videoSize > caches[minCacheId].freeSize) {*/
                    //cacheIsFull = true
                    //Si plus de place on supprime le cache du endpoint (peut etre ameliore car il peut encore rester de la place)
                    e.value.removeCache(minCacheId)
                }
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


fun getEndpointsFromCacheId(cacheId:Int,endpoints:HashMap<Int, Endpoint>):ArrayList<Endpoint> {
    var res :ArrayList<Endpoint> = ArrayList<Endpoint>()
    endpoints.forEach {
        x ->
        if (x.value.cacheLatencies.contains(cacheId)) {
            res.add(x.value)

        }
    }

    return res
}
fun isVideoIsInCacheWithSameEndpoint(request: Request,cache:Cache): Boolean {

    cache.endpoints.forEach {
        endpoint -> endpoint.requests.forEach {
            req ->
            if (req.videoId == request.videoId && req.endpointId != request.endpointId) {
                return true
            }
        }
    }
    return false
}




