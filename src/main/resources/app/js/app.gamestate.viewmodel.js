(function( appGameStateViewModel, $, undefined ) {

    appGameStateViewModel.timeToPhaseChange = ko.observable("∞");
    appGameStateViewModel.phase = ko.observable("Waiting for a Challenger");

    appGameStateViewModel.handlePhaseData = function (msg) {
        appGameStateViewModel.phase(msg.gamePhase);
        appGameStateViewModel.timeToPhaseChange(msg.secondsToPhaseChange);

        switch(msg.gamePhase) {
        case "NOT_STARTED":
            appGameStateViewModel.phase("Connecting to Challenger...");
            break;
        case "PLACEMENT_PHASE":
            swipeLeft();
            appGameStateViewModel.phase("Scramble your fleet!");
            break;
        case "YOU_FIRING":
            swipeRight();
            appGameStateViewModel.phase("Fire!");
            break;
        case "OPPONENT_FIRING":
            swipeLeft();
            appGameStateViewModel.phase("Brace for impact!");
            break;
        }
    }.bind(appGameStateViewModel);

}( window.appGameStateViewModel = window.appGameStateViewModel || {}, jQuery ));

ko.applyBindings(appGameStateViewModel, $("#phase")[0]);
