EventAttacher.attachOnWindowEvent("load", window, "toolsInit");

function toolsInit() {
	//EventAttacher.attachById("form:mapLegendCloseButton2", "click", window, "mapLegendButtonClicked");	
	//legend = document.getElementById("form:map-legend");
}

var isLegendVisible = true;
var legend;
var empty;

function mapLegendButtonClicked() {
	var element = document.getElementById("form:mapLegend");
	
	if (isLegendVisible) {
		element.removeChild(legend);
	} else {
		element.appendChild(legend);
	}
	
	isLegendVisible = !isLegendVisible;
}
