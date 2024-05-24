

var ticket = "";

var IP = "http://localhost:8085/api/";

function updateDepositedWeight() {
  var freespaceElement = document.getElementById("freespace");
  var currentweightElement = document.getElementById("currentweight");
  var depositedWeightElement = document.getElementById("pesoPrenotatoNoDepositato");

  var freespace = parseInt(freespaceElement.innerHTML);
  var currentweight = parseInt(currentweightElement.innerHTML);

  var depositedWeight = 100 - freespace - currentweight;
  depositedWeightElement.innerHTML = depositedWeight;
}
document.getElementById("depositsubmit").addEventListener("submit", function(e)
{
    e.preventDefault();
    var fw = document.getElementById("foodweight").value;
    sendMessage("depositreq", "fw="+fw);
    updateDepositedWeight();
});

document.getElementById("checksubmit").addEventListener("submit", function(e)
{
    e.preventDefault();
    var t = document.getElementById("varticket").value;
    ticket = t;
    sendMessage("checkreq","ticket="+t);
});

document.getElementById("loadsubmit").addEventListener("submit", function(e)
{
    e.preventDefault();
    var t = document.getElementById("varticket").value;
    sendMessage("loadreq","ticket="+t);
});


function responsehandler(type, response){
    switch (type){
        case "weightreq":
            var weights=getMsgValue(response).split(",");
            //test per capire se il weights[0] corrisponde al freespace
            document.getElementById("freespace").innerHTML= weights[1];
            document.getElementById("currentweight").innerHTML=weights[0];



            enableButtons("default");
            break;
        case "depositreq":
            var responsebutton = getMsgType(response);
            var weights=getMsgValue(response).split(",");

            document.getElementById("freespace").innerHTML=weights[1];
            document.getElementById("currentweight").innerHTML=weights[2];
                updateDepositedWeight();
            if(responsebutton == "loadaccepted"){
                document.getElementById("maintext").innerHTML= "Richiesta di deposito accettata" ;
                enableButtons("requestaccepted");
                document.getElementById("varticket").value = weights[0];
            }else{
                document.getElementById("maintext").innerHTML= "Richiesta di deposito rifiutata" ;
                enableButtons("default");
                }
            break;
        case "checkreq":
            var verifybutton = getMsgType(response);
            var reasoninvalid= getMsgValue(response);
            if(verifybutton == "chargetaken"){
                document.getElementById("maintext").innerHTML = "Ticket valido";
                enableButtons("ticketaccepted");
            }else{
                //invalid ticket
                document.getElementById("maintext").innerHTML = "ERRORE: Ticket non valido: " +reasoninvalid;
                enableButtons("default");
            }
            break;
        case "loadreq":
            var resp2button = getMsgType(response);
            if(resp2button == "goaway")
                document.getElementById("maintext").innerHTML = "Il tuo foodload è stato preso in carico! ADDIO";

            else{
            //depositfailed
                document.getElementById("maintext").innerHTML = "ERRORE: Il tuo foodload non è stato depositato nella Cold Room !!!";
            }
            enableButtons("default");
            break;
        default:
            console.log("richiesta non riconosciuta");
    }

}


function sendMessage(request, parameters) {
    try
    {
        if(parameters)
            var url = IP+request+"?"+ parameters;
        else
            var url = IP+request;
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 ) {
                var response = this.responseText;
                //console.log("response: " + response);
                responsehandler(request, response);
            }
        };
        xhttp.open("POST", url, true);
        xhttp.setRequestHeader('Content-Type', 'text/plain');
        xhttp.send();
    }
    catch(erMsg) {
        console.log(erMsg);
    }
}


function getMsgType(msg){
    return msg.split('(')[1].split(',')[0];
}


function getMsgValue(msg){
    return msg.split(/[\(\)]/)[2];
}

function getWeightFromTicket(ticket){
    return ticket.split("_")[2];
}

function enableButtons(mode){
    switch(mode){

        case"requestaccepted":
            document.getElementById("checkbutton").removeAttribute("disabled");
            break;
        case "ticketaccepted":
            document.getElementById("checkbutton").setAttribute("disabled", "disabled");
            document.getElementById("loadbutton").removeAttribute("disabled");
            break;
        default:
            //di default il pulsante di verify ticket e di load done sono disabilitati, si abilitano step by step
            document.getElementById("checkbutton").setAttribute("disabled", "disabled");
            document.getElementById("loadbutton").setAttribute("disabled", "disabled");
    }
}