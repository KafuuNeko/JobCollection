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
                var offset = mPreIdentifierHash

                setLength(mIdentifierLength)
                do {
                    Random(System.currentTimeMillis() + offset).apply {
                        for (i in indices) {
                            setCharAt(i, mTokenSymbolTable[nextInt(mTokenSymbolTable.indices)])
                        }
                        offset = offset * 31 + nextInt()
                    }
                } while (mTokenMap.containsKey(toString()))

            }.toString()

            mPreIdentifierHash = identifier.hashCode()

            val currentTimeMillis = System.currentTimeMillis()
            Token(identifier, currentTimeMillis, currentTimeMillis + mTokenValid).apply {
                mTokenMap[this.identifier] = HashMap()
                mTokenHeap.add(this)
            }

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