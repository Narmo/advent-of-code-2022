fun main() {
	data class Entry(val name: String, var size: Int, val isDirectory: Boolean, val children: MutableList<Entry> = mutableListOf())

	fun findEntries(start: Entry, maxSize: Int): List<Entry> {
		val result = mutableListOf<Entry>()

		if (start.isDirectory && start.size < maxSize) {
			result.add(start)
		}

		start.children.forEach {
			result.addAll(findEntries(it, maxSize))
		}

		return result.filter { it.size < maxSize }
	}

	fun calcSizes(entry: Entry) {
		if (!entry.isDirectory) {
			return
		}

		entry.children.forEach {
			calcSizes(it)
		}

		entry.size += entry.children.sumOf { it.size }
	}

	fun printTree(entry: Entry, indent: String = "") {
		println("$indent${entry.name.trim('/')}${if (entry.isDirectory) "/ (${entry.size})" else " (${entry.size})"}")

		for (child in entry.children) {
			printTree(child, "$indent  ")
		}
	}

	fun buildTree(input: List<String>): Entry {
		val deque = ArrayDeque(input)
		val currentPath = mutableListOf<Entry>()
		var root: Entry? = null

		while (deque.isNotEmpty()) {
			val currentLine = deque.removeFirst()

			when {
				currentLine.startsWith("$") -> { // process command
					val commandContents = currentLine.split(" ")
					val command = commandContents[1]
					val argument = if (commandContents.size > 2) commandContents[2] else null

					print("Command: $command")

					if (argument != null) {
						print(", argument: $argument\n")
					}
					else {
						print("\n")
					}

					when (command) {
						"cd" -> {
							when (argument) {
								".." -> {
									currentPath.removeLast()
								}

								null -> {
									// skip
								}

								else -> {
									val entry = currentPath.lastOrNull()?.children?.firstOrNull { it.name == argument } ?: Entry(argument, 0, true)

									currentPath.add(entry)

									if (argument == "/") {
										root = entry
									}
								}
							}
						}

						"ls" -> {
							// ignore this command and skip to next line
						}
					}

					println("Current path: ${currentPath.joinToString("/") { it.name }.substring(1)}")
				}

				currentLine.first().isDigit() -> { // process file
					val (size, name) = currentLine.split(" ")
					println("Adding file: $name, Size: $size to ${currentPath.joinToString("/") { it.name }.replace("//", "/")}")
					currentPath.last().children.add(Entry(name, size.toInt(), false))
				}

				currentLine.startsWith("dir") -> { // process directory
					val (_, name) = currentLine.split(" ")
					currentPath.last().children.add(Entry(name, 0, true))
				}
			}
		}

		return root?.also { calcSizes(it) } ?: throw IllegalStateException("No root found")
	}

	fun part1(input: List<String>): Int {
		println("\n=== Starting traverse ===\n")
		val root = buildTree(input)
		println("\n=== Finished traverse ===\n\nResulting tree:\n")
		printTree(entry = root)
		return findEntries(root, 100_000).sumOf { it.size }
	}

	fun part2(input: List<String>): Int {
		val total = 70_000_000
		val minReq = 30_000_000
		val root = buildTree(input)
		val spaceLeft = total - root.size

		return findEntries(root, Int.MAX_VALUE).sortedBy {
			it.size
		}.first {
			spaceLeft + it.size >= minReq
		}.size
	}

	val testInput = readInput("Day07_test")
	check(part1(testInput) == 95437)
	check(part2(testInput) == 24933642)

	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}
