(function( appConfigurationViewModel, $, undefined ) {

    appConfigurationViewModel.gridHeight = ko.observable("");
    appConfigurationViewModel.gridWidth = ko.observable("");
    appConfigurationViewModel.players = ko.observable("");
    appConfigurationViewModel.cruisers = ko.observable("");
    appConfigurationViewModel.carriers = ko.observable("");
    appConfigurationViewModel.destroyers = ko.observable("");
    appConfigurationViewModel.submarines = ko.observable("");
    appConfigurationViewModel.canoes = ko.observable("");

    appConfigurationViewModel.loadConfig = function(cfg) {
        appConfigurationViewModel.gridHeight(cfg.settings.GRID_HEIGHT);
        appConfigurationViewModel.gridWidth(cfg.settings.GRID_WIDTH);
        appConfigurationViewModel.players(cfg.settings.PLAYER_COUNT);
        appConfigurationViewModel.cruisers(cfg.settings.CRUISER_COUNT);
        appConfigurationViewModel.carriers(cfg.settings.CARRIER_COUNT);
        appConfigurationViewModel.destroyers(cfg.settings.DESTROYER_COUNT);
        appConfigurationViewModel.submarines(cfg.settings.SUBMARINE_COUNT);
        appConfigurationViewModel.canoes(cfg.settings.CANOE_COUNT);
    }.bind(appConfigurationViewModel);

}( window.appConfigurationViewModel = window.appConfigurationViewModel || {}, jQuery ));

ko.applyBindings(appConfigurationViewModel, $("#gameconfig")[0]);