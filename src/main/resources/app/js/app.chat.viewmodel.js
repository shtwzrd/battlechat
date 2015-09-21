(function( appChatViewModel, $, undefined ) {

    appChatViewModel.messages = ko.observableArray([]);
    appChatViewModel.messageToAdd = ko.observable("");
    appChatViewModel.sendMessage = function() {
        if (appChatViewModel.messageToAdd() != "") {
            var msg = {
                messageType: "CHAT",
                id: appSocket.id,
                body: appChatViewModel.messageToAdd
            };
            appSocket.send(msg);
            // this.messages.push(this.messageToAdd());
            appChatViewModel.messageToAdd("");
        }
    }.bind(appChatViewModel);  // Ensure that "this" is always this view model
}( window.appChatViewModel = window.appChatViewModel || {}, jQuery ));

ko.applyBindings(appChatViewModel);
