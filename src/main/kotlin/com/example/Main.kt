package com.example

import java.io.File
import java.util.*


fun main(args: Array<String>) {

    val homePath = System.getProperty("user.dir")
    val resourcesPath = "/src/main/resources"
    val path = homePath + resourcesPath

    run("$path/big.in", "$path/big.out")
    run("$path/medium.in", "$path/medium.out")
    run("$path/small.in", "$path/small.out")
    run("$path/example.in", "$path/example.out")
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




