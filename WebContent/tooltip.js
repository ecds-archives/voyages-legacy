var isIE = document.all ? true:false;
if (!isIE) {
	document.captureEvents(Event.MOUSEMOVE);
}

var xcoord = -500;
var ycoord = -500;

document.onmousemove = getMousePosition;


function getMousePosition(e) {
  var _x;
  var _y;
  if (!isIE) {
    _x = e.pageX;
    _y = e.pageY;
  }
  if (isIE) {
    _x = event.clientX + document.body.scrollLeft;
    _y = event.clientY + document.body.scrollTop;
  }
  
  xcoord = _x;
  ycoord = _y;
  
  return true;
}


function showToolTip(id, parentId) {
    it = document.getElementById(id);
    parent = document.getElementById(parentId);
        
    if ((it.style.top == '' || it.style.top == 0) 
        && (it.style.left == '' || it.style.left == 0)) {
        // need to fixate default size (MSIE problem)
        it.style.width = it.offsetWidth + 'px';
        it.style.height = it.offsetHeight + 'px';
        
        //img = document.getElementById(parentId); 
    
        // if tooltip is too wide, shift left to be within parent 
        //if (posX + it.offsetWidth > img.offsetWidth) posX = img.offsetWidth - it.offsetWidth;
        //if (posX < 0 ) posX = 0; 
        
        x = xcoord - 200;//xstooltip_findPosX(img) + posX;
        y = ycoord;//xstooltip_findPosY(img) + posY;
		
		it.style.left = x + 'px'; 
		
		/*
		maxx = 0;
		if (isIE) {
			maxx = document.body.offsetWidth;
		} else {
			maxx = window.innerWidth;
		}
        if (x > maxx) {
        	it.style.left = (x - it.style.width) + 'px';
        } else {
        	it.style.left = (x - it.style.width) + 'px';
        }*/
        
        it.style.top = y + 'px';
    }
    
    it.style.visibility = 'visible'; 
}

function hideToolTip(id) {
    it = document.getElementById(id); 
    it.style.visibility = 'hidden'; 
}
