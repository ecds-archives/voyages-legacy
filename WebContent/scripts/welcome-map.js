var WelcomeMapGlobals = 
{

	showPlace: function(welcomeMapId, textElementId, imageElementId, imageUrl, text)
	{
		
		var textElement = document.getElementById(textElementId);
		var imageElement = document.getElementById(imageElementId);
		
		textElement.style.display = "block";
		textElement.firstChild.innerHTML = text;
		
		imageElement.src = imageUrl;
	
	},

	hidePlace: function(welcomeMapId, textElementId, imageElementId, imageUrl)
	{

		var textElement = document.getElementById(textElementId);
		var imageElement = document.getElementById(imageElementId);
		
		textElement.style.display = "none";
	
		imageElement.src = imageUrl;

	}

}