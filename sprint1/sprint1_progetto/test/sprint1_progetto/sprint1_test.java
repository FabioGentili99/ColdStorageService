package sprint1_progetto;

import it.unibo.ctxtestsprint1.MainCtxtestsprint1Kt;
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


public class sprint1_test {
	private final static String hostname = "localhost";
    private final static int port = 9999;
    private final static String actorName = "test_coldstorageservice_sprint1";

    private Thread ctx_test;
    private ActorBasic actorColdStorage;
    private Interaction interaction;
    

    
    
    @Before
    public void startup() {
        CommSystemConfig.tracing = false;

        setupColdStorageService();

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
    
    
    private void setupColdStorageService() {
        new Thread(MainCtxtestsprint1Kt::main).start();

        actorColdStorage = QakContext.Companion.getActor(actorName);
        while(actorColdStorage == null) {
            CommUtils.delay(500);
            actorColdStorage = QakContext.Companion.getActor(actorName);
        }

        ColorsOut.outappl("[Test_ColdStorageService_sprint1] UP", ColorsOut.MAGENTA);
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
            IApplMessage msg = MsgUtil.buildRequest("testTicketOK","verifyticket","verifyticket(1235)",actorName);
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
    public void testWrongTicket() {
    	try {

            IApplMessage msg = MsgUtil.buildRequest("testTicketOK","verifyticket","verifyticket(1237)",actorName);
            ColorsOut.outappl(msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());
            assertTrue(reply.contains("invalidticket"));
            if(reply.contains("wrong_ticket"))
                ColorsOut.outappl("TEST wrongTicket: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST wrongTicket: FAILED", ColorsOut.RED);
    	}catch (Exception e) {
            e.printStackTrace();}
        }
    



	@Test
	public void testExpiredTicket() {
		try {

            IApplMessage msg = MsgUtil.buildRequest("testTicketOK","verifyticket","verifyticket(1236)",actorName);
            ColorsOut.outappl(msg.toString(), ColorsOut.MAGENTA);

            String reply = interaction.request(msg.toString());
            assertTrue(reply.contains("expired_ticket"));
            if(reply.contains("expired_ticket"))
                ColorsOut.outappl("TEST expiredTicket: PASSED", ColorsOut.GREEN);
            else ColorsOut.outappl("TEST expiredTicket: FAILED", ColorsOut.RED);
    	}catch (Exception e) {
            e.printStackTrace();}
    }
   

}

    
    
    
    
    
    


