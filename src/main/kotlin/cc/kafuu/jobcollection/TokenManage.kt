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

    private val mTokenMap = HashMap<String, String>()
    private val mTokenHeap = Heap<Token> { a, b -> a.validTime < b.validTime }

    fun newToken(user: String): Token {
        synchronized(mTokenMap) {
            val identifier = StringBuffer().apply {
                setLength(mIdentifierLength)
                do {
                    Random(System.currentTimeMillis().toInt() xor user.hashCode()).apply {
                        for (i in indices) {
                            setCharAt(i, mTokenSymbolTable[nextInt(mTokenSymbolTable.indices)])
                        }
                    }
                } while (mTokenMap.containsKey(toString()))
            }.toString()

            val currentTimeMillis = System.currentTimeMillis()
            return Token(identifier, currentTimeMillis, currentTimeMillis + mTokenValid).apply {
                mTokenMap[this.identifier] = user
                mTokenHeap.add(this)
            }
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