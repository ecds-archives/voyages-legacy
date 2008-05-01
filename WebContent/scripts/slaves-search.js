AjaxAnywhere.prototype.showLoadingMessage = function()
{
	// ElementUtils.hide(
	// 	"totalBoxBasicTd",
	// 	"totalBoxCountryTd",
	// 	"totalBoxPlacesTd");
	ElementUtils.show(
		"totalUpdateIndicatorBoxBasic",
		"totalUpdateIndicatorBoxCountry",
		"totalUpdateIndicatorBoxPlaces");
}

AjaxAnywhere.prototype.hideLoadingMessage = function()
{
	// ElementUtils.show(
	// 	"totalBoxBasicTd",
	// 	"totalBoxCountryTd",
	// 	"totalBoxPlacesTd");
	ElementUtils.hide(
		"totalUpdateIndicatorBoxBasic",
		"totalUpdateIndicatorBoxCountry",
		"totalUpdateIndicatorBoxPlaces");
}

AjaxAnywhere.prototype.handlePrevousRequestAborted = function()
{
}

var SlavesSearch = 
{

	searchFromBasicBox: function(postpone)
	{
		this.search(postpone, "submitBoxBasic");
	},
	
	searchFromCountryBox: function(postpone)
	{
		this.search(postpone, "submitBoxCountry");
	},

	searchFromPlacesBox: function(postpone)
	{
		this.search(postpone, "submitBoxPlaces");
	},

	search: function(postpone, buttonId)
	{
	
		if (!ajaxAnywhere) return;
	
		var button = document.getElementById(buttonId);
		if (!button) return;

		if (postpone)
		{
		
			this.timeoutId = Timer.extendCall(
				this.timeoutId,
				this, "doUpdateExpectedTotal",
				1000,
				button);

		}
		else
		{
			Timer.cancelCall(this.timeoutId);
			this.doUpdateExpectedTotal(button);
		}

	},
	
	doUpdateExpectedTotal: function(button)
	{
		ajaxAnywhere.submitAJAX(null, button);
	}	

}