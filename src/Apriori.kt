import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {

    val pStartTime = System.currentTimeMillis()
    val whatIs = "product"
    val input = Scanner(System.`in`)
    val tl: MutableList<MutableList<String>> = ArrayList() // Transaction List
    val stl: MutableList<MutableList<String>> = ArrayList() // Sorted @tl
    val al: MutableList<MutableMap<MutableList<String>, Int>> = ArrayList() // Apriori List
    val alK: MutableList<MutableList<String>> = ArrayList() // @al Keys
    val sff = listOf("csv") // Supported File Formats
    var ms = 2 // Minimum Support
    var fh = 0 // First Highest Support

    println("========Apriori Algo Program========\n\n")
    println(
        """
        Usage Instructions:
            General:
                Press 'ENTER' button after every $whatIs / instruction entry
            Specific:
                1). Input '0' to QUIT the program
                2). Input ';' to move to NEXT items input

    """.trimIndent()
    )

    println(
        """
        Available Data Sources:
            1). Read from csv file
            2). Manual input by typing

        Select data source (by corresponding number):
    """.trimIndent()
    )

    try {
        val ds = Scanner(System.`in`).nextLine().toInt() // Data Source

        if (ds in 1..2) {
            if (ds == 1) {
                println("\nAbsolute file path or file location:")
                val path = Scanner(System.`in`).nextLine()
                if (path.isNotEmpty()) {
                    val file = File(path)
                    if (file.isFile) {
                        if (sff.contains(file.extension.toLowerCase())) {
                            file.populateTransactionList(tl)
                        } else {
                            throw Exception("Unsupported file format. Only $sff files accepted")
                        }
                    } else {
                        val files = file.listFiles { _: File, s: String ->
                            sff.contains(s.substringAfterLast('.'))
                        }

                        if (files.isNotEmpty()) {
                            val fs = files.size // Files Size / Number of Files found
                            files.forEachIndexed { i, f -> println("${i + 1}). ${f.name}") }

                            try {
                                println("\nSelect file (by corresponding number):")
                                val fid = Scanner(System.`in`).nextLine().toInt() // File ID i.e. number from the list
                                if (fid in 1..fs) {
                                    files[fid - 1].populateTransactionList(tl)
                                } else {
                                    throw Exception("Selected option '$fid' not available in files list")
                                }
                            } catch (ex: NumberFormatException) {
                                throw Exception("Invalid input. Only integers allowed")
                            } catch (ex: Exception) {
                                throw Exception(ex.message)
                            }
                        } else {
                            throw Exception("No supported file format found. Only $sff files allowed")
                        }
                    }
                } else {
                    throw Exception("File path is empty")
                }
            } else {
                println("Input number of transactions: ")
                val tn = try {
                    Scanner(System.`in`).nextInt()
                } catch (e: Exception) {
                    0
                }

                var count = 0
                while (count < tn) {
                    val il: MutableList<String> = ArrayList() // Item List
                    println("\nInput batch ${count + 1} of $whatIs(s):")
                    while (true) {
                        val userInput = input.nextLine().toLowerCase()
                        if (userInput == ";".toLowerCase()) break
                        if (userInput.isNotEmpty()) il.add(userInput)
                    }
                    tl.add(il)
                    count++
                }
            }
        } else {
            throw Exception("Selected option '$ds' not available")
        }
    } catch (ex: Exception) {
        println("\nError: ${ex.message}")
        System.exit(0)
    }

    tl.forEach { stl.add(it.asSequence().sorted().toMutableList()) }

    val startTime = System.currentTimeMillis()

    var rpc = 1 // Required Item Pair(s) Count
    while (al.size != 1) {
        val cAl: MutableList<MutableMap<MutableList<String>, Int>> = ArrayList() // Copy of @al
        val cAlK: MutableList<MutableList<String>> = ArrayList() // Copy of @al Keys

        if (rpc <= 1) {
            resetPrioriListAndKeys(cAlK, alK, al)
            stl.forEach {
                it.forEach { s ->
                    val il: MutableList<String> = ArrayList()
                    if (!il.contains(s)) {
                        il.add(s)
                        val ip = prioriMergedKeys(il)
                        val mpc = getItemPairCount(ip, stl) // Matching List-Pair Count
                        if (fh < mpc) fh = mpc

                        val map: MutableMap<MutableList<String>, Int> = HashMap()
                        map[ip] = mpc

                        if (!al.contains(map)) {
                            alK.add(ip)
                            al.add(map)
                        }
                    }
                }
            }
        } else {
            resetPrioriListAndKeys(cAlK, alK, al)
            cAlK.forEach { a ->
                cAlK.forEachIndexed { _, b ->
                    val ip = prioriMergedKeys(a, b)
                    if (!alK.contains(ip) && rpc == ip.size) {
                        val map: MutableMap<MutableList<String>, Int> = HashMap()
                        map[ip] = getItemPairCount(ip, stl)

                        if (!al.contains(map)) {
                            alK.add(ip)
                            al.add(map)
                        }
                    }
                }
            }
        }

        cAl.addAll(al) // Make a copy of Apriori List
        cAlK.clear()
        cAlK.addAll(alK) // Make a copy of Apriori List Keys

        cAl.forEachIndexed { i, m ->
            try {
                val s: Int = m.getValue(cAlK[i])
                if (s < ms) {
                    al.remove(m)
                    alK.remove(cAlK[i])
                }
            } catch (ex: Exception) {
            }
        }

        val isEndLoop = al.size <= 0
        var isResetLoop = false
        if (isEndLoop) {
            isResetLoop = ms < fh
            al.addAll(cAl)
            alK.addAll(cAlK)

            val nms = ms + 1

            if (isResetLoop) {
                rpc = 1
                ms += 1

                println(
                    """

                Attempting different Minimum Support...
                Minimum Support is resetting from [${nms - 1}] to [$nms]....
            """.trimIndent()
                )
            }

        }

        if (!isResetLoop) {
            println(
                """

            S$rpc K-C: $cAlK
            S$rpc A-C: $cAl

            S$rpc K: $alK
            S$rpc A: $al
            ........................................................................
        """.trimIndent()
            )
        }

        if (isResetLoop) continue
        if (isEndLoop) break

        rpc += 1
    }

    val endTime = System.currentTimeMillis()

    println(
        """

        ===========================================================================================
        A.R.D: ${(endTime - startTime).toDouble() / 1000}sec
        P.R.D: ${(endTime - pStartTime).toDouble() / 1000}sec
        M.S: $ms
        -------------------------------------------------------------------------------------------
        T.L: $tl
        T.L.S: $stl
        -------------------------------------------------------------------------------------------
        AL: $al
        ===========================================================================================
    """.trimIndent()
    )

}

