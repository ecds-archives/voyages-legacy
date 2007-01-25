var MainMenuBar = 
{

	mouseOver: function(imgElementId, pagesElementId, imgUrl)
	{
	
		var imgElement = document.getElementById(imgElementId);
		var pagesElement = document.getElementById(pagesElementId);
		
		imgElement.src = imgUrl;
		pagesElement.style.display = "block";
		//Effect.SlideDown(pagesElement, {duration: 0.2});
		
	},

	mouseOut: function(imgElementId, pagesElementId, imgUrl)
	{
	
		var imgElement = document.getElementById(imgElementId);
		var pagesElement = document.getElementById(pagesElementId);
		
		imgElement.src = imgUrl;
		pagesElement.style.display = "none";
	
	}

}