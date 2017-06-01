
package BMP.Model

data class Info(private val bytes: ByteArray, val viewerWay: Int = 1) {

    val version: Int

    val width: Int
    val height: Int

    val bitsInPixel: Int

    val data: Array<Int>

    private val rawData: ByteArray
    private val rawTable: ByteArray?
    private val table: Array<Int>?

    init {

        val start = bytesToInt(0x0a, 4, bytes)

        rawData = bytes.sliceArray(start..bytes.size - 1)
        version = bytesToInt(0x0E, 4, bytes)

        when (version) {
            12 -> {
                width = bytesToInt(0x12, 2, bytes)
                height = bytesToInt(0x14, 2, bytes)
                bitsInPixel = bytesToInt(0x18, 2, bytes)
            }
            40, 108, 124 -> {
                width = bytesToInt(0x12, 4, bytes)
                height = bytesToInt(0x16, 4, bytes)
                bitsInPixel = bytesToInt(0x1C, 2, bytes)
            }
            else -> throw IllegalArgumentException("Something went wrong")
        }

        rawTable = if (bitsInPixel <= 8) {
            bytes.sliceArray(version + 0X0E..start - 1)
        }
        else null

        if (viewerWay == 1) {
            table = parseTable()
            data = parsing()
        }
        else TODO("Realize some other ways of visualization")
    }

    private fun bytesToInt(startIndex: Int, byteAmount: Int, source: ByteArray): Int {

        var value: Int = 0

        (startIndex + byteAmount - 1 downTo startIndex)
                .asSequence()
                .map { byteToUnsignedInt(source[it]) }
                .forEach { value = (value shl 8) + it }
        return value
    }

    private fun byteToUnsignedInt(byte: Byte): Int {
        if (byte.toInt() < 0) {
            return byte.toInt() + 256
        }
        else {
            return byte.toInt()
        }
    }

    private fun parseTable(): Array<Int>? {
        val table: Array<Int>?
        if (rawTable != null) {
            table = Array(rawTable.size / 4, {0})
            for (i in 0..rawTable.size - 1 step 4) {
                table[i / 4] = bytesToInt(i, 4, rawTable)
            }
        }
        else {
            table = null
        }
        return table
    }

    private fun parsing(): Array<Int> {
        val data = Array(width * height, {0})
        val alignment = (4 - ((width * bitsInPixel / 8) % 4)) % 4
        when (bitsInPixel) {
            24 -> {
                for (i in 0..height - 1) {
                    for (j in 0..width - 1) {
                        val index = (i * (3 * width + alignment) + 3 * j)
                        data[i * width + j] = bytesToInt(index, 3, rawData)
                    }
                }
            }
            32 -> {
                for (i in 0..height - 1) {
                    for (j in 0..width - 1) {
                        val index = (i * (4 * width + alignment) + 4 * j)
                        data[i * width + j] = bytesToInt(index, 4, rawData)
                    }
                }
            }
            8 -> {
                for (i in 0..height - 1) {
                    for (j in 0..width - 1) {
                        val index = (i * (width + alignment) + j)
                        data[i * width + j] = table!![byteToUnsignedInt(rawData[index])]
                    }
                }
            }
            else -> throw IllegalArgumentException("Invalid bits per pixel")
        }
        return data
    }
}