private fun File.populateTransactionList(tl: MutableList<MutableList<String>>) {
    var c = 0
    this.forEachLine {
        if (c > 0 && it != "") { // Skips the 1st line since it 'normally' contains column titles
            val il: MutableList<String> = ArrayList() // Item List
            it.split(Regex(",")).forEach { item -> il.add(item.cleansed()) }
            tl.add(il)
        }
        ++c
    }
}

private fun String.cleansed(): String =
    this.replace("\"", "").replace("'", "").trimStart().trimEnd()

/**
 * @param cAlK:
 * @param alK:
 * @param al:
 */
private fun resetPrioriListAndKeys(
    cAlK: MutableList<MutableList<String>>,
    alK: MutableList<MutableList<String>>,
    al: MutableList<MutableMap<MutableList<String>, Int>>
) {
    cAlK.clear()
    cAlK.addAll(alK)
    alK.clear()
    al.clear()
}

/**
 * @param l1: List of items to be merged
 * @return list
 */
fun prioriMergedKeys(vararg l1: MutableList<String>): MutableList<String> {
    val k1: MutableList<String> = ArrayList()

    l1.forEach { k1.addAll(it) }
    return k1.toSortedSet().toMutableList()
}

/**
 * @param ip: List of item pair(s) to check count of
 * @param tl: List of transaction(s) from which comparison is done
 * @return count(Int): Number of matching item pairs found
 */
fun getItemPairCount(ip: MutableList<String>, tl: MutableList<MutableList<String>>): Int {
    var count = 0
    tl.forEach {
        if (it.containsAll(ip)) count++
    }
    return count
}