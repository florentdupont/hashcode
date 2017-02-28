package com.example



data class Request(var videoId:Int, var viewCount:Int, var endpointId:Int,var videoSize:Int) : Comparable<Request> {

    override fun compareTo(other: Request) : Int {
        if (this.viewCount > other.viewCount) {
            if (this.videoSize > other.videoSize) {
                return -10;

            } else if (this.videoSize == other.videoSize) {
                return -50
            }
            return -100
        } else if (this.viewCount < other.viewCount) {
            if (this.videoSize < other.videoSize) {
                return 10;

            } else if (this.videoSize == other.videoSize) {
                return 50
            }
            return 100
        } else {
            if (this.videoSize > other.videoSize) {
                return 1;

            } else if (this.videoSize == other.videoSize) {
                return 0
            }
            return -1;

        }

    }



}
