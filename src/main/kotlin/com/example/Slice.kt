package com.example

import java.util.*


data class Slice(val cells : ArrayList<Cell>) {

    fun isIn(cell:Cell):Boolean {
        println("cell  :$cell")
        println("cells :$cells")
        println("result :" + (cells.indexOf(cell) != -1))
        return (cells.indexOf(cell) != -1)
    }

    fun numberOfCellsOfElement(type:String):Int {
        var res = 0
        cells.forEach {
            cell -> if (cell.type == type) {
            res++
        }
        }
        return res
    }

    fun setCellUnavailable(pizza:Pizza) {
        cells.forEach {
            cell -> pizza.ingredients[cell.row][cell.col]!!.setInSlice()
        }
    }
}