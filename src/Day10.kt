fun main() {
	fun part1(input: List<String>): Int {
		var x = 1
		val keyCycles = listOf(20, 60, 100, 140, 180, 220)
		var acc = 0
		var cycle = 1

		fun checkKey(c: Int): Boolean = c in keyCycles

		input.forEach { line ->
			println("command: $line")

			val entry = line.split(" ")

			if (entry.first() == "addx") {
				println("cycle: $cycle, acc: $acc, x: $x")

				cycle += 1
				acc += if (checkKey(cycle)) x * cycle else 0

				cycle += 1
				x += entry.second().toInt()
				acc += if (checkKey(cycle)) x * cycle else 0

				println("cycle: $cycle, acc: $acc, x: $x")
			}
			else {
				cycle += 1
				acc += if (checkKey(cycle)) x * cycle else 0
				println("cycle: $cycle, acc: $acc, x: $x")
			}
		}

		return acc
	}

	fun part2(input: List<String>): String {
		var x = 1
		var cycle = 1
		val pixels = arrayOfNulls<Char>(240)

		fun shouldDrawSprite(): Boolean = (cycle - 1) % 40 in (x - 1..x + 1)
		fun printPixel(pos: Int) = if (shouldDrawSprite()) pixels[pos - 1] = '#' else pixels[pos - 1] = '.'

		input.forEach { line ->
			val entry = line.split(" ")

			if (entry.first() == "addx") {
				printPixel(cycle)
				cycle += 1

				printPixel(cycle)
				cycle += 1
				x += entry.second().toInt()
			}
			else {
				printPixel(cycle)
				cycle += 1
			}
		}

		val result = pixels.toList().chunked(40).joinToString("\n") { it.joinToString("") }

		println(result)

		return result
	}

	val testInput = readInput("Day10_test")

	check(part1(testInput) == 13140)

	check(part2(testInput) == """
		##..##..##..##..##..##..##..##..##..##..
		###...###...###...###...###...###...###.
		####....####....####....####....####....
		#####.....#####.....#####.....#####.....
		######......######......######......####
		#######.......#######.......#######.....
	""".trimIndent())

	val input = readInput("Day10")
	println(part1(input))
	println(part2(input))
}
