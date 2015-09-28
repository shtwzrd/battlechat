(function( appGameStateViewModel, $, undefined ) {

    appGameStateViewModel.timeToPhaseChange = ko.observable("∞");
    appGameStateViewModel.phase = ko.observable("Waiting for a Challenger");
    appGameStateViewModel.phaseEnum = ko.observable("NOT_STARTED");

    var lastPhase = "";

    function isPhaseChanged(phase) {
        if(phase == "PLACEMENT_PHASE" && !appBoardViewModel.canPlaceShips()) {
            appBoardViewModel.canPlaceShips(true);
        } else if (phase != "PLACEMENT_PHASE") {
            appBoardViewModel.canPlaceShips(false);
        }
        return lastPhase != phase;
    }

    appGameStateViewModel.handlePhaseData = function (msg) {
        appGameStateViewModel.phaseEnum(msg.gamePhase);
        appGameStateViewModel.timeToPhaseChange(msg.secondsToPhaseChange);
        if(msg.gamePhase == "YOU_WIN" || msg.gamePhae == "YOU_LOSE") {
            console.log("It's OVER!");
        }

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
                appBoardViewModel.canFire(true);
            }
            appGameStateViewModel.phase("Fire!");
            break;
        case "OPPONENT_FIRING":
            if(isPhaseChanged(msg.gamePhase)) {
                swipeLeft();
                appBoardViewModel.canFire(false);
            }
            appGameStateViewModel.phase("Brace for impact!");
            break;
        case "YOU_LOSE":
            appGameStateViewModel.phase("You've been defeated.");
            break;
        case "YOU_WIN":
            appGameStateViewModel.phase("You are victorious!");
            break;
        }
        lastPhase = msg.gamePhase;
    }.bind(appGameStateViewModel);
}( window.appGameStateViewModel = window.appGameStateViewModel || {}, jQuery ));

ko.applyBindings(appGameStateViewModel, $("#phase")[0]);
