(function( appBoardViewModel, $, undefined ) {
    appBoardViewModel.boards = ko.observableArray();
}( window.appBoardViewModel = window.appBoardViewModel || {}, jQuery ));

ko.applyBindings(appBoardViewModel, $("#boards")[0]);
