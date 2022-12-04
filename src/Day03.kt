fun main() {
	fun Char.getPriority(): Int = when {
		isUpperCase() -> this - 'A' + 27
		else -> this - 'a' + 1
	}

	fun part1(input: List<String>): Int = input.sumOf { line ->
		val compartmentSize = line.length / 2
		val firstCompartment = line.substring(0, compartmentSize).toSet()
		val secondCompartment = line.substring(compartmentSize).toSet()
		secondCompartment.intersect(firstCompartment).sumOf { it.getPriority() }
	}

	fun part2(input: List<String>): Int = input.windowed(size = 3, step = 3, partialWindows = false).sumOf { (first, second, third) ->
		first.toSet().intersect(second.toSet()).intersect(third.toSet()).sumOf { it.getPriority() }
	}

	val testInput = readInput("Day03_test")
	check(part1(testInput) == 157)
	check(part2(testInput) == 70)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}
