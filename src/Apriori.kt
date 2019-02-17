import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    println("========Apriori Algo Program========\n\n")
    val whatIs = "product"
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
    val input = Scanner(System.`in`)
    val transactionList: MutableList<MutableList<String>> = ArrayList() // Transaction List
    val sortedTransactionList: MutableList<MutableList<String>> = ArrayList() // Sorted Transaction List
    val al: MutableList<MutableMap<MutableList<String>, Int>> = ArrayList() // Apriori List
    val ms = 2
    var count = 0

    println("Input number of transactions: ")
    val tn = try {
        input.nextInt()
    } catch (e: Exception) {
        0
    }

    while (count < tn) {
        val itemList: MutableList<String> = ArrayList()
        println("\nInput batch ${count + 1} of $whatIs(s):")
        while (true) {
            val userInput = input.next().toLowerCase()
            if (userInput == ";".toLowerCase()) break
            itemList.add(userInput)
        }
        transactionList.add(itemList)
        count++
    }

    transactionList.forEach { sortedTransactionList.add(it.asSequence().sorted().toMutableList()) }

    println(
        """

        T.L: $transactionList
        T.L.S: $sortedTransactionList

    """.trimIndent()
    )

    var rpc = 1 // Required Item Pair(s) Count
    while (al.size != 1) {
        val cAl: MutableList<MutableMap<MutableList<String>, Int>> = ArrayList() // Copy of @al
        val alK: MutableList<MutableList<String>> = ArrayList() // @al Keys
        val cAlK: MutableList<MutableList<String>> = ArrayList() // Copy of @al Keys

        sortedTransactionList.forEach {
            if (rpc <= 1) {
                it.forEach { s ->
                    val il: MutableList<String> = ArrayList()
                    il.add(s)
                    val ip = il.asSequence().sorted().toMutableList()
                    val map: MutableMap<MutableList<String>, Int> = HashMap()
                    map[ip] = getItemPairCount(ip, sortedTransactionList)

                    if (!al.contains(map)) {
                        alK.add(ip)
                        al.add(map)
                    }
                }
            } else {
                // TODO: Implement else...
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
                println("Error: ${ex.message}\n")
            }
        }

        println(
            """
            S$rpc A: $al
            S$rpc KEYS: $alK

        """.trimIndent()
        )

        rpc++
    }

    println(
        """
        ============================================================
        AL: ${al[0]}
        ============================================================
    """.trimIndent()
    )

}

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