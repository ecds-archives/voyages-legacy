var CheckboxListPopupGlobals = 
{

	checkboxLists: new Array(),

	registerCheckboxList: function(checkboxList)
	{
		CheckboxListPopupGlobals.checkboxLists[checkboxList.checkboxListId] = checkboxList;
	},
	
	popupShow: function(checkboxListId, popupId, imageUrl)
	{
		var checkboxList = CheckboxListPopupGlobals.checkboxLists[checkboxListId];
		if (checkboxList) checkboxList.popupShow(popupId, imageUrl);
	},

	popupHide: function(checkboxListId, popupId)
	{
		var checkboxList = CheckboxListPopupGlobals.checkboxLists[checkboxListId];
		if (checkboxList) checkboxList.popupHide(popupId);
	}

}

function CheckboxListPopup(
	checkboxListId,
	formName)
{
	this.checkboxListId = checkboxListId;
	this.formName = formName;
}

CheckboxListPopup.prototype.popupShow = function(popupId, imageUrl)
{
	var popup = document.getElementById(popupId);
	popup.style.display = "block";
	popup.style.backgroundImage = "url('" + imageUrl + "')";
}

CheckboxListPopup.prototype.popupHide = function(popupId)
{
	var popup = document.getElementById(popupId);
	popup.style.display = "none";
}
