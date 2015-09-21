(function( appSocket, $, undefined ) {
    // Private property

    var webSocket;
    var domain = $(this.document).attr("baseURI").split('/')[2];

    // Public property

    appSocket.id = $(this.document).attr("URL").split('/').pop();

    // Public functions

    appSocket.openSocket = function () {
        // Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
            console.log("WebSocket is already opened.");
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket("ws://" + domain + "/session", ["v1", appSocket.id]);

        // Binds functions to the listeners for the websocket.
        webSocket.onopen = function(event){
            console.log("Connected.")
            if(event.data === undefined)
                return;

            console.log(event.data);
        };

        webSocket.onmessage = function(event){
            writeResponse(JSON.parse(event.data).body);
        };

        webSocket.onclose = function(event){
            console.log("Connection closed");
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
        appChatViewModel.messages.push(text);
    };

}( window.appSocket = window.appSocket || {}, jQuery ));
