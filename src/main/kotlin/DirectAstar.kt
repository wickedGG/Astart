import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 단일 방향성 대응
 */

class DirectAstar {
    val nodeList = ArrayList<Node>()
    val open = PriorityQueue<Node>()
    val close = HashSet<Node>()
    val neighborSet = HashSet<Neighbor>()

    fun find(start: Int, end: Int) {
        initNode()
        initNeighbor()
        astarAlgorithm(start, end)
        route(close, end)
    }


    private fun route(close: java.util.HashSet<Node>, end: Int) {
        var id = end
        val result = ArrayList<Int>()

        println(
            "min cost : ${
                close.find {
                    it.id == id
                }?.f
            }"
        )


        while (close.isNotEmpty()) {
            close.find {
                it.id == id
            }?.apply {
                result.add(id)
                if (parentId != Int.MAX_VALUE) {
                    id = parentId
                }
                close.remove(this)
            } ?: break
        }

        println(result.reversed().joinToString("-"))
    }

    private fun astarAlgorithm(start: Int, end: Int) {
        val tartgetNode = nodeList.find {
            it.id == end
        }?.apply {
            h = 0.0
        }
        var startNode = nodeList.find {
            it.id == start
        }?.apply {
            h = getDistance(this.position, tartgetNode?.position)
        }


        open.add(startNode)

        while (open.isNotEmpty()) {
            val topNode = open.poll()

            // close에서 업데이트 하는 부분
            val findCloseNode = close.find {
                it.position == topNode.position
            }

            if (findCloseNode == null) {
                close.add(topNode)
            } else {
                if (findCloseNode.f > topNode.f) {
                    close.add(topNode)
                    close.remove(findCloseNode)
                }
            }


            if (topNode.id == end) {
                break
            }



            topNode?.apply {
                val neighbor = neighborSet.filter {
                    it.nodeId == this.id || it.nodeId2 == this.id
                }

                neighbor.forEach {
                    val parentId = this.id
                    val id = if (it.nodeId == this.id) {
                        it.nodeId2
                    } else {
                        it.nodeId
                    }
                    val g = this.g + it.cost
                    val saveNode = nodeList.find { it.id == id }?.copy()

                    saveNode?.apply {
                        this.parentId = parentId
                        this.g = g
                        this.h = getDistance(
                            position,
                            tartgetNode?.position
                        )
                    }

                    val findOpenNode = open.find { node ->
                        node.position == saveNode?.position
                    }
                    if (findOpenNode == null) {
                        open.add(saveNode)
                    } else if (findOpenNode.f > saveNode?.f ?: Double.MAX_VALUE) {
                        open.add(saveNode)
                        open.remove(this)
                    }
                }
            }
        }
    }


    private fun initNode() {
        nodeList.add(Node(id = 0, Pair(1, 1)))
        nodeList.add(Node(id = 1, Pair(4, 4)))
        nodeList.add(Node(id = 2, Pair(6, 8)))
        nodeList.add(Node(id = 3, Pair(3, 6)))
        nodeList.add(Node(id = 4, Pair(2, 10)))
        nodeList.add(Node(id = 5, Pair(7, 9)))
        nodeList.add(Node(id = 6, Pair(8, 3)))
        nodeList.add(Node(id = 7, Pair(6, 4)))
        nodeList.add(Node(id = 8, Pair(1, 5)))
        nodeList.add(Node(id = 9, Pair(3, 2)))
        nodeList.add(Node(id = 10, Pair(4, 8)))
    }

    private fun initNeighbor() {
        val neighborData = ArrayList<Pair<Int, Int>>()

        neighborData.add(Pair(0, 8))
        neighborData.add(Pair(0, 9))
        neighborData.add(Pair(9, 6))
        neighborData.add(Pair(9, 1))
        neighborData.add(Pair(1, 7))
        neighborData.add(Pair(1, 2))
        neighborData.add(Pair(1, 3))
        neighborData.add(Pair(1, 10))
        neighborData.add(Pair(8, 9))
        neighborData.add(Pair(8, 4))
        neighborData.add(Pair(4, 3))
        neighborData.add(Pair(4, 5))
        neighborData.add(Pair(3, 10))

        neighborData.add(Pair(4, 10))
        neighborData.add(Pair(10, 2))
        neighborData.add(Pair(2, 5))
        neighborData.add(Pair(5, 6))
        neighborData.add(Pair(6, 7))

        neighborData.forEach {
            neighborSet.add(Neighbor(it.first, it.second).apply {
                this.cost = getDistance(nodeList[it.first].position, nodeList[it.second].position)
            })
        }


    }


    data class Node(
        var id: Int = Int.MAX_VALUE,
        //좌표
        var position: Pair<Int, Int> = Pair(0, 0)
    ) : Comparable<Node> {


        // 이동거리
        var g: Double = 0.0

        // 남은 예상거리
        var h: Double = 0.0
        val f: Double
            get() {
                return g + h
            }

        var parentId: Int = Int.MAX_VALUE

        override fun compareTo(other: Node): Int {
            return if (other.f <= this.f) 1 else -1
        }

    }

    // 무방향성일시 대응
    class Neighbor(
        var nodeId: Int = Int.MAX_VALUE,
        var nodeId2: Int = Int.MAX_VALUE
    ) {
        var cost = Double.MAX_VALUE
    }

    fun getDistance(start: Pair<Int, Int>?, end: Pair<Int, Int>?): Double {
        if (start == null || end == null) {
            return Double.MAX_VALUE
        }

        return sqrt(
            (start.first - end.first).toDouble().pow(2)
                    + (start.second - end.second).toDouble().pow(2)
        )

    }
}