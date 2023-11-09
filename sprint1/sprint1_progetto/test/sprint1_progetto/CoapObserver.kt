package sprint1_progetto

import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import unibo.basicomm23.coap.CoapConnection
import unibo.basicomm23.utils.ColorsOut
import unibo.basicomm23.utils.CommUtils

class CoapObserver : CoapHandler {

    private var actor : String = ""
    private var context : String = ""
    private lateinit var connection : CoapConnection
    private var messages : MutableList<String> = ArrayList()
    private var hostname : String = ""
    private var port : Int = 0

    fun setActor(actor:String){
        this.actor = actor;
    }

    fun setContext(ctx:String){
        context = ctx
    }

    fun setHostname(hn:String){
        hostname = hn
    }

    fun setPort(port:Int){
        this.port = port
    }


    fun createConnection(){
        var address = hostname + ":" + port
        var path = context + "/" + actor
        connection = CoapConnection(address, path)
        connection.observeResource(this);
        while (connection.request("") == "0") {
            ColorsOut.out("Wating for connection to $actor", ColorsOut.BLUE)
            CommUtils.delay(100)}

        ColorsOut.out("created connection",ColorsOut.CYAN)
    }



    fun closeConnection(){
        connection.close()
    }

    fun getMessages(): MutableList<String> {
        return messages
    }


    fun clear(){
        messages.clear()
    }





    override fun onLoad(response: CoapResponse) {
        messages.add(response.responseText)
    }

    override fun onError() {
        ColorsOut.outerr("Observer error")
    }


}