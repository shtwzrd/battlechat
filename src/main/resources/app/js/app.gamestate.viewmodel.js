(function( appGameStateViewModel, $, undefined ) {

    appGameStateViewModel.timeToPhaseChange = ko.observable("");
    appGameStateViewModel.phase = ko.observable("");

}( window.appGameStateViewModel = window.appGameStateViewModel || {}, jQuery ));

ko.applyBindings(appGameStateViewModel, $("#phase-data")[0]);
