EventAttacher.attach(window, "load", window, "chartInit");

function chartInit() {
	EventAttacher.attachById("form:chart-forward-button-id", "click", window, "chartForwardPressed");
	EventAttacher.attachById("form:chart-back-button-id", "click", window, "chartBackPressed");
}

function chartForwardPressed() {
	document.getElementById("form:chart-buttons-hiden").value = "1";
	document.forms["form"].submit();
}

function chartBackPressed() {
	document.getElementById("form:chart-buttons-hiden").value = "-1";
	document.forms["form"].submit();
}