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

function openPopup(pageUrl)
{
	window.open(
		pageUrl,
		"tastPopupHelp",
		"resizable=yes, " +
		"location=no, " +
		"status=no, " +
		"scrollbars=yes, " +
		"width=680, " +
		"height=680").focus();
}
