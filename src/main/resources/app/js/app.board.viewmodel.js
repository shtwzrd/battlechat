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

    function buildShipElement(orientation, shiptype, index) {
        var shipType = shiptype.toLowerCase();
        var oddEven = "odd";
        if(shipType == "battleship" || shipType == "destroyer") {
            oddEven = "even";
        }
        return "<div class='" + oddEven + " rotation-trans " + orientation.toLowerCase() + " ship " + shipType + " draggable' id='ship" + index + "' </div>";
    }

    function scrambleShips() {
        for(var i = 0; i < appBoardViewModel.ships().length; i++) {
            var ele = $("#lsquare" + i + 0);
            var html = buildShipElement("horizontal", appBoardViewModel.ships()[i].shiptype, i);
            ele.append(html);
        }
        init = true;
    }

    function clearPlacement() {
        $(".ship").remove();
    }

    appBoardViewModel.mapUpdate = function(msg) {
        appBoardViewModel.myBoard(applyMetaData(msg.boards[msg.yourBoardIndex].cells));
        appBoardViewModel.ships(msg.boards[msg.yourBoardIndex].fleet.ships);
        msg.boards.splice(msg.yourBoardIndex, 1);
        appBoardViewModel.theirBoard(applyMetaData(msg.boards[0].cells));

        if(!init) {
            scrambleShips();
        } else {
            clearPlacement();
            for(var i = 0; i < appBoardViewModel.ships().length; i++) {
                var ship = appBoardViewModel.ships()[i];
                var element = buildShipElement(ship.rotation, ship.shiptype, i);
                $("#lsquare" + ship.y + ship.x).append(element);
                var shipEle = $("#ship" + i)[0];
                if(ship.rotation.toLowerCase() == "vertical") {
                    var translation = (ship.cells.length - 3) * 2.5 + 5;
                    shipEle.style.transform += "rotate(90deg) translate(" + translation + "vh, " + translation + "vh)";
                }
                shipEle.style.position = "relative";
                shipEle.style.marginLeft = "0vh";
                $("#ship" + i).removeClass("draggable");
            }
        }
    }.bind(appBoardViewModel);
}( window.appBoardViewModel = window.appBoardViewModel || {}, jQuery ));

ko.applyBindings(appBoardViewModel, $("#boards")[0]);
