# Apriori Algorithm Program Implementation

### About apriori algorithm
In a nut-shell, Apriori is an algorithm for frequent item set mining and association rule learning 
over transactional databases. More on the algorithm can be found 
[here](https://en.wikipedia.org/wiki/Apriori_algorithm "Apriori Algorithm").

### About the program
This is [console-based](https://en.wikipedia.org/wiki/Console_application "Console Programs") 
application designed to be used via a text only computer interface.

### Usage Instructions
**N/B**: Press **ENTER** / **INSERT** after every input or command for the program to record the 
input or execute command.
1. Run the application through your terminal using the following command `java -jar apriori.jar`
2. Select data source. Two options are available:
    - Read from a **.csv** or **.txt** file by typing `1`. (recommended)            
    - Manually input the transactions by typing `2`.
3. Upon selecting option `1`, you will be required to input the 
[full/absolute path](https://en.wikipedia.org/wiki/Path_(computing\) "Absolute Path") containing 
supported file formats (.csv or .txt) files.
    > **Examples,** if you input something like `F:\abc\`, the program will check and list all 
    the supported files found in that specific directory from which you can choose the file by 
    it's corresponding number to run the algorithm on. And if you input `F:\abc\file.csv`, the 
    program will run the algorithm on and produce the out.
4. Upon selecting option `2`, you will be required to input the number of **transactions** first then 
input an unlimited number of **products** for the `1st, 2nd...` transactions respectively.
    > **Note,** after you have finished **product** input for **every** transaction, type `;` 
    then press **ENTER** / **INSERT** to input **product** set for the next transaction.
    
    > **Example,**
    >
    > | Transaction No. | Products / Itemset |
    > | ------- | ------ |
    > | 1st | Bread, Milk |
    > | 2nd | Bread, Diaper, Beer, Eggs |
    > | 3rd | Milk, Diaper, Beer, Coke |
    > | 4th | Bread, Milk, Diaper, Beer |
    > | 5th | Bread, Milk, Diaper, Coke |
      > For the above sample transaction, you can input `5` as the number of transactions 
      then input `1st` transaction's products by typing `Bread`, press **ENTER**, `Milk`, 
      press **ENTER** then `;` and press **ENTER** to input **product** set for the `2nd` 
      transaction similar pattern applies to the rest of the transactions.
      