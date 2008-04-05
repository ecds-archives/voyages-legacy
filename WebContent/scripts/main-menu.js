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

	function openFAQ()
	{
		openPopup("./help/faq.faces");
	}

	function openDemos()
	{
		openPopup("./help/demos.faces");
	}

	function openSitemap()
	{
		openPopup("./help/sitemap.faces");
	}

	function openGlossary()
	{
		openPopup("./help/glossary.faces");
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
			"width=600, " +
			"height=400"); 
	}
	
