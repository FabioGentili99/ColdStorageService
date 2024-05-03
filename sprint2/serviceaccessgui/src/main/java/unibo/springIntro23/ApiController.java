package unibo.springIntro23;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class ApiController {

    private MessageSender sender = new MessageSender();


    @PostMapping("/weightreq")
    public String weightreq(){
        String msg = "msg(getweight,request,accessgui,coldsotrageservice,getweight(NO_PARAM),1)\n";
        return sender.sendMessage(msg);
    }

    @PostMapping("/depositreq")
    public String depositreq(@RequestParam String fw){
        String msg = "msg(storerequest,request,accessgui,coldstorageservice,storerequest(" + fw + "),1)\n";
        return sender.sendMessage(msg);
    }

    @PostMapping("/checkreq")
    public String checkreq(@RequestParam(name = "ticket") String ticket){
        String msg = "msg(verifyticket,request,accessgui,coldstorageservice,verifyticket(" + ticket + "),1)\n";
        return sender.sendMessage(msg);
    }

    @PostMapping("/loadreq")
    public String loadreq(@RequestParam(name = "ticket") String ticket){
        String msg = "msg(loaddone,request,accessgui,coldstorageservice,loaddone(" +ticket + "),1)\n";
        return sender.sendMessage(msg);
    }

}
