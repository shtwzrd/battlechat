
//some function
function vWidth(clientwidth){
	var windowwidth = window.innerWidth;
	var width = (clientwidth*100)/windowwidth;
	return width; 
}

function vHeight(clientheight){
	var windowheight = window.innerHeight;
	var height = (clientheight*100)/windowheight;
	return height; 
}

//swipe + arrowkeys	
$(document).ready(function() {
	
	function swipeRight() 
	{
		jQuery('#left').animate({
				width:0,
				'left': '-30%',
				opacity:0
			},
			100
		).css('right','');
		jQuery('#right').animate({
				width:'100%',
				'left': '0%',
				opacity:1.0
			},
			90
		).css('right','');
		jQuery('#right').toggleClass('active',true);
		jQuery('#left').toggleClass('active',false);
		jQuery('#rightarrow').hide();
		jQuery('#leftarrow').show();
		
	};
	function swipeLeft()
	{

		jQuery('#right').animate({
				width:0,
				'right': '-30%',
				opacity:0
			},
			100
		).css('left','');
		jQuery('#left').animate({
				width:'100%',
				'right': '0%',
				opacity:1.0
			},
			90	
		).css('left','');
		jQuery('#left').toggleClass('active',true);
		jQuery('#right').toggleClass('active',false);
		jQuery('#leftarrow').hide();
		jQuery('#rightarrow').show();
		
	};
	
	//Arrow management
	$('#rightarrow').on('click', function(e){
		swipeRight();
	});
	$('#leftarrow').on('click', function(e){
		swipeLeft();
	});
	//Keyboard navigation
	$("body").keydown(function( event ) {
  		if ( event.keyCode == 39 && $('#left').hasClass('active') ){
			swipeRight();		
  		}
		else if ( event.keyCode == 37 && $('#right').hasClass('active') ) {
			swipeLeft();		
  		}
	});
});



//resize management
window.onresize = function(event) {
    var ships = document.getElementsByClassName('ship');
	for (var i=0;i<ships.length;i++){
	ships[i].style.display='none';
	ships[i].offsetHeight; // no need to store this anywhere, the reference is enough
	ships[i].style.display='block';
	}
};


//Drag and drop stuff
function dragMoveListener (event) {
    var target = event.target,
        // keep the dragged position in the data-x/data-y attributes
        x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
        y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
				
    // translate the element
    target.style.webkitTransform =
    target.style.transform =
      'translate(' + vWidth(x) + 'vw, ' + vHeight(y) + 'vh) ';
		
		if(target.classList.contains('horizontal')){
			target.style.transform += 'rotate(-0deg)';
		} else 	if(target.classList.contains('vertical')){
			target.style.transform += 'rotate(-90deg)';
		}

    // update the posiion attributes
    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
}  

function removeOccupiedDivsBy (targetId) {
		var occupieddivs = document.querySelectorAll(".by"+targetId);
		var l = occupieddivs.length;
		for (var i = 0; i < l; i++) {
			occupieddivs[i].classList.remove("occupied");
			occupieddivs[i].classList.remove("by"+targetId);
			console.log(occupieddivs[i]);
		}
}

function removeDroppableFeedback () {
		var targetdivs = document.querySelectorAll(".drop-target");
		var l = targetdivs.length;
		for (var i = 0; i < l; i++) {
			targetdivs[i].classList.remove("drop-target");
			console.log(targetdivs[i]);
		}
}



var startPos = {x: 0, y: 0};

interact('.draggable').draggable({
    // enable inertial throwing
  		inertia: true,
    // keep the element within the area of it's parent
  		restrict: {
      		restriction: ('#tableleft'),
      		endOnly: true,
	        elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
    	},
		// call this function on every dragmove event
		onmove: dragMoveListener,
		onstart: function (event) {
		  removeOccupiedDivsBy(event.target.id);
		  event.target.classList.remove('dropped');
		  var rect = interact.getElementRect(event.target);
		  
		  startPos.x = rect.left + rect.width  / 2;
    	  startPos.y = rect.top  + rect.height / 2;
		},
		// call this function on every dragend event
		onend: function (event) {
			console.log(event.target.dropped);
            if (event.target.classList.contains('dropped')) {
                // snap to the start position
                //event.target.snap();
            }
		}
	})
	.snap({
		  mode: 'anchor',
		  anchors: [],
		  range: Infinity,
		  elementOrigin: { x: 0.5, y: 0.5 },
		  endOnly: true
	})
	//Doubleclick change direction
	.on('doubletap', function (event) {
		if(event.currentTarget.classList.contains('horizontal')){
			event.currentTarget.classList.add('vertical');
			event.currentTarget.classList.remove('horizontal');
		} else 	if(event.currentTarget.classList.contains('vertical')){
			event.currentTarget.classList.add('horizontal');
			event.currentTarget.classList.remove('vertical');
		}
		var re = /-?\d+(\.\d+)?/g; 
		var trans = event.currentTarget.style.transform;
		
		if(trans.indexOf('rotate') >= -1) event.currentTarget.style.transform+=' rotate(-90deg)';
		else {
			
			var tval = trans.match(re);
			var degree = parseInt(tval[tval.length-1]);
			//console.log(degree)
			if(degree != -360){
				degree=degree-90;
			} else degree = 0;
			
			event.currentTarget.style.transform.replace(/rotate\(-?\d+(\.\d+)?deg\)/g, 'rotate('+degree+'deg)');
			//console.log(event.currentTarget.style.transform);
			
		}
		removeOccupiedDivsBy(event.currentTarget.id);
		onstart;

	});
	
