import kotlin.math.abs

fun main() {
	class Knot(var x: Int, var y: Int) {
		fun copy() = Knot(x, y)
		fun isAdjacentTo(other: Knot) = abs(x - other.x) < 2 && abs(y - other.y) < 2
		override fun toString() = "($x, $y)"
		override fun equals(other: Any?) = other is Knot && x == other.x && y == other.y
		override fun hashCode() = x * 31 + y * y * y * y

		fun moveToNear(other: Knot) {
			if (x < other.x) x++
			if (x > other.x) x--
			if (y < other.y) y++
			if (y > other.y) y--
		}
	}

	fun process(input: List<String>, knotsNumber: Int): Int {
		val knots = (0 until knotsNumber).map { Knot(0, 0) }
		val visitedPoints = mutableSetOf(knots.last().copy())

		input.forEach {
			val (direction, steps) = it.split(" ")

			for (i in 0 until steps.toInt()) {
				val headPosition = knots.first()

				when (direction) {
					"U" -> headPosition.y += 1 // inverted
					"D" -> headPosition.y -= 1 // inverted
					"L" -> headPosition.x -= 1
					"R" -> headPosition.x += 1
				}

				for (j in 1 until knots.size) {
					val knot = knots[j]
					val previousKnot = knots[j - 1]

					if (!knot.isAdjacentTo(previousKnot)) {
						knot.moveToNear(previousKnot)
					}
				}

				visitedPoints.add(knots.last().copy())
			}
		}

		return visitedPoints.size
	}

	fun part1(input: List<String>): Int = process(input, knotsNumber = 2)
	fun part2(input: List<String>): Int = process(input, knotsNumber = 10)

	val testInput1 = readInput("Day09_test1")
	check(part1(testInput1) == 13)
	check(part2(testInput1) == 1)

	val testInput2 = readInput("Day09_test2")
	check(part2(testInput2) == 36)

	val input = readInput("Day09")
	println(part1(input))
	println(part2(input))
}
