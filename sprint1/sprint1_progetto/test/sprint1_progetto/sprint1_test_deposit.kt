package sprint1_progetto


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

class sprint1_test_deposit {
    companion object {
        val hostname = "localhost"
        val port = 9999
        val actorName = "test_deposit_transporttrolley"
        val ctxName = "ctxcss_test_deposit"
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
        val msg = buildRequest("testDeposit", "loaddone", "loaddone(1234)", actorName)
        ColorsOut.out("sending loaddone request", ColorsOut.CYAN)
        var reply : String = interaction.request(msg.toString())

        assertTrue(reply.contains("goaway"))
        if (reply.contains("goaway"))
            ColorsOut.out("TEST pickup completed PASSED",ColorsOut.GREEN)
        else
            ColorsOut.out("TEST pickup completed FAILED",ColorsOut.RED)
        CommUtils.delay(5000)

        CommUtils.delay(10000)
        val list = mutableListOf(
            "test_deposit_sprint1(handle_loaddone)",
            "test_deposit_sprint1(move_to_indoor)",
            "test_deposit_sprint1(pickup_completed)",
            "test_deposit_sprint1(move_to_port)",
            "test_deposit_sprint1(deposit_done)",
            "test_deposit_sprint1(returning_home)"
        )

        ColorsOut.out(obs.getCoapHistory().toString(),ColorsOut.GREEN)
        assertTrue(obs.getCoapHistory().containsAll(list))

        if (obs.getCoapHistory().containsAll(list))
            ColorsOut.out("TEST DEPOSIT PASSED",ColorsOut.GREEN)
        else
            ColorsOut.out("TEST DEPOSIT FAILED",ColorsOut.RED)



    }



}