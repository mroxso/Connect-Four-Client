package de.aiarena.community

enum class Coins(val icon: String) {
    ENEMY("🔴"),
    ME("🔵"),
    EMPTY("🔘")
}

class WarZone {

    var map: Array<Array<Coins>> = Array(6) { kotlin.Array(7) { Coins.EMPTY } };

    fun copy(): WarZone {
        val newZone = WarZone()
        for (i in map.indices) {
            for (j in map[i].indices) {
                newZone.map[i][j] = map[i][j]
            }
        }
        return newZone
    }

    fun updateField(col: Int?, currentCoins: Coins) {
        col ?: return

        for (row in map.reversed()) {
            if (row[col] == Coins.EMPTY) {
                row[col] = currentCoins
                break
            }
        }
    }

    fun checkForWin(forPlayer: Coins): Boolean {
        return winInColFor(forPlayer) || winInRowFor(forPlayer)
    }

    fun checkForLoss(): Boolean {
        return winInColFor(Coins.ENEMY) || winInRowFor(Coins.ENEMY)
    }

    fun winInRowFor(coin: Coins): Boolean {
        var winCol: Int? = null
        var winCondition = 0
        for (row in map) {
            winCondition = 0
            for (col in row) {
                if (col == coin) winCondition++
                else winCondition = 0
                if (winCondition == 4) {
                    winCol = map.indexOf(row)
                    break
                }
            }
            if (winCondition == 4) break
        }
        return winCondition == 4
    }

    fun winInColFor(coin: Coins): Boolean {
        for (row in map.indices) {
            var winCondition = 0
            for (col in map[row].indices) {
                for (row2 in map.indices) {
                    if (map[row2][col] == coin) winCondition++
                    else winCondition = 0
                    if (winCondition == 4) return true
                }
            }
            return winCondition == 4
        }
        return false
    }

    fun printField() {
        for (row in map) {
            for (col in row) {
                print(col.icon)
            }
            println();
        }
    }

    fun getRow(row: Int): Array<Coins> {
        return map[row]
    }

    fun getNumberOfRows(): Int {
        return map.size
    }

}
