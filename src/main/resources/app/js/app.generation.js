// HTML GENERATION
var gridx = 8, gridy = 8;
var Ships = [["Minesweeper",2,4],["Frigate",3,4],[ "Cruiser",4,2],[ "Battleship",5,1]];

var player1Grid = [[]];
var player2Grid = [[]];
var player1Ships = [];

document.writeln("<div id=\"animated-background\" ></div>");
document.writeln("<p id=\"phase\" > <span data-bind='text: appGameStateViewModel.timeToPhaseChange'></span> : <span data-bind='text: appGameStateViewModel.phase'></span> </p>");

// left panel 
document.writeln("<div id='left' class='active'>");

//player 1 table
document.writeln("<div id='tableleft' class='table'>");
for (i=0; i<(gridx); i++)
{
	document.writeln("<div id='lrow" + i + "' class='row'>");
		for (j=0; j<gridy; j++)
		{
		document.writeln("<div id='lsquare" + i+''+j + "' class='square'>");
		document.writeln("</div>");
		}
	document.writeln("</div>");
}
document.writeln("</div></div>");

document.writeln("<div id='right' style='right:-30%;'>");
// player 2 table
document.writeln("<div id='tableright' class='table'>");
for (i=0; i<(gridx); i++)
{
	document.writeln("<div id='rrow" + i + "' class='row'>");
		for (j=0; j<gridy; j++)
		{
		document.writeln("<div id='rsquare" + i +''+ j + "' class='square'>");
		document.writeln("</div>");
		}
	document.writeln("</div>");
}
document.writeln("</div></div>");

document.writeln("<div id=\"rightarrow\">-></div>");
document.writeln("<div id=\"leftarrow\"><-</div>");


//peudo random ship placement
var div1 = document.getElementById('lsquare13');
div1.innerHTML = div1.innerHTML + "<div class ='odd rotation-trans horizontal ship cruiser draggable' id='ship1'></div>";
//peudo random ship placement
var div2 = document.getElementById('lsquare23');
div2.innerHTML = div2.innerHTML + "<div class ='odd rotation-trans horizontal ship carrier draggable' id='ship2'></div>";
//peudo random ship placement
var div3 = document.getElementById('lsquare33');
div3.innerHTML = div3.innerHTML + "<div class ='even rotation-trans horizontal ship battleship draggable' id='ship3'></div>";
//peudo random ship placement
var div4 = document.getElementById('lsquare43');
div4.innerHTML = div4.innerHTML + "<div class ='odd rotation-trans horizontal ship submarine draggable' id='ship4'></div>";
//peudo random ship placement
var div5 = document.getElementById('lsquare63');
div5.innerHTML = div5.innerHTML + "<div class ='even rotation-trans horizontal ship destroyer draggable' id='ship5'></div>";



document.writeln("<div id='chat'>");
document.writeln("<form data-bind='submit: sendMessage'>");
document.writeln("<input id='chatinput' name='chatinput' form='chat' data-bind='value: appChatViewModel.messageToAdd, valueUpdate: 'afterkeydown''>");
document.writeln("<button type='submit' id='chatbutton' data-bind='enable: appChatViewModel.messageToAdd().length > 0'>Send</button>");
document.writeln("</form>");
document.writeln("<div id='chattextarea' name='chattextarea' data-bind='foreach: appChatViewModel.messages'></div>");
document.writeln("</div>");// JavaScript Document