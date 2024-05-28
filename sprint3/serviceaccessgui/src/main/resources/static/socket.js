var socket;
var socketIP = "localhost:8085";


function sendWSMessage(message) {
    var jsonMsg = JSON.stringify( {'name': message});
    socket.send(jsonMsg);
    console.log("Sent Message: " + jsonMsg);
}

function connect(){
    var addr     = "ws://" + socketIP + "/socket"  ;
    //alert("connect addr=" + addr   );

    // Assicura che sia aperta un unica connessione
    if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);

    socket.onopen = function (event) {
        console.log("Connected to " + addr);
    };

    socket.onmessage = function (event) {
        //alert(`Got Message: ${event.data}`);
        msg = event.data;
        if (msg.includes("updateWeight")){
           updateWeightField(msg);
        }
        //alert(`Got Message: ${msg}`);
        console.log("ws-status:" + msg);

    };

    function updateWeightField(msg){
        var weights = msg.split("-");
        console.log(weights);
        if(weights.length == 3){
            document.getElementById("freespace").innerHTML= parseInt(weights[1]) ;
            document.getElementById("currentweight").innerHTML= parseInt(weights[2]) ;
            document.getElementById("pesoPrenotatoNoDepositato").innerHTML= 100 - parseInt(weights[1]) - parseInt(weights[2]) ;

        }
    }

}//connect
