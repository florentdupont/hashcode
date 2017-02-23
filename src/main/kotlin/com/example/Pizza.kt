package com.example


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
