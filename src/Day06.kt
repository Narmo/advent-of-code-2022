fun main() {
	fun process(input: String, packetSize: Int): Int {
		var counter = 0
		val deque = ArrayDeque<Char>()

		for (i in input) {
			deque.addLast(i)

			counter += 1

			if (deque.size == packetSize) {
				if (deque.toSet().size == packetSize) {
					break
				}
				else {
					deque.removeFirst()
				}
			}
		}

		return counter
	}

	fun part1(input: String): Int = process(input, packetSize = 4)

	fun part2(input: String): Int = process(input, packetSize = 14)

	val testInput1 = readInput("Day06_test1").first()
	check(part1(testInput1) == 7)
	check(part2(testInput1) == 19)

	val testInput2 = readInput("Day06_test2").first()
	check(part1(testInput2) == 5)
	check(part2(testInput2) == 23)

	val testInput3 = readInput("Day06_test3").first()
	check(part1(testInput3) == 6)
	check(part2(testInput3) == 23)

	val testInput4 = readInput("Day06_test4").first()
	check(part1(testInput4) == 10)
	check(part2(testInput4) == 29)

	val testInput5 = readInput("Day06_test5").first()
	check(part1(testInput5) == 11)
	check(part2(testInput5) == 26)

	val input = readInput("Day06").first()
	println(part1(input))
	println(part2(input))
}
