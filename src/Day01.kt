fun main() {
	data class Food(val calories: Int)

	class Elf {
		private val inventory = mutableListOf<Food>()

		fun add(food: Food) = inventory.add(food)
		fun getInventory() = inventory.toList()
		fun getTotalEnergy() = inventory.sumOf { it.calories }
	}

	fun getElves(input: List<String>): List<Elf> {
		val elves = mutableListOf<Elf>()
		var currentElf = Elf()

		input.forEachIndexed { index, s ->
			if (s.isEmpty()) {
				if (currentElf.getInventory().isNotEmpty()) {
					elves.add(currentElf)
				}

				currentElf = Elf()
			}
			else {
				currentElf.add(Food(s.toInt()))
			}

			if (index == input.size - 1) {
				elves.add(currentElf)
			}
		}

		return elves.toList()
	}

	fun part1(input: List<String>): Int {
		return getElves(input).maxOf { it.getTotalEnergy() }
	}

	fun part2(input: List<String>): Int {
		val elves = getElves(input).sortedBy { it.getTotalEnergy() }.reversed()
		return elves.take(3).sumOf { it.getTotalEnergy() }
	}

	val testInput = readInput("Day01_test")
	check(part1(testInput) == 24_000)
	check(part2(testInput) == 45_000)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}
