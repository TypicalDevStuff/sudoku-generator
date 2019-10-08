package com.typicaldev.sudoku

import java.util.stream.IntStream

internal object Solver {

    fun solvable(grid: Array<IntArray>) : Boolean {
        val gridToOperate = grid.copy()

        return solve(gridToOperate)
    }

    private fun Array<IntArray>.copy() = Array(size) { get(it).clone() }

    private fun solve(grid: Array<IntArray>) : Boolean {
        for (i in 0 until GRID_SIZE) {
            for (j in 0 until GRID_SIZE) {
                if (grid[i][j] == 0) {
                    for (k in MIN_DIGIT_VALUE..MAX_DIGIT_VALUE) {
                        grid[i][j] = k
                        if (isValid(grid, i, j) && solve(grid)) {
                            return true
                        }
                        grid[i][j] = 0
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValid(grid: Array<IntArray>, row: Int, column: Int) =
            isRowValid(grid, row) && isColumnValid(grid, column) && isBoxValid(grid, row, column)

    private fun isRowValid(grid: Array<IntArray>, row: Int) : Boolean {
        val constraint = BooleanArray(GRID_SIZE)

        return IntStream.range(MIN_DIGIT_INDEX, MAX_DIGIT_INDEX)
                .allMatch { column -> checkConstraint(grid, row, column, constraint) }
    }

    private fun isColumnValid(grid: Array<IntArray>, column: Int) : Boolean {
        val constraint = BooleanArray(GRID_SIZE)

        return IntStream.range(MIN_DIGIT_INDEX, MAX_DIGIT_INDEX)
                .allMatch { row -> checkConstraint(grid, row, column, constraint) }
    }

    private fun isBoxValid(grid: Array<IntArray>, row: Int, column: Int) : Boolean {
        val constraint = BooleanArray(GRID_SIZE)
        val rowStart = findBoxStart(row)
        val rowEnd = findBoxEnd(rowStart)
        val columnStart = findBoxStart(column)
        val columnEnd = findBoxEnd(columnStart)

        for (i in rowStart until rowEnd) {
            for (j in columnStart until columnEnd) {
                if (!checkConstraint(grid, i, j, constraint)) {
                    return false
                }
            }
        }
        return true
    }

    private fun checkConstraint(grid: Array<IntArray>, row: Int, column: Int,
                                constraint: BooleanArray) : Boolean {
        if (grid[row][column] != 0) {
            if (!constraint[grid[row][column] - 1]) {
                constraint[grid[row][column] - 1] = true
            } else {
                return false
            }
        }
        return true
    }

    private fun findBoxStart(index: Int) = index - index % GRID_SIZE_SQUARE_ROOT

    private fun findBoxEnd(index: Int) = index + BOX_SIZE - 1
}
