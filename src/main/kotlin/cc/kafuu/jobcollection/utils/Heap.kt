package cc.kafuu.jobcollection.utils

class Heap<T>(val compare: (T, T) -> Boolean) {
    companion object {
        fun <T> instanceMinHeap(): Heap<T> where T : Comparable<T> = Heap<T> { a, b -> a < b }
        fun <T> instanceMaxHeap(): Heap<T> where T : Comparable<T> = Heap<T> { a, b -> a > b }
    }

    private val list = ArrayList<T>()

    public fun topOrNull() = synchronized(list) { list.firstOrNull() }
    public fun top() = synchronized(list) { list.first() }
    public fun isEmpty() = synchronized(list) { list.isEmpty() }

    public fun add(value: T) {
        synchronized(list) {
            list.add(value)
            fixUp(list.lastIndex)
        }
    }

    /**
     * 弹出堆顶元素
     * 如果堆为空则返回null
     * */
    public fun popOrNull() = if (list.isEmpty()) null else pop()

    public fun pop(): T {
        synchronized(list) {
            if (list.isEmpty()) {
                throw Exception("Heap is empty")
            }

            //将堆顶元素与最后一个元素替换
            swap(0, list.lastIndex)

            //取出最后一个元素
            val lastElement = list.removeLast()

            //下沉堆顶元素
            firstFixDown()

            return lastElement
        }
    }

    /**
     * 将指定元素上浮
     * */
    private fun fixUp(index: Int) {
        var current = index

        while (current > 0) {
            val parent = ((current + 1) shr 1) - 1
            if (compare(list[current], list[parent])) {
                swap(parent, current)
                current = parent
            } else {
                break
            }
        }
    }

    /**
     * 将第一个元素下沉到合适位置
     * */
    private fun firstFixDown() {
        var index = 0
        while (index < list.size) {
            val rightChild = (index + 1) shl 1
            val leftChild = rightChild - 1

            val minChild = when {
                leftChild >= list.size -> break
                rightChild >= list.size -> leftChild
                else -> if (compare(list[leftChild], list[rightChild])) leftChild else rightChild
            }

            if (compare(list[minChild], list[index])) {
                swap(index, minChild)
                index = minChild
            } else break
        }
    }

    /**
     * 交换两个元素在列表中到位置
     * */
    private fun swap(a: Int, b: Int) {
        list[b] = list[a].also { list[a] = list[b] }
    }


}


