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

    println("Input number of transactions: ")
    val tn = try {
        input.nextInt()
    } catch (e: Exception) {
        0
    }

    var count = 0
    while (count < tn) {
        val il: MutableList<String> = ArrayList()
        println("\nInput batch ${count + 1} of $whatIs(s):")
        while (true) {
            val userInput = input.nextLine().toLowerCase()
            if (userInput == ";".toLowerCase()) break
            if (userInput.isNotEmpty()) il.add(userInput)
        }
        tl.add(il)
        count++
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

        var isEndLoop = false
        var isResetLoop = false
        val isNotRequiredAlSize = al.size <= 0 // is not required @al size - Prevents showing NULL/ZERO result
        if (isNotRequiredAlSize) {
            isEndLoop = isNotRequiredAlSize
            isResetLoop = ms < fh && !isEndLoop
            al.addAll(cAl)
            alK.addAll(cAlK)

            val nms = ms + 1
            rpc = 1
            ++ms

            println(
                """
    Attempting different Minimum Support...
    Minimum Support is resetting from [${nms - 1}] to [$nms]....
            """
            )
        }

        println(
            """

            S$rpc K-C: $cAlK
            S$rpc A-C: $cAl

            S$rpc K: $alK
            S$rpc A: $al
            ........................................................................
        """.trimIndent()
        )

        if (isResetLoop) continue
        if (isEndLoop) break

        rpc++
    }

    val endTime = System.currentTimeMillis()

    println(
        """

        ================================================================
        A.R.D: ${(endTime - startTime).toDouble() / 1000}sec
        P.R.D: ${(endTime - pStartTime).toDouble() / 1000}sec
        M.S: $ms
        ----------------------------------------------------------------
        T.L: $tl
        T.L.S: $stl
        ----------------------------------------------------------------
        AL: $al
        ================================================================
    """.trimIndent()
    )

}

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