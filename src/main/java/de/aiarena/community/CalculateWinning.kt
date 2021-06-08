package de.aiarena.community

import java.util.*

class CalculateWinning(private val field: WarZone) {

    fun runMatrix(forPlayer: Coins ): Int? {
        var result: Int? = null;
        for (col in field.map[0].indices) {
            val testZone = field.copy()
            testZone.updateField(col, forPlayer)
            if (testZone.checkForWin(forPlayer)) {
                result = col;
                break
            }
        }

        return result
    }

    fun calcTurn(field: WarZone, coin: Coins):TreeMap<Int, TreeMap<Int, WarZone>> {

        val turns = TreeMap<Int, TreeMap<Int, WarZone>>()


        for(i in field.getRow(0).indices) {
            val testTurnField = field.copy()
            testTurnField.updateField(i, coin)
            val e = TreeMap<Int, WarZone>()
            e.put(i, testTurnField)
            turns[i] = e
        }


        return turns
    }

}
