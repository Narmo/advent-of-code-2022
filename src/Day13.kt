import kotlin.math.max

fun main() {
	val rightOrder = -1
	val wrongOrder = 1
	val same = 0

	class Packet {
		val children = mutableListOf<Packet>()
		var parent: Packet? = null
		var value = -1

		override fun toString(): String {
			if (value == -1) {
				return "[${children.joinToString(", ")}]"
			}

			return value.toString()
		}

		fun compareTo(other: Packet): Int {
			val thisChildren = if (value == -1) children.toList() else listOf(Packet().also { it.value = value })
			val otherChildren = if (other.value == -1) other.children.toList() else listOf(Packet().also { it.value = other.value })

			return compare(thisChildren, otherChildren)
		}

		private fun compare(left: List<Packet>, right: List<Packet>): Int {
			for (i in 0 until max(left.size, right.size)) {
				var leftValue = -2
				var rightValue = -2

				if (i < left.size) {
					leftValue = left[i].value
				}

				if (i < right.size) {
					rightValue = right[i].value
				}

				when {
					leftValue == -2 && rightValue == -2 -> return same
					leftValue == -2 -> return rightOrder
					rightValue == -2 -> return wrongOrder
				}

				when {
					leftValue == -1 -> {
						val result = if (rightValue == -1) {
							compare(left[i].children, right[i].children)
						}
						else {
							compare(left[i].children, listOf(Packet().apply { value = rightValue }))
						}

						if (result == same) {
							continue
						}
						else {
							return result
						}
					}

					rightValue == -1 -> {
						val result = compare(listOf(Packet().apply { value = leftValue }), right[i].children)

						if (result == same) {
							continue
						}
						else {
							return result
						}
					}
				}

				return if (leftValue < rightValue) {
					rightOrder
				}
				else if (leftValue > rightValue) {
					wrongOrder
				}
				else {
					continue
				}
			}

			return same
		}
	}

	fun parseLine(line: String): Packet {
		var currentItem: Packet? = null
		val currentInt = StringBuilder()

		val addPacket: () -> Unit = {
			val newPacket = Packet()
			newPacket.value = currentInt.toString().toInt()
			currentItem?.children?.add(newPacket)
			currentInt.clear()
		}

		line.forEach {
			when (it) {
				'[' -> {
					if (currentItem == null) {
						currentItem = Packet()
					}
					else {
						val newPacket = Packet()
						newPacket.parent = currentItem
						currentItem?.children?.add(newPacket)
						currentItem = newPacket
					}
				}

				']' -> {
					if (currentInt.isNotEmpty()) {
						addPacket()
					}

					if (currentItem?.parent == null) {
						return currentItem ?: throw IllegalArgumentException("Invalid input")
					}

					currentItem = currentItem?.parent
				}

				',' -> {
					if (currentInt.isNotEmpty()) {
						addPacket()
					}
				}

				else -> {
					if (it.isDigit()) {
						currentInt.append(it)
					}
				}
			}
		}

		throw IllegalArgumentException("Invalid input")
	}

	fun parseInput(input: List<String>): List<Pair<Packet, Packet>> = input.windowed(3, 3, true) { Pair(parseLine(it[0]), parseLine(it[1])) }

	fun parseInputFlat(input: List<String>): List<Packet> = input.mapNotNull { if (it.isNotEmpty()) parseLine(it) else null }

	fun part1(input: List<String>): Int {
		val indexes = parseInput(input).mapIndexedNotNull { index, pair ->
			if (pair.first.compareTo(pair.second) == rightOrder) {
				index + 1
			}
			else {
				null
			}
		}

		return indexes.sum()
	}

	fun part2(input: List<String>): Int {
		val packets = parseInputFlat(input).toMutableList()
		val divider1 = Packet().apply { children.add(Packet().apply { value = 2 }) }
		val divider2 = Packet().apply { children.add(Packet().apply { value = 6 }) }

		packets.add(divider1)
		packets.add(divider2)

		packets.sortWith { o1, o2 ->
			o1.compareTo(o2)
		}

		println(packets.joinToString("\n"))

		return (packets.indexOf(divider1) + 1) * (packets.indexOf(divider2) + 1)
	}

	val testInput = readInput("Day13_test")
	check(part1(testInput) == 13)
	check(part2(testInput) == 140)

	val input = readInput("Day13")
	println(part1(input))
	println(part2(input))
}
