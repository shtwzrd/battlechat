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
        webSocket = new WebSocket(protocol + domain + "/session");

        // Binds functions to the listeners for the websocket.
        webSocket.onopen = function(event){
            console.log("Connected.");
            appChatViewModel.handleMessage("Connected.", "EVENT");
            if(event.data === undefined)
                return;
        };

        webSocket.onmessage = function(event){
            routeMessage(JSON.parse(event.data));
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
        case "REGISTRATION":
            handshake(msg.body);
            break;
        case "CHAT":
            appChatViewModel.handleMessage(msg.body, msg.messageType);
            break;
        case "EVENT":
            appChatViewModel.handleMessage(msg.body, msg.messageType);
            break;
        case "ERROR":
            appChatViewModel.handleMessage(msg.body, msg.messageType);
            break;
        case "STATUS":
            appGameStateViewModel.handlePhaseData(msg.body);
            break;
        case "CONFIGURATION":
            appConfigurationViewModel.loadConfig(msg.body);
            break;
        case "UPDATE":
            appBoardViewModel.mapUpdate(msg.body);
            break;
        case "HIT":
            appBoardViewModel.handleShotNotification(msg.body, msg.messageType);
            break;
        case "MISS":
            appBoardViewModel.handleShotNotification(msg.body, msg.messageType);
            break;
        }
    };

    function handshake(msg) {
        appSocket.send({ id: appSocket.id, messageType: "REGISTRATION", body: msg });
    }

}( window.appSocket = window.appSocket || {}, jQuery ));
