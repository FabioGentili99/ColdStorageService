import it.unibo.kactor.MsgUtil.buildDispatch
import it.unibo.kactor.MsgUtil.buildRequest
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

        CommUtils.delay(2000)
        obs.addContext(ctxName, Pair(hostname,port))
        obs.addActor(actorName, ctxName)
        obs.createCoapConnection(actorName)

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
        ColorsOut.out("TEST DEPOSIT START",ColorsOut.CYAN)
        obs.clearCoapHistory()
        val storereq = buildRequest("testDeposit","storerequest","storerequest(10)",facade)

        ColorsOut.out("sending store request", ColorsOut.CYAN)

        var reply : String = interaction.request(storereq.toString())

        assertTrue(reply.contains("loadaccepted"))
        if (reply.contains("loadaccepted"))
            ColorsOut.out("TEST storerequest completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.out("TEST storerequest completed FAILED",ColorsOut.RED)

        reply = reply.split(",")[4]
        var ticket : String= reply.replace("loadaccepted(","");
        ticket = ticket.replace(")","");


        val verifyticket = buildRequest("testDeposit", "verifyticket", "verifyticket("+ ticket +")", facade)

        ColorsOut.out("sending verify ticket request", ColorsOut.CYAN)

        reply = interaction.request(verifyticket.toString())

        assertTrue(reply.contains("chargetaken"))
        if (reply.contains("chargetaken"))
            ColorsOut.out("TEST verifyticket completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.out("TEST verifyticket completed FAILED",ColorsOut.RED)


        val loaddone = buildRequest("testDeposit", "loaddone", "loaddone(" + ticket +")", facade)
        ColorsOut.out("sending loaddone request", ColorsOut.CYAN)
        reply = interaction.request(loaddone.toString())

        assertTrue(reply.contains("goaway"))
        if (reply.contains("goaway"))
            ColorsOut.out("TEST loaddone completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.out("TEST loaddone completed FAILED",ColorsOut.RED)
        CommUtils.delay(500)


        ColorsOut.out("sending stop message", ColorsOut.CYAN)

        val stop = buildDispatch("testDeposit", "stop", "stop(_)", facade)
        interaction.forward(stop)

        CommUtils.delay(2000)

        ColorsOut.out("sending resume message", ColorsOut.CYAN)
        val resume = buildDispatch("testDeposit", "resume", "resume(_)", facade)
        interaction.forward(resume)



        CommUtils.delay(15000)
        val list = mutableListOf(
            "test_deposit(handle_loaddone)",
            "test_deposit(move_to_indoor)",
            "test_deposit(pickup_completed)",
            "tets_deposit(stopped)",
            "test_deposit(resumed)",
            "test_deposit(move_to_port)",
            "test_deposit(deposit_done)",
            "test_deposit(returning_home)"
        )

        ColorsOut.out(obs.getCoapHistory().toString(),ColorsOut.GREEN)
        assertTrue(obs.getCoapHistory().containsAll(list))

        if (obs.getCoapHistory().containsAll(list))
            ColorsOut.out("TEST DEPOSIT PASSED",ColorsOut.GREEN)
        else
            ColorsOut.out("TEST DEPOSIT FAILED",ColorsOut.RED)
    }



}