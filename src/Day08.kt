fun main() {
	fun toGrid(input: List<String>): List<IntArray> = input.map { line -> line.map { it.digitToInt() }.toIntArray() }
	fun List<IntArray>.getColumn(index: Int): IntArray = this.map { it[index] }.toIntArray()
	fun IntArray.toPrintableString(): String = joinToString(",")

	fun visibleIn(line: IntArray, index: Int, fromLeft: Boolean): Pair<Boolean, Int> {
		if (fromLeft && index == 0) {
			return true to 0
		}

		if (!fromLeft && index == line.size - 1) {
			return true to 0
		}

		val range = if (fromLeft) (index - 1 downTo 0) else (index + 1 until line.size)
		var score = 0

		for (i in range) {
			score += 1

			if (line[i] >= line[index]) {
				return false to score
			}
		}

		return true to score
	}

	fun part1(input: List<String>): Int {
		val grid = toGrid(input)
		var counter = 0

		grid.forEachIndexed row@{ rowIndex, row ->
			row.forEachIndexed column@{ columnIndex, tree ->
				val column = grid.getColumn(columnIndex)

				println("Row: ${row.toPrintableString()}")
				println("Column: ${column.toPrintableString()}")

				if (visibleIn(row, columnIndex, true).first) {
					println("Visible from left: $tree")
					counter += 1
					return@column
				}

				if (visibleIn(row, columnIndex, false).first) {
					println("Visible from right: $tree")
					counter += 1
					return@column
				}

				if (visibleIn(column, rowIndex, true).first) {
					println("Visible from top: $tree")
					counter += 1
					return@column
				}

				if (visibleIn(column, rowIndex, false).first) {
					println("Visible from bottom: $tree")
					counter += 1
					return@column
				}
			}
		}

		println("Counter: $counter")

		return counter
	}

	fun part2(input: List<String>): Int {
		val grid = toGrid(input)

		return grid.mapIndexed row@{ rowIndex, row ->
			row.mapIndexed column@{ columnIndex, _ ->
				val column = grid.getColumn(columnIndex)
				val leftScore = visibleIn(row, columnIndex, true).second
				val rightScore = visibleIn(row, columnIndex, false).second
				val topScore = visibleIn(column, rowIndex, true).second
				val bottomScore = visibleIn(column, rowIndex, false).second

				leftScore * rightScore * topScore * bottomScore
			}.max()
		}.max()
	}

	val testInput = readInput("Day08_test")
	check(part1(testInput) == 21)
	check(part2(testInput) == 8)

	val input = readInput("Day08")
	println(part1(input))
	println(part2(input))
}
