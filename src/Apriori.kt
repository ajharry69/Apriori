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
    val tl: MutableList<MutableList<String>> = ArrayList() // Transaction List
    val ctl: MutableList<String> = ArrayList() // Consolidated Transaction List
    val copyCtl: MutableList<String> = ArrayList() // Copy of Consolidated Transaction List
    val apList: MutableList<Map<MutableList<String>, Int>> = ArrayList() // Consolidated Transaction List
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
        tl.add(itemList)
        count++
    }

    tl.forEach {
        it.forEach { product ->
            ctl.add(product)
        }
    }

    copyCtl.addAll(ctl)

    ctl.forEachIndexed { i, s ->
        val list: MutableList<String> = ArrayList()
        val keyMap: Map<MutableList<String>, Int> = HashMap()
//        keyMap[] = copyCtl.contain
//        apList.add()
    }

    println("Original: $tl")
    println("Consolidated: $ctl")
}