interact('.square').dropzone({
			accept : ".draggable",
			overlap: 'center',
			
			  // listen for drop related events:

			  ondropactivate: function (event) {
				// add active dropzone feedback
				event.target.classList.add('drop-active');
			  },
			  ondragenter: function (event) {
				var draggableElement = event.relatedTarget,
					dropzoneElement = event.target;
				var length = Math.round(document.getElementById(draggableElement.id).offsetWidth/document.getElementById(dropzoneElement.id).offsetWidth);
				var row= dropzoneElement.id.charAt(dropzoneElement.id.length-2);
				var column = dropzoneElement.id.charAt(dropzoneElement.id.length-1);
				
				var dropRect = interact.getElementRect(dropzoneElement);
				var dropLeft =0;
				
					if ($('#'+draggableElement.id).hasClass('odd')){
						dropLeft = {
						  x: dropRect.left + dropRect.width/2,
						  y:dropRect.top + dropRect.height/2
						};
						
					} else 	if ($('#'+draggableElement.id).hasClass('even')){
						if($('#'+draggableElement.id).hasClass('horizontal')){
							dropLeft = {
							  x: dropRect.left + dropRect.width,
							  y: dropRect.top + dropRect.height/2
							};
						} else if($('#'+draggableElement.id).hasClass('vertical')){
							dropLeft = {
							  x: dropRect.left + dropRect.width/2,
							  y: dropRect.top + dropRect.height
							};
						}
					}
					console.log(dropLeft);
					
				
				// feedback the possibility of a drop
				draggableElement.classList.add('can-drop');
			
				event.draggable.snap({
				  anchors: [ dropLeft ]
				});
			  },
			  ondragleave: function (event) {
				// remove the drop feedback style
				removeDroppableFeedback();
				event.relatedTarget.classList.remove('can-drop');
				//event.draggable.snap(false);
			  },
			  ondrop: function (event) {
				  var ship = event.relatedTarget;
				  var dropzone = event.target;
				  
				$('#'+ship.id).css('left',0).appendTo($('#'+dropzone.id));
				
				// notify user with occupied cells
				if ($('#'+ship.id).hasClass('horizontal')){
					var length = Math.round(document.getElementById(ship.id).offsetWidth/document.getElementById(dropzone.id).offsetWidth);
					var row= dropzone.id.charAt(dropzone.id.length-2);
					
					if ($('#'+ship.id).hasClass('odd')){
						var middledivnum = parseInt(dropzone.id.slice(-1));
						var divoneachsidenum = (length-1)/2;
						console.log(divoneachsidenum + ' : divs on each side');
						
						var startingdivnum = middledivnum - divoneachsidenum;
						console.log(startingdivnum + ' : starting div num')
						
						var endingdivnum = middledivnum + divoneachsidenum;
						console.log(endingdivnum + ' : ending div num')
						
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+row+i);
							if(d.classList.contains('occupied')) return false;
						}	
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+row+i);
							d.classList.add('occupied');
							d.classList.add('by'+ship.id);
							//console.log('lsquare'+row+i);
						}
					}
						if ($('#'+ship.id).hasClass('even')){
							
						//console.log('boat middle: ' + ((document.getElementById(ship.id).getBoundingClientRect().left+document.getElementById(ship.id).getBoundingClientRect().right)/2));
						//console.log('cell right: ' +document.getElementById(dropzone.id).getBoundingClientRect().right);
						//console.log('cell left: ' +document.getElementById(dropzone.id).getBoundingClientRect().left);

						
						var boatcenterx = (document.getElementById(ship.id).getBoundingClientRect().left+document.getElementById(ship.id).getBoundingClientRect().right)/2;
						var leftcellbound = document.getElementById(dropzone.id).getBoundingClientRect().left;
						var rightcellbound = document.getElementById(dropzone.id).getBoundingClientRect().right;
						
						if ( Math.round(boatcenterx) > (Math.round(rightcellbound)-2) && Math.round(boatcenterx) < (Math.round(rightcellbound)+2) ){
							var divsonrightside = ((length)/2);
							var divsonleftside = divsonrightside-1;	
						} else if ( Math.round(boatcenterx) > (Math.round(leftcellbound)-2) && Math.round(boatcenterx) < (Math.round(leftcellbound)+2)){
							var divsonleftside = ((length)/2);
							var divsonrightside = divsonleftside-1;	
						}
						
						var middledivnum = parseInt(dropzone.id.slice(-1));
						//console.log(divsonrightside + ' : divs on right side');
						//console.log(divsonleftside + ' : divs on left side');
						
						var startingdivnum = middledivnum - divsonleftside;
						//console.log(startingdivnum + ' : starting div num');
						
						var endingdivnum = middledivnum + divsonrightside;
						//console.log(endingdivnum + 'ending div num')
						
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+row+i);
							if(d.classList.contains('occupied')) return false;
						}
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+row+i);
							d.classList.add('occupied');
							d.classList.add('by'+ship.id);
							//console.log('lsquare'+row+i);
						}
					}
				}	else if ($('#'+ship.id).hasClass('vertical')){
					var length = Math.round(document.getElementById(ship.id).offsetWidth/document.getElementById(dropzone.id).offsetWidth);
					var column= dropzone.id.slice(-1);
					
					if ($('#'+ship.id).hasClass('odd')){
						var middledivnum = parseInt(dropzone.id.charAt(dropzone.id.length-2));
						var divoneachsidenum = (length-1)/2;
						//console.log(divoneachsidenum + ' : divs on each side');
						
						var startingdivnum = middledivnum - divoneachsidenum;
						//console.log(startingdivnum + ' : starting div num')
						
						var endingdivnum = middledivnum + divoneachsidenum;
						//console.log(endingdivnum + 'ending div num')
						
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+i+column);
							if(d.classList.contains('occupied')) return false;
						}
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+i+column);
							d.classList.add('occupied');
							d.classList.add('by'+ship.id);
							//console.log('lsquare'+i+column);
						}
					}
					
					
						if ($('#'+ship.id).hasClass('even')){
							
						//console.log('boat middle: ' + ((document.getElementById(ship.id).getBoundingClientRect().left+document.getElementById(ship.id).getBoundingClientRect().right)/2));
						//console.log('cell right: ' +document.getElementById(dropzone.id).getBoundingClientRect().right);
						//console.log('cell left: ' +document.getElementById(dropzone.id).getBoundingClientRect().left);
						
						var boatcentery = (document.getElementById(ship.id).getBoundingClientRect().top+document.getElementById(ship.id).getBoundingClientRect().bottom)/2;
						//console.log('boat center ' + boatcentery);
						var topcellbound = document.getElementById(dropzone.id).getBoundingClientRect().top;
						//console.log('top cell bound ' + topcellbound);
						var bottomcellbound = document.getElementById(dropzone.id).getBoundingClientRect().bottom;
						//console.log('bottom cell bound ' + bottomcellbound);
						
						if ( Math.round(boatcentery) > (Math.round(topcellbound)-2) && Math.round(boatcentery) < (Math.round(topcellbound)+2) ){
							var divsontop = ((length)/2);
							var divsonbottom = divsontop-1;	
						} else if ( Math.round(boatcentery) > (Math.round(bottomcellbound)-2) && Math.round(boatcentery) < (Math.round(bottomcellbound)+2)){
							var divsonbottom = ((length)/2);
							var divsontop = divsonbottom-1;	
						}
						
						var middledivnum = parseInt(dropzone.id.charAt(dropzone.id.length-2));
						//console.log('middle div : ',middledivnum)
						//console.log(divsonrightside + ' : divs on right side');
						//console.log(divsonleftside + ' : divs on left side');
						
						var startingdivnum = middledivnum - divsontop;
						//console.log(startingdivnum + ' : starting div num');
						
						var endingdivnum = middledivnum + divsonbottom;
						//console.log(endingdivnum + 'ending div num')
						
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+i+column);
							if(d.classList.contains('occupied')) return false;
						}
						for(i=startingdivnum; i<endingdivnum+1; i++){
							var d = document.getElementById('lsquare'+i+column);
							d.classList.add('occupied');
							d.classList.add('by'+ship.id);
							//console.log('lsquare'+row+i);
						}
					}
				}
				ship.classList.add('dropped');
				
				
			  },
			  ondropdeactivate: function (event) {
				// remove active dropzone feedback
				//removeDroppableFeedback();
			  }
	});