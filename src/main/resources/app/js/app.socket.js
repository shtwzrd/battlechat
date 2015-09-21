(function( appSocket, $, undefined ) {
    // Private property

    var webSocket;
    var messages = document.getElementById("messages");
    var domain = $(this.document).attr("baseURI").split('/')[2];

    // Public property

    appSocket.id = $(this.document).attr("URL").split('/').pop();

    // Public functions

    appSocket.openSocket = function () {
        // Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
            writeResponse("WebSocket is already opened.");
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket("ws://" + domain + "/session", ["v1", appSocket.id]);

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
    };

    // Sends the value of the text input to the server
    appSocket.send = function (msg){
        webSocket.send(JSON.stringify(msg));
    };

    appSocket.closeSocket = function (){
        webSocket.close();
    };

    // Private function

    function writeResponse(text){
        messages.innerHTML += "<br/>" + text;
    };

}( window.appSocket = window.appSocket || {}, jQuery ));
