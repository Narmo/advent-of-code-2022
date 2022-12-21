import java.util.*

fun main() {
	data class Point(val x: Int, val y: Int, val elevation: Int)

	var startPoint: Point? = null
	var endPoint: Point? = null

	fun getPoints(input: List<String>): List<List<Point>> = input.mapIndexed { y, line ->
		line.mapIndexed { x, char ->
			val elevation = when (char) {
				'S' -> 'a'
				'E' -> 'z'
				else -> char
			}.code - 'a'.code

			val p = Point(x, y, elevation)

			if (char == 'S') {
				startPoint = p
			}
			else if (char == 'E') {
				endPoint = p
			}

			p
		}
	}

	fun getAdjacentPoints(points: List<List<Point>>, row: Int, col: Int): Set<Point> {        /*
		 -------------------
		 |     |     |     |
		 |     |-1,0 |     |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 | 0,-1| 0,0 | 0,1 |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 |     | 1,0 |     |
		 |     |     |     |
		 -------------------
		 */

		return listOf(Pair(1, 0), Pair(0, 1), Pair(0, -1), Pair(-1, 0)).mapNotNull {
			if ((row + it.first) in points.indices && (col + it.second) in points[row].indices) {
				points[row + it.first][col + it.second]
			}
			else {
				null
			}
		}.toSet()
	}

	fun getNeighbors(points: List<List<Point>>, point: Point): List<Point> = getAdjacentPoints(points, point.y, point.x).filter { it.elevation - point.elevation <= 1 }

	// based on this discussion: https://stackoverflow.com/questions/41789767/finding-the-shortest-path-nodes-with-breadth-first-search
	fun bfs(points: List<List<Point>>, start: Point): Int {
		var point = start
		var pathToPoint = mutableListOf(point)
		val queue: Queue<MutableList<Point>> = LinkedList()
		val visited = mutableSetOf<Point>()

		fun isVisited(point: Point): Boolean {
			if (visited.contains(point)) {
				return true
			}

			visited.add(point)

			return false
		}

		queue.add(pathToPoint)

		while (!queue.isEmpty()) {
			pathToPoint = queue.poll()
			point = pathToPoint[pathToPoint.size - 1]

			if (point == endPoint) {
				return pathToPoint.size - 1 // -1 because first point is added to path at the start
			}

			val neighbors = getNeighbors(points, point)

			for (nextNode in neighbors) {
				if (!isVisited(nextNode)) {
					val pathToNextNode = pathToPoint.toMutableList()
					pathToNextNode.add(nextNode)
					queue.add(pathToNextNode)
				}
			}
		}

		return Int.MAX_VALUE
	}

	fun part1(input: List<String>): Int {
		val points = getPoints(input)
		return bfs(points, startPoint ?: throw IllegalStateException("Start point not found"))
	}

	fun part2(input: List<String>): Int {
		val points = getPoints(input)

		val edgePoints = mutableSetOf<Point>()
		edgePoints.addAll(points.first())
		edgePoints.addAll(points.last())

		points.forEach {
			edgePoints.add(it.first())
			edgePoints.add(it.last())
		}

		return edgePoints.filter { it.elevation == 0 }.minOf { bfs(points, it) }
	}

	val testInput = readInput("Day12_test")
	check(part1(testInput) == 31)
	check(part2(testInput) == 29)

	val input = readInput("Day12")
	println(part1(input))
	println(part2(input))
}
