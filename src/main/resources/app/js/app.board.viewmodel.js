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

    function scrambleShips() {
        for(var i = 0; i < appBoardViewModel.ships().length; i++) {
            var id = "#lsquare" + i + "0";
            var ele = $(id);
            switch(appBoardViewModel.ships()[i].shiptype) {
            case "BATTLESHIP":
                ele.append("<div class='even rotation-trans horizontal ship battleship draggable' id='ship" + i +"'</div>");
                break;
            case "CRUISER":
                ele.append("<div class='odd rotation-trans horizontal ship cruiser draggable' id='ship" + i +"'</div>");
                break;
            case "CARRIER":
                ele.append("<div class='odd rotation-trans horizontal ship carrier draggable' id='ship" + i +"'</div>");
                break;
            case "DESTROYER":
                ele.append("<div class='even rotation-trans horizontal ship destroyer draggable' id='ship" + i +"'</div>");
                break;
            case "SUBMARINE":
                ele.append("<div class='odd rotation-trans horizontal ship submarine draggable' id='ship" + i +"'</div>");
                break;
            }
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
