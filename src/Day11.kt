import java.math.BigInteger

fun main() {
	class Monkey {
		val items = ArrayDeque<Long>()
		var operation: (Long, Long) -> Long = { item, _ -> item }
		var targetTrue: Int = -1
		var targetFalse: Int = -1
		var inspections = BigInteger.valueOf(0)
		var divider = 1L

		override fun toString(): String = "Monkey(items=$items, inspections=$inspections)\n"
	}

	// ended up with help of this solution: https://chasingdings.com/2022/12/11/advent-of-code-day-11-monkey-in-the-middle/
	fun fillMonkeys(input: List<String>, shouldDecreaseWorryLevel: Boolean): List<Monkey> {
		return input.chunked(7) { data ->
			val monkey = Monkey()

			monkey.divider = data[3].split(" ").last().trim().toLong()

			val items = data[1].split(":").last().trim().split(",").map { it.trim().toLong() }

			monkey.items.addAll(items.map { it })

			val operation = data[2].split("=").last().trim().split(" ")
			val op = operation[1]

			val firstOperand = if (operation[0] == "old") null else (operation[0].toLong())
			val secondOperand = if (operation[2] == "old") null else (operation[2].toLong())

			monkey.operation = when (op) {
				"+" -> { item, lcm ->
					if (shouldDecreaseWorryLevel) {
						((firstOperand ?: item) + (secondOperand ?: item)) / 3
					}
					else {
						((firstOperand ?: item) + (secondOperand ?: item)) % lcm
					}
				}

				"*" -> { item, lcm ->
					if (shouldDecreaseWorryLevel) {
						((firstOperand ?: item) * (secondOperand ?: item)) / 3
					}
					else {
						((firstOperand ?: item) * (secondOperand ?: item)) % lcm
					}
				}

				else -> {
					throw IllegalArgumentException("Unknown operation: $op")
				}
			}

			monkey.targetTrue = data[4].split(" ").last().trim().toInt()
			monkey.targetFalse = data[5].split(" ").last().trim().toInt()

			monkey
		}
	}

	fun simulateMonkeys(input: List<String>, shouldDecreaseWorryLevel: Boolean, rounds: Int): Long {
		val monkeys = fillMonkeys(input, shouldDecreaseWorryLevel)

		val lcm = monkeys.fold(1L) { acc, monkey ->
			println("Divider: ${monkey.divider}")
			acc * monkey.divider
		}

		println("LCM: $lcm")

		println("Initial monkeys: $monkeys")

		repeat(rounds) {
			monkeys.forEach { monkey ->
				while (monkey.items.isNotEmpty()) {
					var item = monkey.items.removeFirst()
					monkey.inspections += BigInteger.ONE

					item = monkey.operation(item, lcm)

					if (item % monkey.divider == 0L) {
						monkeys[monkey.targetTrue].items.addLast(item)
					}
					else {
						monkeys[monkey.targetFalse].items.addLast(item)
					}
				}
			}
		}

		println(monkeys)

		return monkeys.sortedByDescending { it.inspections }.take(2).fold(BigInteger.ONE) { acc, monkey -> acc * monkey.inspections }.toLong()
	}

	fun part1(input: List<String>): Long = simulateMonkeys(input, shouldDecreaseWorryLevel = true, rounds = 20)
	fun part2(input: List<String>): Long = simulateMonkeys(input, shouldDecreaseWorryLevel = false, rounds = 10_000)

	val testInput = readInput("Day11_test")
	check(part1(testInput) == 10605L)
	check(part2(testInput) == 2713310158L)

	val input = readInput("Day11")
	println(part1(input))
	println(part2(input))
}
