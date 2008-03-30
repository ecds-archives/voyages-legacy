var MainMenuBar = 
{

	mouseOver: function(imgElementId, popupBoxId, imgUrl)
	{
	
		var imgElement = document.getElementById(imgElementId);
		var popupBox = document.getElementById(popupBoxId);
		
		imgElement.src = imgUrl;
		popupBox.style.display = "";
		
	},

	mouseOut: function(imgElementId, popupBoxId, imgUrl)
	{
	
		var imgElement = document.getElementById(imgElementId);
		var popupBox = document.getElementById(popupBoxId);
		
		imgElement.src = imgUrl;
		popupBox.style.display = "none";
	
	}

}