(function( appSocket, $, undefined ) {
    // Private property

    var webSocket;
    var domain = $(this.document).attr("baseURI").split('/')[2];
    var protocol = domain.startsWith('localhost') ? "ws://" : "wss://";

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
        webSocket = new WebSocket(protocol + domain + "/session", ["v1", appSocket.id]);

        // Binds functions to the listeners for the websocket.
        webSocket.onopen = function(event){
            console.log("Connected.")
            if(event.data === undefined)
                return;
        };

        webSocket.onmessage = function(event){
            routeMessage(JSON.parse(event.data));
            console.log(event.data);
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

    function routeMessage(msg) {
        switch(msg.messageType) {
        case "CHAT":
            writeChatMessage(msg.body);
            break;
        case "STATUS":
            writePhaseData(msg.body);
            break;
        case "CONFIGURATION":
            appConfigurationViewModel.loadConfig(msg.body);
            break;
        }
    };

    function writeChatMessage(text) {
        appChatViewModel.messages.push(text);
    };

    function writePhaseData(msg) {
        appGameStateViewModel.phase(msg.gamePhase);
        appGameStateViewModel.timeToPhaseChange(msg.secondsToPhaseChange);
    };

}( window.appSocket = window.appSocket || {}, jQuery ));
