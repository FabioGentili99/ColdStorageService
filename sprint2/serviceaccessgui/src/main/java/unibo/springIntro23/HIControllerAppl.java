package unibo.springIntro23;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
@EnableScheduling
//@RequestMapping("/Api")
public class HIControllerAppl {
    @Value("${spring.application.name}")
    String appName;
    MessageSender sender = new MessageSender();

    @GetMapping("/home")
    public String homePage(Model model) {
        this.aggiornaPesoCorrente(model);
        model.addAttribute("arg", appName);
        return "homeCSS";
    }


    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "HIControllerAppl ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
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
