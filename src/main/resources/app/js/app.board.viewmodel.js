(function( appBoardViewModel, $, undefined ) {
    var init = false;
    var boardIndex = 0;

    appBoardViewModel.myBoard = ko.observableArray();
    appBoardViewModel.theirBoard = ko.observableArray();
    appBoardViewModel.ships = ko.observableArray();
    appBoardViewModel.shipPlacedCount = ko.observable();
    appBoardViewModel.canPlaceShips = ko.observable(false);
    appBoardViewModel.canFire = ko.observable(false);
    appBoardViewModel.placementSent = ko.observable(false);

    appBoardViewModel.shipsPlaced = ko.computed(function() {
        return appBoardViewModel.ships().length == appBoardViewModel.shipPlacedCount();
    });


    appBoardViewModel.enablePlacementButton = ko.computed(function() {
        return !appBoardViewModel.placementSent() && appBoardViewModel.shipsPlaced();
    });

    appBoardViewModel.handleShotNotification = function(msg, status) {
        var prefix;
        if(msg.boardIndex == boardIndex) {
            prefix = "#lsquare";
        }  else {
            prefix = "#rsquare";
        }
        var style = status == "HIT" ? "firesquare" : "splashsquare";

        $(prefix + msg.Y + msg.X).append("<div class='wrapper'><div class='" + style + "' </div></div>");
    };

    function transposeBoard(board, boardLength) {
        var boardOut = [];
        for(var i = 0; i < board.length; i++){
            boardOut.push([]);
        };

        for(var i = 0; i < board.length; i++){
            for(var j = 0; j < boardLength; j++){
                boardOut[j].push({c: i, r: j, state:board[i][j]});
            };
        };

        for(var i = 0; i < boardOut.length; i++) {
            if (boardOut.length == 0) {
                boardOut.splice(i, 1);
            }
        }
        return (boardOut);
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

    appBoardViewModel.sendFleetPlacement = function() {
        var numberOfBoat = appBoardViewModel.ships().length;
        var shipState = [];
        for(var i=0; i<numberOfBoat; i++){
            var ship = document.getElementById('ship' + i);
            var firstOccupiedCell = document.getElementsByClassName('byship'+ i)[0];

            //Position
            var x = firstOccupiedCell.id.charAt(firstOccupiedCell.id.length-1);
            var y = firstOccupiedCell.id.charAt(firstOccupiedCell.id.length-2);

            //orientation
            var orientation='';
            if (ship.classList.contains('horizontal')) orientation = 'horizontal';
            else if (ship.classList.contains('vertical')) orientation = 'vertical';

            //ShipType
            var shipType='';
            if (ship.classList.contains('cruiser')) shipType = 'cruiser';
            else if (ship.classList.contains('submarine')) shipType = 'submarine';
            else if (ship.classList.contains('battleship')) shipType = 'battleship';
            else if (ship.classList.contains('destroyer')) shipType = 'destroyer';
            else if (ship.classList.contains('carrier')) shipType = 'carrier';

            var s = {
                "x": x,
                "y": y,
                "rotation": orientation.toUpperCase(),
                "shiptype": shipType.toUpperCase()
            };
            shipState.push(s);
        }
        var msg = {
            messageType: "PLACEMENT",
            id: appSocket.id,
            body: { "ships": shipState }
        };
        appSocket.send(msg);
        appBoardViewModel.placementSent(true);
        return shipState;
    };

    function clearPlacement() {
        $(".ship").remove();
    }

    appBoardViewModel.shootMe = function(me) {
        var b = boardIndex == 0 ? 1 : 0;

        var msg = {
            messageType: "FIRE",
            id: appSocket.id,
            body: [{
                boardIndex: b,
                X: me.c,
                Y: me.r
            }]};
        appBoardViewModel.canFire(false);
        appSocket.send(msg);
    };

    appBoardViewModel.mapUpdate = function(msg) {
        boardIndex = msg.yourBoardIndex;
        appBoardViewModel.myBoard(transposeBoard(msg.boards[boardIndex].cells, msg.boards[boardIndex].cells[0].length));
        appBoardViewModel.ships(msg.boards[boardIndex].fleet.ships);
        msg.boards.splice(boardIndex, 1);
        appBoardViewModel.theirBoard(transposeBoard(msg.boards[0].cells, msg.boards[0].cells[0].length));

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
