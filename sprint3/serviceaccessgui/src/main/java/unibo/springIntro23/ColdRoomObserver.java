package unibo.springIntro23;
import org.springframework.stereotype.Component;
import unibo.basicomm23.coap.CoapConnection;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.utils.CommUtils;
@Component
public class ColdRoomObserver implements CoapHandler{
    String CSIPADDRESS = "127.0.0.1";
    int CSPORT = 9990;
    String ctxqakdest = "ctxcoldstorageservice";

    public ColdRoomObserver(){
        System.out.println("observer started");

        CoapConnection coldroomconn = new CoapConnection(CSIPADDRESS+":"+CSPORT, ctxqakdest+"/coldroommanager" );
        coldroomconn.observeResource( this );
    }


    @Override
    public void onLoad(CoapResponse response) {
        CommUtils.outcyan("ColdRoomCoapObserver changed! " + response.getResponseText() );
        WebSocketConfiguration.wshandler.sendToAll("" + response.getResponseText());
    }

    @Override
    public void onError() {
        System.out.println("ColdRoomCoapObserver observe error!");
    }
}