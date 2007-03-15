AjaxAnywhere.prototype.showLoadingMessage = function()
{
	//document.getElementById("totalContainer").style.display = "none";
	//document.getElementById("totalUpdateIndicator").style.display = "";
}

AjaxAnywhere.prototype.hideLoadingMessage = function()
{
	//document.getElementById("totalContainer").style.display = "";
	//document.getElementById("totalUpdateIndicator").style.display = "none";
}

AjaxAnywhere.prototype.handlePrevousRequestAborted = function()
{
}

var SlavesSearch = 
{

	searchFromBasicBox: function()
	{
		this.search("submitBoxBasic");
	},
	
	searchFromCountryBox: function()
	{
		this.search("submitBoxCountry");
	},

	searchFromPlacesBox: function()
	{
		this.search("submitBoxPlaces");
	},

	search: function(buttonId)
	{
	
		if (!ajaxAnywhere) return;
	
		var button = document.getElementById(buttonId);
		if (!button) return;
	
		this.timeoutId = Timer.extendCall(
			this.timeoutId,
			this, "doUpdateExpectedTotal",
			delay,
			button);

	},
	
	doUpdateExpectedTotal: function(button)
	{
		ajaxAnywhere.submitAJAX(null, button);
	}	

}