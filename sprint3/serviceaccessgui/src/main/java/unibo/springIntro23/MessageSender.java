package unibo.springIntro23;
import java.io.*;
import java.net.Socket;
public class MessageSender {

    String COLDSTORAGESERVICEIPADDRESS = "localhost";
    int COLDSTORAGESERVICEPORT = 9990;

    Socket client;
    BufferedReader reader;
    BufferedWriter writer;

    public String sendMessage(String msg){
        System.out.print(msg);
        String response = "";
        try{
            this.connectToColdStorageService();
            writer.write(msg);
            writer.flush();

            response = reader.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }

    private void connectToColdStorageService() throws IOException {

        client = new Socket(COLDSTORAGESERVICEIPADDRESS, COLDSTORAGESERVICEPORT);
        writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

    }
}
