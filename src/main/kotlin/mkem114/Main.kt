package mkem114

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File

fun main() {
    val file =  File("src/main/resources/matrix.csv")
//    val file = File("src/main/resources/small-matrix.csv")

    val weights: List<List<Int>> = csvReader()
        .readAll(file.readText())
        .map { stringList -> stringList.map { it.toInt() } }

    val height = weights.size
    val endRow = height - 1
    val width = weights[0].size
    val endColumn = width - 1

    val cumulativeWeights = MutableList(height) { MutableList(width) { Int.MAX_VALUE / 2 } }

    for (row in 0 until height) {
        cumulativeWeights[row][0] = weights[row][0]
    }

    for (column in 0 until width) {
        for (row in 0 until height) {
            if (row != 0) {
                val candidateWeight = cumulativeWeights[row - 1][column] + weights[row][column]
                if (candidateWeight < cumulativeWeights[row][column]) {
                    cumulativeWeights[row][column] = candidateWeight
                }
            }

            if (column != 0) {
                val candidateWeight = cumulativeWeights[row][column - 1] + weights[row][column]
                if (candidateWeight < cumulativeWeights[row][column]) {
                    cumulativeWeights[row][column] = candidateWeight
                }
            }
        }

        for (row in (0 until height).reversed()) {
            if (row != endRow) {
                val candidateWeight = cumulativeWeights[row + 1][column] + weights[row][column]
                if (candidateWeight < cumulativeWeights[row][column]) {
                    cumulativeWeights[row][column] = candidateWeight
                }
            }
        }
    }

    var weight = Int.MAX_VALUE
    for (row in 0 until height) {
        val current = cumulativeWeights[row][endColumn]
        if (current < weight) {
            weight = current
        }
    }

    println(weight)
}
