package sprint0;
import it.unibo.ctxtestsprint0.MainCtxtestsprint0Kt;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.MsgUtil;
import it.unibo.kactor.QakContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;

import static org.junit.Assert.assertTrue;

public class TestStoreRequest {
	
	private final static String hostname = "localhost";
    private final static int port = 9999;
    private final static String actorName = "test_sprint0";

    private Thread ctx_test;
    private ActorBasic actorColdStorage;
    private Interaction interaction;



    @Before
    public void startup() {
        CommSystemConfig.tracing = false;

        setupWasteService();

        try {
            interaction = TcpClientSupport.connect(hostname, port, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @AfterEach
    public void shutdown() {
        actorColdStorage.terminate(0);
        actorColdStorage.terminateCtx(0);

        try {
            interaction.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ColorsOut.outappl("TEST TERMINATED", ColorsOut.CYAN);
    }


    private void setupWasteService() {
        new Thread(MainCtxtestsprint0Kt::main).start();

        actorColdStorage = QakContext.Companion.getActor(actorName);
        while(actorColdStorage == null) {
            CommUtils.delay(500);
            actorColdStorage = QakContext.Companion.getActor(actorName);
        }

        ColorsOut.outappl("[Test_ColdStorageService_sprint0] UP", ColorsOut.MAGENTA);
    }


    @Test
    public void testLoadAccepted() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("testLoadOK", "storerequest", "storerequest(30)", actorName);
            ColorsOut.outappl( msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("loadaccepted"));
            if(reply.contains("loadaccepted"))
                ColorsOut.outappl("TEST LoadAccepted: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST LoadAccepted: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testLoadRejected() {
        try {
            IApplMessage msg = MsgUtil.buildRequest("testLoadOK", "storerequest", "storerequest(110)", actorName);
            ColorsOut.outappl(msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("loadrejected"));
            if(reply.contains("loadrejected"))
                ColorsOut.outappl("TEST LoadRejected: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST LoadRejected: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testValidTicket(){
        try{
            IApplMessage msg = MsgUtil.buildRequest("testTicketOK","verifyticket","verifyticket(1234)",actorName);
            ColorsOut.outappl(msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("chargetaken"));
            if(reply.contains("chargetaken"))
                ColorsOut.outappl("TEST ValidTicket: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST ValidTicket: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();}
    }


    @Test
    public void testInvalidTicket(){
        try{
            IApplMessage msg = MsgUtil.buildRequest("testTicketOK","verifyticket","verifyticket(1235)",actorName);
            ColorsOut.outappl(msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());

            assertTrue(reply.contains("invalidticket"));
            if(reply.contains("invalidticket"))
                ColorsOut.outappl("TEST InvalidTicket: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST InvalidTicket: FAILED", ColorsOut.RED);
        } catch (Exception e) {
            e.printStackTrace();}
        }



}
