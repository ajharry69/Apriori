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
    val prioriList: MutableList<MutableMap<MutableList<String>, Int>> = ArrayList() // Consolidated Transaction List
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

    sortedTransactionList.forEach {
        it.forEach { s ->
            val itemList: MutableList<String> = ArrayList()
            itemList.add(s)
            val ip = itemList.asSequence().sorted().toMutableList()
            val map: MutableMap<MutableList<String>, Int> = HashMap()
            map[ip] = getItemPairCount(ip, sortedTransactionList)

            if (!prioriList.contains(map)) prioriList.add(map)
        }
    }

//    var rpc = 1 // Required Item Pair(s) Count
//    while (prioriList.size != 1) {
//
//        sortedTransactionList.forEach {
//            val itemList: MutableList<String> = ArrayList()
//            var i = 0
//            while (i < rpc) {
//
//                val s = it[i]
//                if (!itemList.contains(s)) itemList.add(s)
//                i++
//            }
//            val ip = itemList.asSequence().sorted().toMutableList()
//            val map: MutableMap<MutableList<String>, Int> = HashMap()
//            map.putIfAbsent(
//                ip,
//                getItemPairCount(ip, sortedTransactionList)
//            )
//            prioriList.add(map)
//        }
//
//        rpc++
//    }

    println(
        """

        ORIGINAL: $transactionList
        ORIGINAL SORTED: $sortedTransactionList
        APRIORI: $prioriList
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