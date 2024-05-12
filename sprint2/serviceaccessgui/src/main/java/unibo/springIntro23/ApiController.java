package unibo.springIntro23;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class ApiController {

    private MessageSender sender = new MessageSender();


    @PostMapping("/weightreq")
    public String weightreq( Model model){
        this.aggiornaPesoCorrente(model);
        String msg = "msg(getweight,request,accessgui,coldsotrageservice,getweight(NO_PARAM),1)\n";
        return sender.sendMessage(msg);
    }

    @PostMapping("/depositreq")
    public String depositreq(@RequestParam String fw, Model model){
        this.aggiornaPesoCorrente(model);
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
    private void aggiornaPesoCorrente(Model model){
        String msg = "msg(getweight,request,accessgui,coldstorageservice,getweight(NO_PARAM),1)\n";
        String response = sender.sendMessage(msg);
        String[] weights = response.split("\\(|\\)")[2].split(",");

        int freespace = Integer.valueOf(weights[1]);
        int currentweight = Integer.valueOf(weights[0]);
        int pesoPrenotatoNoDepositato = 100 - freespace - currentweight;

        model.addAttribute("freespace", freespace);
        model.addAttribute("currentweight", currentweight);
        model.addAttribute("pesoPrenotatoNoDepositato", pesoPrenotatoNoDepositato);
    }

}
