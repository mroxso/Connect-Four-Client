package de.aiarena.community

fun main(args: Array<String>){
    SimpleConnectFourClient("ai-arena.de",1805,"key1")

    while(true){
        Thread.sleep(5000)
    }
}

class SimpleConnectFourClient(host: String, port: Int, secret: String){
    private val client: AMTPClient

    init{
        client = AMTPClient(host,port,secret,this::onMessage,debug=true)
    }

    private val map = Array(7){0}
    private fun onMessage(msg: MessageFromServer, myTurn: Boolean){
        val rnd = java.util.Random()

        msg.headers["Column"]?.let{
            map[it.toInt()]++
        }

        var column = rnd.nextInt(7)
        while(map[column] >= 6){
            column = (column + 1) % 7
        }

        if(!myTurn){
            return
        }

        val myAction = MessageToServer(
                "GAME",
                "AMTP/0.0",
                hashMapOf(
                        "Action" to "Put",
                        "Column" to column.toString()
                )
        )

        client.send(myAction)
        { resp
        ->
            if(resp.code != 0){
                println("PANIC "+resp.code)
            }
        }
    }
}