package com.example.karnaughmap

import kotlin.math.pow

class Kmap(expression: String) {

    private val regexType: Array<String> = arrayOf(
        """(a'?)(b'?)""",
        """(a'?)(b'?)(c'?)""",
        """(a'?)(b'?)(c'?)(d'?)""",
        """(a'?)(b'?)(c'?)(d'?)(e'?)""",
        """(a'?)(b'?)(c'?)(d'?)(e'?)(f'?)"""
    )
    var varsCount: Int
    val values: List<Boolean>
    var grayCodeTable: List<List<Boolean>>
    val rows: Int
    val cols: Int

    init {
        val n = requireNotNull(countVarsNumber(expression))
        varsCount = n
        grayCodeTable = createGrayCode(n)
        values = constructKMap(expression, n, grayCodeTable)
        rows = countRows(n)
        cols = countCols(n)
    }

    /**
     * Посчитать количество неизвестных переменных
     * @param {String} выражение
     * @return {Int?} количество переменных
     */
    private fun countVarsNumber(s: String): Int? {
        val arr = MutableList(26) {0}
        for (x in s) {
            if (x in 'a'..'z') {
                arr[x-'a']++
            }
        }
        arr.sortedDescending()
        val arr2 = arr.filter { it > 0 }
        var k = 1
        var error = false
        while (k < arr2.size && !error) {
            if (arr2[k] != arr2[k-1]) {
                error = true
            }
            k++
        }
        return if (error) null else k
    }

    /**
     * Получить значения логического выражения
     * @param {String} логическое выражение
     * @param {Int} количество переменных
     * @param {List<List<Boolean>> код Грея
     * @return {List<Boolean>} список возможных значений функции
     */
    private fun constructKMap(expression: String, count: Int, grayTable: List<List<Boolean>>): List<Boolean> {
        val text = expression.replace("""\s+""".toRegex(), "").toLowerCase()
        val solveList = mutableListOf<Boolean>()
        val regex = regexType[count-2].toRegex()
        grayTable.size

        for (i in grayTable.indices) {
            var matchResult = regex.find(text)
            var hasTrueConjunction = false
            while (null != matchResult?.value && !hasTrueConjunction) {
                var allIsTrue = true
                for (j in 1..count) {
                    val cell = if (matchResult.groups[j]!!.value.getOrNull(1) == '\'') !grayTable[i][j-1] else grayTable[i][j-1]
                    if (!cell) {
                        allIsTrue = false
                        break
                    }
                }
                if (allIsTrue) {
                    hasTrueConjunction = true
                }
                matchResult = matchResult.next()
            }
            solveList.add(hasTrueConjunction)
        }
        return solveList
    }

    /**
     * Построить таблицу по коду Грея
     * @param {Int} количество переменных
     * @return {List<List<Boolean>> построенная таблица кода Грея
     */
    private fun createGrayCode(n: Int): List<List<Boolean>> {
        val g = mutableListOf(mutableListOf(false), mutableListOf(true))
        grayCodeRecurse(g, n-1)
        return g
    }

    /**
     * Построить рекурсивно таблицу по коду Грея с различными значениями переменных
     * @param {MutableList<MutableList<Boolean>> двумерный список текущей таблицы
     * @param {Int} количество переменных
     */
    private fun grayCodeRecurse(g: MutableList<MutableList<Boolean>>, n: Int) {
        val k = g.size
        if (n <= 0) {
            return
        }
        else {
            for (i in k-1 downTo 0) {
                val temp = mutableListOf(true)
                temp.addAll(g[i])
                g.add(temp)

            }
            for (i in k-1 downTo 0) {
                g[i].add(0, false)
            }
            grayCodeRecurse(g, n-1)
        }
    }

    private fun countRows(n: Int): Int {
        return 2 * 2.0.pow((n - 2).div(2).toDouble()).toInt()
    }

    private fun countCols(n: Int): Int {
        return 2 * 2.0.pow((n - 1).div(2).toDouble()).toInt()
    }
}