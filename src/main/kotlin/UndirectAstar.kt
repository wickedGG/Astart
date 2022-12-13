import java.util.*

/**
 * 무방향성(양방향성) 대응
 */

class UndirectAstar {

    val open = PriorityQueue<Node>()
    val close = HashSet<Node>()
    val neighborSet = HashMap<Pair<Int, Int>, Float>()

    fun find(start: Int, end: Int) {
        initNeighbor()
        astarAlgorithm(start, end)
        route(close, end)
    }

    private fun route(close: HashSet<Node>, end: Int) {
        var id = end
        val result = ArrayList<Int>()

        while (close.isNotEmpty()) {
            close.find {
                it.id == id
            }?.apply {
                result.add(id)
                if (parentId != null) {
                    id = Integer.parseInt(parentId)
                }
                close.remove(this)
            } ?: break
        }

        print(result.reversed().joinToString("-"))
    }

    private fun astarAlgorithm(start: Int, end: Int) {
        open.add(Node().apply {
            id = start
        })

        while (open.isNotEmpty()) {
            val topNode = open.poll()

            val saveData = close.find {
                it.id == topNode.id
            }

            if (saveData == null) {
                close.add(topNode)
            } else {
                if (saveData.f > topNode.f) {
                    close.add(topNode)
                    close.remove(saveData)
                }
            }

            if (topNode.id == end) {
                break
            }
            topNode?.apply {
                val neighbor = neighborSet.filter {
                    it.key.first == topNode.id
                }

                neighbor.forEach {
                    val parentId = it.key.first.toString()
                    val id = it.key.second
                    val g = topNode.g + it.value
                    open.add(Node().apply {
                        this.parentId = parentId
                        this.id = id
                        this.g = g
                    })
                }
            }
        }
    }


    private fun initNeighbor() {
        neighborSet.set(Pair(0, 1), 2f)
        neighborSet.set(Pair(0, 3), 5f)
        neighborSet.set(Pair(1, 6), 1f)
        neighborSet.set(Pair(3, 1), 5f)
        neighborSet.set(Pair(3, 6), 6f)
        neighborSet.set(Pair(3, 4), 2f)
        neighborSet.set(Pair(2, 1), 4f)
        neighborSet.set(Pair(4, 2), 4f)
        neighborSet.set(Pair(4, 5), 3f)
        neighborSet.set(Pair(5, 2), 6f)
        neighborSet.set(Pair(5, 5), 3f)
        neighborSet.set(Pair(6, 4), 7f)
    }


    class Node : Comparable<Node> {
        var id: Int = 0

        // 이동거리
        var g: Float = Float.MAX_VALUE

        // 남은 예상거리
        var h: Float = 0.0f
        val f: Float
            get() {
                return g + h
            }

        var parentId: String? = null

        override fun compareTo(other: Node): Int {
            return if (other.f <= this.f) 1 else -1
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Node

            if (id != other.id) return false

            return true
        }
    }

}