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
    val ctl: MutableList<String> = ArrayList() // Consolidated Transaction List
    val copyCtl: MutableList<String> = ArrayList() // Copy of Consolidated Transaction List
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

    transactionList.forEach {
        it.forEach { product ->
            ctl.add(product)
        }
    }

    transactionList.forEach { sortedTransactionList.add(it.asSequence().sorted().toMutableList()) }

    var rpc = 1 // Required Item Pair(s) Count
    while (prioriList.size != 1) {

        sortedTransactionList.forEach {
            var innerRpc = 0
            while (innerRpc < rpc) {

                innerRpc++
            }
        }

        rpc++
    }

    copyCtl.addAll(ctl.asSequence().sorted().toMutableList())

    ctl.clear()

    ctl.addAll(copyCtl)

    ctl.forEachIndexed { _, s ->
        val itemList: MutableList<String> = ArrayList()
        itemList.add(s)
        val map: MutableMap<MutableList<String>, Int> = HashMap()
        map.putIfAbsent(
            itemList,
            getItemPairCount(itemList.asSequence().sorted().toMutableList(), sortedTransactionList)
        )
        prioriList.add(map)
    }

    println(
        """

        Original: $transactionList
        Original Sorted: $sortedTransactionList
        Consolidated: $ctl
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
        while (it.containsAll(ip)) count++
    }
    return count
}