package de.aiarena.community

fun main(args: Array<String>) {
    SimpleConnectFourClient("ai-arena.de", 1805, "YOUR_KEY")

    while (true) {
        Thread.sleep(5000)
    }
}

class SimpleConnectFourClient(host: String, port: Int, secret: String) {
    private val client: AMTPClient

    init {
        client = AMTPClient(host, port, secret, this::onMessage, debug = true)
    }

    private val warZone = WarZone()
    private fun onMessage(msg: MessageFromServer, myTurn: Boolean) {
        val rnd = java.util.Random()
        println("msg: " + msg.code)
        println("msg: " + msg.headers.toString())
        println("myTurn: $myTurn")
        if (msg.headers.containsKey("Winner")) {
            if (client.getMySlot() != msg.headers["Winner"]!!.toInt()) {
                warZone.updateField(msg.headers["Column"]?.toInt(), Coins.ENEMY)
                println("Enemy put Coin in " + msg.headers["Column"])
                if (msg.headers["Winner"]!!.toInt() == -1) {
                    println("Its a draw!!!")
                } else {
                    println("YOU LOOSE FATALITY")
                }
            } else {
                println("MEEEE WOOOOONNN!!! ¯\\_(ツ)_/¯")
            }
            warZone.printField()
            return
        }

        if(!myTurn) return

        // Wenn mein turn ist hat der gegner vorher sein zug gesetzt und ich erhalte ihn jetzt hier
        warZone.updateField(msg.headers["Column"]?.toInt(), Coins.ENEMY)
        println("Enemy put Coin in " + msg.headers["Column"])
        warZone.printField()

        var column = CalculateWinning(warZone).runMatrix(Coins.ME)
        val enemyColumn = CalculateWinning(warZone).runMatrix(Coins.ENEMY)

        if (column == null && enemyColumn != null) {
            column = enemyColumn
        }

        // do random if no win is possible
        if (column == null) {
            println("Gernarte Random Number!")
            column = rnd.nextInt(7)
            while (warZone.map[0][column!!] != Coins.EMPTY) {
                column = rnd.nextInt(7)
            }
        }

        val myAction = buildMessageToServer(column)
        warZone.updateField(column, Coins.ME)
        println("Me put Coin in $column")
        warZone.printField()

        sendTurn(myAction)
    }

    private fun sendTurn(myAction: MessageToServer) {
        client.send(myAction)
        { resp
            ->
            if (resp.code != 0) {
                println("PANIC " + resp.code)
            }
        }
    }

    private fun buildMessageToServer(column: Int) = MessageToServer(
            "GAME",
            "AMTP/0.0",
            hashMapOf(
                    "Action" to "Put",
                    "Column" to column.toString()
            )
    )

}
