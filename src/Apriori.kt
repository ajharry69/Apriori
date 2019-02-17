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

        ORIGINAL: $transactionList
        ORIGINAL SORTED: $sortedTransactionList

    """.trimIndent()
    )

    var rpc = 1 // Required Item Pair(s) Count
    while (al.size != 1) {
        val cAl: MutableList<MutableMap<MutableList<String>, Int>> = ArrayList() // Copy of @al
        val alKeys: MutableList<MutableList<String>> = ArrayList()

        sortedTransactionList.forEach {
            it.forEach { s ->
                val itemList: MutableList<String> = ArrayList()
                itemList.add(s)
                val ip = itemList.asSequence().sorted().toMutableList()
                val map: MutableMap<MutableList<String>, Int> = HashMap()
                map[ip] = getItemPairCount(ip, sortedTransactionList)

                if (!al.contains(map)) {
                    alKeys.add(ip)
                    al.add(map)
                }
            }
        }

        println(
            """
            S$rpc: $al
            S$rpc KEYS: $alKeys

        """.trimIndent()
        )

        cAl.addAll(al) // Make a copy of Apriori List

        cAl.forEachIndexed { i, m ->
            val s: Int = m.getValue(alKeys[i])
            if (s < ms) {
                al.remove(m)
            }
        }

        cAl.addAll(al)

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