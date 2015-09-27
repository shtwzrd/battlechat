(function( appBoardViewModel, $, undefined ) {
    var init = false;

    appBoardViewModel.myBoard = ko.observableArray();
    appBoardViewModel.theirBoard = ko.observableArray();
    appBoardViewModel.ships = ko.observableArray();

    function applyMetaData(board) {
        for(var i = 0; i < board.length; i++) {
            for(var j = 0; j <board[0].length; j++) {
                board[i][j] = {"r": i, "c": j, "state": board[i][j]};
            }
        }
        return board;
    }

    function placeShip(x, y, orientation, shiptype, index) {
        var ele = $("#lsquare" + y + x);
        var occupied = $("#lsquare" + y + x).children().length > 0;
        if(!occupied) {
            var shipType = shiptype.toLowerCase();
            var oddEven = "odd";
            if(shipType == "battleship" || shipType == "destroyer") {
                oddEven = "even";
            }
            var html = "<div class='" + oddEven + " rotation-trans " + orientation + " ship " + shipType + " draggable' id='ship" + index + "' </div>";
            ele.append(html);
        }
    }

    function scrambleShips() {
        for(var i = 0; i < appBoardViewModel.ships().length; i++) {
                placeShip(0, i, "horizontal", appBoardViewModel.ships()[i].shiptype, i);
        }
        init = true;
    }

    appBoardViewModel.mapUpdate = function(msg) {
        appBoardViewModel.myBoard(applyMetaData(msg.boards[msg.yourBoardIndex].cells));
        appBoardViewModel.ships(msg.boards[msg.yourBoardIndex].fleet.ships);
        msg.boards.splice(msg.yourBoardIndex, 1);
        (appBoardViewModel).theirBoard(applyMetaData(msg.boards[0].cells));

        if(!init) {
            scrambleShips();
        }
    }.bind(appBoardViewModel);
}( window.appBoardViewModel = window.appBoardViewModel || {}, jQuery ));

ko.applyBindings(appBoardViewModel, $("#boards")[0]);
