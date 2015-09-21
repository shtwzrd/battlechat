var webSocket;
var messages = document.getElementById("messages");


function openSocket() {
    // Ensures only one connection is open at a time
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
        writeResponse("WebSocket is already opened.");
        return;
    }
    // Create a new instance of the websocket
    var id = $(this.document).attr("URL").split('/').pop();
    var domain = $(this.document).attr("baseURI").split('/')[2];
    console.log(href);
    webSocket = new WebSocket("ws://" + domain + "/session", ["v1", id]);

    // Binds functions to the listeners for the websocket.
    webSocket.onopen = function(event){
        if(event.data === undefined)
            return;

        writeResponse(event.data);
    };

    webSocket.onmessage = function(event){
        writeResponse(event.data);
    };

    webSocket.onclose = function(event){
        writeResponse("Connection closed");
    };
}

// Sends the value of the text input to the server
function send(){
    var text = document.getElementById("messageinput").value;
    webSocket.send(text);
}

function closeSocket(){
    webSocket.close();
}

function writeResponse(text){
    messages.innerHTML += "<br/>" + text;
}

