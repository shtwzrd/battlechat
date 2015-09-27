(function( appGameStateViewModel, $, undefined ) {

    appGameStateViewModel.timeToPhaseChange = ko.observable("∞");
    appGameStateViewModel.phase = ko.observable("Waiting for a Challenger");

    var lastPhase = "";

    function isPhaseChanged(phase) {
        console.log(phase);
        console.log(lastPhase);
        console.log(lastPhase != phase);
        return lastPhase != phase;
    }

    appGameStateViewModel.handlePhaseData = function (msg) {
        appGameStateViewModel.phase(msg.gamePhase);
        appGameStateViewModel.timeToPhaseChange(msg.secondsToPhaseChange);

        switch(msg.gamePhase) {
        case "NOT_STARTED":
            appGameStateViewModel.phase("Connecting to Challenger...");
            break;
        case "PLACEMENT_PHASE":
            if(isPhaseChanged(msg.gamePhase)) {
                swipeLeft();
            }
            appGameStateViewModel.phase("Scramble your fleet!");
            break;
        case "YOU_FIRING":
            if(isPhaseChanged(msg.gamePhase)) {
                swipeRight();
            }
            appGameStateViewModel.phase("Fire!");
            break;
        case "OPPONENT_FIRING":
            if(isPhaseChanged(msg.gamePhase)) {
                swipeLeft();
            }
            appGameStateViewModel.phase("Brace for impact!");
            break;
        }
        lastPhase = msg.gamePhase;
    }.bind(appGameStateViewModel);
}( window.appGameStateViewModel = window.appGameStateViewModel || {}, jQuery ));

ko.applyBindings(appGameStateViewModel, $("#phase")[0]);
