package cc.kafuu.jobcollection

import cc.kafuu.jobcollection.bean.Token
import cc.kafuu.jobcollection.utils.Heap
import kotlin.random.Random
import kotlin.random.nextInt

class TokenManage {

    companion object {
        private const val mTokenSymbolTable = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val mTokenValid: Long = 86400000
        private const val mIdentifierLength = 32
    }

    private var mPreIdentifierHash = mTokenSymbolTable.hashCode()
    private val mTokenMap = HashMap<String, HashMap<String, String>>()
    private val mTokenHeap = Heap<Token> { a, b -> a.validTime < b.validTime }


    /*
    * 构造一个新的Token，并返回新Token的identifier
    * */
    fun newToken(): String {
        synchronized(mTokenMap) {
            val identifier = StringBuffer().apply {
                var count = 0

                setLength(mIdentifierLength)
                do {
                    Random(System.currentTimeMillis() + mPreIdentifierHash + count).apply {
                        for (i in indices) {
                            setCharAt(i, mTokenSymbolTable[nextInt(mTokenSymbolTable.indices)])
                        }
                    }
                    count += 1
                } while (mTokenMap.containsKey(toString()))

            }.toString()

            val currentTimeMillis = System.currentTimeMillis()
            Token(identifier, currentTimeMillis, currentTimeMillis + mTokenValid).apply {
                mTokenMap[this.identifier] = HashMap()
                mTokenHeap.add(this)
            }

            mPreIdentifierHash = identifier.hashCode()

            return identifier
        }
    }

    /**
     * 回收失效的Token
     * */
    fun recoveryFailureToken() {
        synchronized(mTokenMap) {
            val currentTimeMillis = System.currentTimeMillis()
            while (!mTokenHeap.isEmpty()) {
                val token = mTokenHeap.top()
                if (token.validTime <= currentTimeMillis) {
                    mTokenHeap.pop()
                    mTokenMap.remove(token.identifier)
                } else {
                    break
                }
            }
        }
    }
}