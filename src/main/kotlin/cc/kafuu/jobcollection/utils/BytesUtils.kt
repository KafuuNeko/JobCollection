package cc.kafuu.jobcollection.utils

object BytesUtils {
    public fun bytesToHexString(bytes: ByteArray): String {
        val table = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val result = StringBuffer()

        for (byte in bytes) {
            result.append(table[((byte.toUInt() shr 4) % 16u).toInt()])
            result.append(table[(byte.toUInt() % 16u).toInt()])
        }

        return result.toString()
    }
}