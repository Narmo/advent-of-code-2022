import java.util.*

fun main() {
	val model9000 = 9000
	val model9001 = 9001
	val consonantsCount = ('A'..'Z').filter { it !in "AEIOU" }.size
	val stacks = mutableListOf<MutableList<Char>>()
	var fileNum = 1

	fun printStacks(craneModel: Int) {
		val copy = stacks.map { it.toMutableList() }.toMutableList()

		buildString {
			copy.forEach {
				while (it.size < consonantsCount) {
					it.add(' ')
				}
			}

			for (j in consonantsCount - 1 downTo 0) {
				for (i in 0 until stacks.size) {
					append("[${copy[i][j]}] ")
				}

				append("\n")
			}

			repeat(stacks.size * 4 - 1) {
				append("-")
			}

			append("\n")
		}.let { result ->
			val fileName = "demo/${craneModel}/${String.format(Locale.getDefault(), "%06d", fileNum)}.txt"

			textToGraphics(result, fileName)

			fileNum += 1
		}
	}

	fun process(times: Int, source: MutableList<Char>, target: MutableList<Char>, craneModel: Int, saveToFiles: Boolean) {
		when (craneModel) {
			model9000 -> {
				repeat(times) {
					source.removeLast().let { target.add(it) }

					if (saveToFiles) {
						printStacks(craneModel)
					}
				}
			}

			model9001 -> {
				source.takeLast(times).let { target.addAll(it) }.also { repeat(times) { source.removeLast() } }

				if (saveToFiles) {
					printStacks(craneModel)
				}
			}

			else -> {
				throw RuntimeException("Wrong crane model")
			}
		}
	}

	fun arrangeCrates(input: List<String>, craneModel: Int, saveToFiles: Boolean): String {
		var processingStacks = true

		input.forEach { line ->
			if (line.isEmpty()) {
				if (saveToFiles) {
					printStacks(craneModel)
				}

				processingStacks = false
				return@forEach
			}

			if (processingStacks) {
				var stackNum = 0

				line.windowed(size = 3, step = 4) {
					val stack = stacks.getOrNull(stackNum) ?: mutableListOf<Char>().also { newStack -> stacks.add(newStack) }

					if (it[0] == '[' && it[2] == ']') {
						stack.add(0, it[1])
					}

					stackNum += 1
				}

				return@forEach
			}

			val instructions = line.split(" ").mapNotNull { it.toIntOrNull() }
			val sourceStack = stacks[instructions[1] - 1]
			val targetStack = stacks[instructions[2] - 1]

			process(instructions[0], sourceStack, targetStack, craneModel, saveToFiles)
		}

		return stacks.map { it.last() }.joinToString("")
	}

	fun part1(input: List<String>, saveToFiles: Boolean): String {
		stacks.clear()
		return arrangeCrates(input, model9000, saveToFiles)
	}

	fun part2(input: List<String>, saveToFiles: Boolean): String {
		stacks.clear()
		return arrangeCrates(input, model9001, saveToFiles)
	}

	val testInput = readInput("Day05_test")
	check(part1(testInput, false) == "CMZ")
	check(part2(testInput, false) == "MCD")

	val input = readInput("Day05")
	println(part1(input, false)) // pass `true` to save results to images
	println(part2(input, false)) // pass `true` to save results to images
}
