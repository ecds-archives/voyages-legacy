var QueryBuilder = 
{

	timeoutIds: new Array(),

	updateExpectedTotal: function(id, hiddenField, delay)
	{
	
		// cancel old queued call
		var oldTimeoutId = QueryBuilder.timeoutIds[id];
		if (oldTimeoutId) Timer.cancelCall(oldTimeoutId);
		
		// right now? ok
		if (delay <= 0)
		{
			doUpdateExpectedTotal(hiddenField);
		}
		
		// delay
		else
		{
			var newTimeoutId = Timer.delayedCall(QueryBuilder, "doUpdateExpectedTotal", delay, hiddenField);
			QueryBuilder.timeoutIds[id] = newTimeoutId;
		}
		
	}
	
	doUpdateExpectedTotal: function(id)
	{
		hiddenField.value = "true";
		ajaxAnywhere.submitAJAX(null, null);
	}

}