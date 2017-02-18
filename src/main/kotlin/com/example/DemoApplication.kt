package com.example

import java.io.File
import java.util.*


fun main(args: Array<String>) {

    run("D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\big.in", "D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\big.out")
    run("D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\medium.in", "D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\medium.out")
    run("D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\small.in", "D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\small.out")
    run("D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\example.in", "D:\\dev\\repo_git\\hashcode\\src\\main\\resources\\example.out")
}


fun run(path:String, out:String) {

    val lines = File(path).readLines()

    val first = lines.first()
    val rest = lines.drop(1);

    val (rowCount, columnCount, eltsPerSlice, maxCellsPerSlice) = first.split(" ")

    println("rows : $rowCount")
    println("columns : $columnCount")
    println("elements per slice : $eltsPerSlice")
    println("max cells per slice : $maxCellsPerSlice")

    val pizza = Pizza(rowCount.toInt(), columnCount.toInt())
    pizza.load(rest)

    println(pizza)

    val ingredient = pizza[1,1]
    println(ingredient)

    val slices = arrayListOf<Slice>();
    var cells = ArrayList<Cell>()
    var slice = Slice(cells)
    for(r in 0 until pizza.rowCount){
        cells = ArrayList<Cell>()
        slice = Slice(cells)
        for(c in 0 until pizza.columnCount) {

            val current = pizza[r, c] ?: continue;

            // Si la part n'a pas assez de cellules et que la cellule n'est pas deja dans une part on l'ajoute a la part
            if(slice.cells.size < maxCellsPerSlice.toInt() && !current.isInSlice) {
                println("part possible")

                cells.add(current)
                //si la part est la derniere de la rangee et qu'elle est valide on l'ajoute
                if (c == pizza.columnCount -1 && slice.numberOfCellsOfElement("T") >= eltsPerSlice.toInt()
                        && slice.numberOfCellsOfElement("M") >= eltsPerSlice.toInt()) {
                    slices.add(slice)
                    slice.setCellUnavailable(pizza)
                    println(slice)
                    cells = ArrayList<Cell>()
                    slice = Slice(cells)

                }
                // Sinon si la part a le nombre limite de cellule on ajoute la part et on reinitialise
            } else if (slice.cells.size == maxCellsPerSlice.toInt() && slice.numberOfCellsOfElement("T") >= eltsPerSlice.toInt()
                    && slice.numberOfCellsOfElement("M") >= eltsPerSlice.toInt()) {
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                // Sinon si on a une part a une cellule et que l'on est a la fin de la ligne on l'ajoute
                // TODO : verifier qu'on a bien assez d'ingredients de chaque type
            } else if (cells.size >= 1 && r == pizza.columnCount - 1  && slice.cells.size < maxCellsPerSlice.toInt() && !current.isInSlice) {
                cells.add(current)
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                //Sinon on cree une nouvelle part
            } else {
                cells = ArrayList<Cell>()
                slice = Slice(cells)
            }

        }
    }
    cells = ArrayList<Cell>()
    slice = Slice(cells)
    for(c in 0 until pizza.columnCount){
        cells = ArrayList<Cell>()
        slice = Slice(cells)
        for(r in 0 until pizza.rowCount) {

            //val last = pizza[r-1, c] ?: continue;

            val current = pizza[r, c] ?: continue;
            // Si la part n'a pas assez de cellules et que la cellule n'est pas deja dans une part on l'ajoute a la part
            if(slice.cells.size < maxCellsPerSlice.toInt() &&  !current.isInSlice) {
                println("part possible")

                cells.add(current)

                //si la part est la derniere de la colonne et qu'elle est valide on l'ajoute
                if (c == pizza.rowCount -1 && slice.numberOfCellsOfElement("T") >= eltsPerSlice.toInt()
                        && slice.numberOfCellsOfElement("M") >= eltsPerSlice.toInt()) {
                    slices.add(slice)
                    slice.setCellUnavailable(pizza)
                    println(slice)
                    cells = ArrayList<Cell>()
                    slice = Slice(cells)

                }
                // Sinon si la part a le nombre limite de cellule on ajoute la part et on reinitialise
            } else if (slice.cells.size == maxCellsPerSlice.toInt() && slice.numberOfCellsOfElement("T") >= eltsPerSlice.toInt()
                    && slice.numberOfCellsOfElement("M") >= eltsPerSlice.toInt()) {
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
                // Sinon si on a une part a une cellule et que l'on est a la fin de la colonne on l'ajoute
                // TODO : verifier qu'on a bien assez d'ingredients de chaque type
            } else if (cells.size >= 1 && r == pizza.rowCount - 1 && slice.cells.size < maxCellsPerSlice.toInt() && !current.isInSlice) {
                cells.add(current)
                slices.add(slice)
                slice.setCellUnavailable(pizza)
                println(slice)
                cells = ArrayList<Cell>()
                slice = Slice(cells)
                cells.add(current)
            } else {
                cells = ArrayList<Cell>()
                slice = Slice(cells)
            }

        }
    }

    File(out).printWriter().use { out ->
        out.println(slices.size)

        slices.forEach {
            slice -> out.println("${slice.cells.first().row} ${slice.cells.first().col} ${slice.cells.last().row} ${slice.cells.last().col}")
        }
    }

}

fun isInSlices(slices: ArrayList<Slice>, cell:Cell):Boolean {
    println("isInSlice ? $cell" )
    val result = slices.filter { slice -> slice.isIn(cell)}.size > 0
    println("result : $result")
    return result
}

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

data class Cell(val row:Int, val col:Int, val type:String) {
    var isInSlice: Boolean
    init {
        isInSlice = false
    }
    fun setInSlice() {
        isInSlice = true
    }
}


data class Pizza(val rowCount:Int, val columnCount:Int) {

    val ingredients: Board<Cell?>

    init {
        println("initializing Pizza")
        ingredients = Board(rowCount, columnCount)
    }

    fun load(rows: List<String>) {
        if (rows.size != rowCount) {
            throw Exception("Erreur : le nombre de ligne ne correspond pas à ce qui est attendu")
        }

        rows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, value ->
                ingredients[rowIndex, colIndex] = Cell(rowIndex, colIndex,"$value")
            }
        }
    }

    operator fun get(row: Int, col: Int): Cell?  {
        try {
            return ingredients[row][col]
        } catch(e:ArrayIndexOutOfBoundsException) {
            return null
        }
    }

    override fun toString(): String {
        val str = StringBuilder()
        str.append("Pizza [rows=$rowCount, cols=$columnCount]\n\n")
        for(i in 0 until rowCount) {
            str.append(ingredients[i].joinToString("")).append("\n")
        }
        return "$str"
    }
}
