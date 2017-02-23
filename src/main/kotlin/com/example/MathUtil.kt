package com.example

import java.lang.Math.pow
import java.lang.Math.sqrt

class MathUtil {

    companion object {


        /**
         * Distance entre deux cellules
         */
        fun dist(c1:Cell, c2:Cell):Int {
            return sqrt(pow((c1.row - c2.row).toDouble(),2.0) + pow((c1.col - c2.col).toDouble(),2.0)).toInt();
        }

    }

}
