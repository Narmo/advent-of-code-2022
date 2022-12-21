import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
	val cave = mutableListOf<MutableList<Any?>>()

	var minX = 0
	var maxX: Int
	var minY: Int
	var maxY = Int.MAX_VALUE

	data class Point(val x: Int, val y: Int)
	data class Sand(var x: Int = 500 - minX, var y: Int = 0)

	fun Sand.move(): Boolean {
		return if (cave[y + 1][x] == null) {
			// println("Moving down to $x, ${y + 1}")
			y++
			true
		}
		else if (cave[y + 1][x - 1] == null) {
			// println("Moving left to ${x - 1}, ${y + 1}")
			x--
			y++
			true
		}
		else if (cave[y + 1][x + 1] == null) {
			// println("Moving right to ${x + 1}, ${y + 1}")
			x++
			y++
			true
		}
		else {
			// println("Not moving")
			false
		}
	}

	fun printCave() {
		cave.forEach { row ->
			row.forEach { point ->
				when (point) {
					null -> print(".")
					is Point -> print("#")
					is Sand -> print("o")
					else -> print(" ")
				}
			}

			println()
		}

		println()
	}

	fun buildCave(input: List<String>, infiniteFloor: Boolean = false) {
		cave.clear()

		val points = mutableSetOf<Point>()

		input.map { line ->
			val coordinates = line.split(" -> ")
			val (px, py) = coordinates[0].split(",").map { it.toInt() }
			var prevPoint = Point(px, py)

			points.add(prevPoint)

			for (i in 1 until coordinates.size) {
				val (x, y) = coordinates[i].split(",").map { it.toInt() }
				val point = Point(x, y)

				points.add(point)

				if (prevPoint.x == point.x) {
					for (j in min(prevPoint.y, point.y)..max(prevPoint.y, point.y)) {
						points.add(Point(x, j))
					}
				}
				else {
					for (j in min(prevPoint.x, point.x)..max(prevPoint.x, point.x)) {
						points.add(Point(j, y))
					}
				}

				prevPoint = point
			}
		}

		minX = points.minOf { it.x } - 1
		maxX = points.maxOf { it.x } + 1
		minY = 0
		maxY = points.maxOf { it.y }

		if (infiniteFloor) {
			minX = max(0, minX - maxY)
			maxX += maxY
		}

		val columns = maxX - minX + 1
		val rows = maxY - minY + 1

		for (y in 0..rows) {
			cave.add(mutableListOf())

			for (x in 0..columns) {
				cave[y].add(points.find { it.x == x + minX && it.y == y + minY })
			}
		}

		if (infiniteFloor) {
			val row = mutableListOf<Any?>()
			cave.add(row)

			for (i in minX..maxX) {
				row.add(Point(i, maxY))
			}
		}

		printCave()
	}

	fun part1(input: List<String>): Int {
		buildCave(input)

		var sandsCounter = 0
		var currentSand = Sand()

		printCave()

		while (currentSand.y < maxY) {
			val prevX = currentSand.x
			val prevY = currentSand.y

			if (currentSand.move()) {
				cave[prevY][prevX] = null
				cave[currentSand.y][currentSand.x] = currentSand
			}
			else {
				if (currentSand.x == 500 - minX && currentSand.y == 0) {
					break
				}

				currentSand = Sand()

				sandsCounter++
			}

			// printCave()
		}

		println("Total sands: $sandsCounter")

		return sandsCounter
	}

	fun part2(input: List<String>): Int {
		buildCave(input, true)

		var sandsCounter = 1
		var currentSand = Sand()

		printCave()

		while (currentSand.y < maxY + 2) {
			val prevX = currentSand.x
			val prevY = currentSand.y

			if (currentSand.move()) {
				cave[prevY][prevX] = null
				cave[currentSand.y][currentSand.x] = currentSand
			}
			else {
				if (currentSand.x == 500 - minX && currentSand.y == 0) {
					break
				}

				currentSand = Sand()

				sandsCounter++
			}

			// printCave()
		}

		println("Total sands: $sandsCounter")

		return sandsCounter
	}

	val testInput = readInput("Day14_test")
	check(part1(testInput) == 24)
	check(part2(testInput) == 93)

	val input = readInput("Day14")
	println(part1(input))

	benchmark {
		println(part2(input))
	}
}
