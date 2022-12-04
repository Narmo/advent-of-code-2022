fun main() {
	fun IntRange.fullyContains(other: IntRange): Boolean = this.first <= other.first && this.last >= other.last

	fun findOverlaps(input: List<String>, shouldFullyContain: Boolean): Int = input.map { pair ->
		pair.split(",").map { range ->
			range.split("-").map {
				it.toInt()
			}.run {
				this[0]..this[1]
			}
		}.let { (first, second) ->
			if (shouldFullyContain) {
				first.fullyContains(second) || second.fullyContains(first)
			}
			else {
				first.intersect(second).isNotEmpty()
			}
		}
	}.count { it }

	fun part1(input: List<String>): Int = findOverlaps(input, true)

	fun part2(input: List<String>): Int = findOverlaps(input, false)

	val testInput = readInput("Day04_test")
	check(part1(testInput) == 2)
	check(part2(testInput) == 4)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}
