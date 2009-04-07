var GlossaryListGlobals = 
{

	glossaryLists: Array(),

	registerGlossaryList: function(glossaryList)
	{
		this.glossaryLists[glossaryList.glossaryListId] = glossaryList;
	},
	
	scrollTo: function(glossaryListId, letter)
	{
		var glossaryList = GlossaryListGlobals.glossaryLists[glossaryListId];
		if (glossaryList) glossaryList.scrollTo(letter);
	}

}

function GlossaryList(glossaryListId, frameId, letterToElementId)
{

	this.glossaryListId = glossaryListId;
	this.frameId = frameId;
	this.letterToElementId = letterToElementId;

}

GlossaryList.prototype.scrollTo = function(letter)
{

	// frame
	var frame = document.getElementById(this.frameId);

	// letter element
	var letterElement = document.getElementById(this.letterToElementId[letter]);
	if (!letterElement) return;

	// get offset and scroll
	var offset = ElementUtils.getOffsetTop(letterElement, frame);
	frame.scrollTop = offset - 5;
	
}