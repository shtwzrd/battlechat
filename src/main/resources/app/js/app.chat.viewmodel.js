(function( appChatViewModel, $, undefined ) {

    appChatViewModel.messages = ko.observableArray();
    appChatViewModel.messageToAdd = ko.observable("");
    appChatViewModel.sendMessage = function() {
        if (appChatViewModel.messageToAdd() != "") {
            var msg = {
                messageType: "CHAT",
                id: appSocket.id,
                body: appChatViewModel.messageToAdd()
            };
            appSocket.send(msg);
            appChatViewModel.messageToAdd("");
        }
    }.bind(appChatViewModel);
}( window.appChatViewModel = window.appChatViewModel || {}, jQuery ));

ko.applyBindings(appChatViewModel, $("#chat")[0]);

//autoscroll down
var textarea = document.getElementById('chattextarea');
textarea.scrollTop = textarea.scrollHeight;
