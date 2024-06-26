import it.unibo.kactor.MsgUtil.buildDispatch
import it.unibo.kactor.MsgUtil.buildRequest
//import jdk.internal.org.jline.utils.Colors
import unibo.basicomm23.interfaces.Interaction
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.utils.ColorsOut
import unibo.basicomm23.utils.CommSystemConfig
import unibo.basicomm23.utils.CommUtils
import kotlin.concurrent.thread
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue


class TestDeposit {
    companion object {
        val hostname = "localhost"
        val port = 9990
        val actorName = "transporttrolley"
        val ctxName = "ctxcoldstorageservice"
        val facade = "coldstorageservice"
    }
    private lateinit var obs : CoapObserver1
    private lateinit var interaction : Interaction


    @BeforeTest
    fun setup() {
        CommSystemConfig.tracing = false
        ColorsOut.outappl("Test Started", ColorsOut.CYAN)

        obs = CoapObserver1()
        var thread = thread {
            RunDepositTest().main()
        }
        CommUtils.delay(20000)
        /*
        obs.addContext(ctxName, Pair(hostname,port))
        obs.addActor(actorName, ctxName)

         */
        //obs.createCoapConnection(actorName)

        obs.createCoapConnection(hostname, port, ctxName, actorName)

        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
            ColorsOut.outappl("TCP Connected to the context established.", ColorsOut.MAGENTA);
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    @AfterTest
    fun shutdown(){
        obs.clearCoapHistory()
        obs.closeAllCoapConnections()

        try {
            interaction.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        CommUtils.delay(1000)
        ColorsOut.outappl("===== TEST Completed =====", ColorsOut.CYAN)
        CommUtils.delay(1000)
    }



    @Test
    fun testDeposit(){
        ColorsOut.outappl("TEST DEPOSIT START",ColorsOut.CYAN)
        obs.clearCoapHistory()
        val storereq = buildRequest("testDeposit","storerequest","storerequest(10)",facade)

        ColorsOut.outappl("sending store request", ColorsOut.CYAN)

        var reply : String = interaction.request(storereq.toString())

        assertTrue(reply.contains("loadaccepted"))
        if (reply.contains("loadaccepted"))
            ColorsOut.outappl("TEST storerequest completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.outappl("TEST storerequest completed FAILED",ColorsOut.RED)

        reply = reply.split(",")[4]
        var ticket : String= reply.replace("loadaccepted(","");
        ticket = ticket.replace(")","");


        val verifyticket = buildRequest("testDeposit", "verifyticket", "verifyticket("+ ticket +")", facade)

        ColorsOut.outappl("sending verify ticket request", ColorsOut.CYAN)

        reply = interaction.request(verifyticket.toString())

        assertTrue(reply.contains("chargetaken"))
        if (reply.contains("chargetaken"))
            ColorsOut.outappl("TEST verifyticket completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.outappl("TEST verifyticket completed FAILED",ColorsOut.RED)


        val loaddone = buildRequest("testDeposit", "loaddone", "loaddone(" + ticket +")", facade)
        ColorsOut.outappl("sending loaddone request", ColorsOut.CYAN)
        reply = interaction.request(loaddone.toString())

        assertTrue(reply.contains("goaway"))
        if (reply.contains("goaway"))
            ColorsOut.outappl("TEST loaddone completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.outappl("TEST loaddone completed FAILED",ColorsOut.RED)
        CommUtils.delay(500)


        ColorsOut.outappl("sending stop message", ColorsOut.CYAN)

        val stop = buildDispatch("testDeposit", "stop", "stop(_)", facade)
        interaction.forward(stop)

        CommUtils.delay(2000)

        ColorsOut.outappl("sending resume message", ColorsOut.CYAN)
        val resume = buildDispatch("testDeposit", "resume", "resume(_)", facade)
        interaction.forward(resume)



        CommUtils.delay(25000)

        val list = mutableListOf(
            "test_deposit(handle_loaddone)",
            "test_deposit(move_to_indoor)",
            "test_deposit(pickup_completed)",
            "test_deposit(stopped)",
            "test_deposit(resumed)",
            "test_deposit(move_to_port)",
            "test_deposit(deposit_done)",
            "test_deposit(returning_home)"
        )

        ColorsOut.outappl("stringa finale: " + obs.getCoapHistory().toString(),ColorsOut.GREEN)
        assertTrue(obs.getCoapHistory().containsAll(list))

        if (obs.getCoapHistory().containsAll(list))
            ColorsOut.outappl("TEST DEPOSIT PASSED",ColorsOut.GREEN)
        else
            ColorsOut.outappl("TEST DEPOSIT FAILED",ColorsOut.RED)

        val getweight = buildRequest("testDeposit","getweight","getweight(_)",facade)
        reply = interaction.request(getweight.toString())
        var currentweight = reply.split(",")[4]
        var freespace = reply.split(",")[5]
        currentweight = currentweight.replace("currentweight(","")
        freespace = freespace.replace(")","")
        assertTrue(currentweight.toInt() == 10)
        assertTrue(freespace.toInt() == 90)
        ColorsOut.outappl("Current weight: $currentweight freespace: $freespace",ColorsOut.GREEN)
        if (currentweight.toInt() == 10 && freespace.toInt() == 90)
            ColorsOut.outappl("TEST getweight completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.outappl("TEST getweight completed FAILED",ColorsOut.RED)


    }



}