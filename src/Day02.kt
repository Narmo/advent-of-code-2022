private enum class Shape(val score: Int) {
	ROCK(1), PAPER(2), SCISSORS(3);

	fun beats(other: Shape): Boolean? = when {
		this == other -> null // this means draw
		this == PAPER && other == ROCK -> true
		this == SCISSORS && other == PAPER -> true
		this == ROCK && other == SCISSORS -> true
		else -> false
	}

	fun fromOutcome(outcome: Outcome): Shape = Shape.values().first {
		outcome == Outcome.fromBoolean(it.beats(this))
	}

	companion object {
		fun fromString(string: String): Shape = when (string) {
			"A", "X" -> ROCK
			"B", "Y" -> PAPER
			"C", "Z" -> SCISSORS
			else -> throw IllegalArgumentException("Invalid shape: $string")
		}
	}
}

private enum class Outcome(val score: Int) {
	WIN(6), DRAW(3), LOSE(0);

	companion object {
		fun fromString(string: String): Outcome = when (string) {
			"X" -> LOSE
			"Y" -> DRAW
			"Z" -> WIN
			else -> throw IllegalArgumentException("Invalid outcome: $string")
		}

		fun fromBoolean(boolean: Boolean?): Outcome = when (boolean) {
			true -> WIN
			false -> LOSE
			null -> DRAW
		}
	}
}

fun main() {
	fun part1(input: List<String>): Int = input.fold(0) { score, line ->
		score + line.split(" ").map { Shape.fromString(it) }.run {
			when (second().beats(first())) {
				true -> Outcome.WIN.score + second().score
				false -> second().score
				null -> Outcome.DRAW.score + second().score
			}
		}
	}

	fun part2(input: List<String>): Int = input.fold(0) { score, line ->
		score + line.split(" ").let { (shape, outcome) ->
			Shape.fromString(shape).run {
				Outcome.fromString(outcome).run {
					this.score + fromOutcome(this).score
				}
			}
		}
	}

	val testInput = readInput("Day02_test")
	check(part1(testInput) == 15)
	check(part2(testInput) == 12)

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}
