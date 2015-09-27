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
	
	function getShipsState(numberOfBoat) {
		var shipState = [];
		for(var i=0; i<numberOfBoat; i++){
			var ship = document.getElementById('ship'+(i+1));
			var firstOccupiedCell = document.getElementsByClassName('byship'+(i+1))[0];
			
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
			
			var array = [x,y,orientation,shipType];
			shipState.push(array);
		}
		return shipState;
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
