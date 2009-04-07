package edu.emory.library.tast.common.voyage;

public abstract class VoyageRouteSymbol
{
	public abstract String[] getSimpleSymbol();
	public abstract String getLegendSymbol();
	public abstract String[] getNthSymbol(int n);
}

abstract class VoyageRouteSimpleSymbol extends VoyageRouteSymbol
{
	private String baseName;
	private boolean principal;
	
	public VoyageRouteSimpleSymbol(String baseName, boolean principal)
	{
		this.baseName = baseName;
		this.principal = principal;
	}
	
	public String[] getNthSymbol(int n)
	{
		return new String[] {
				baseName + 
				(principal ? "-circle" : "") + "-" +
				(n + 1)};
	}
	
	public String[] getSimpleSymbol()
	{
		return new String[] {
				baseName + 
				(principal ? "-circle" : "")};
	}
	
	public String getLegendSymbol()
	{
		return 
			baseName + 
			(principal ? "-circle" : "");
	}
	
}

abstract class VoyageRouteNumberedSymbol extends VoyageRouteSymbol
{
	private String baseName;
	private int index;
	private boolean principal;
	
	public VoyageRouteNumberedSymbol(String baseName, int index, boolean principal)
	{
		this.baseName = baseName;
		this.index = index;
		this.principal = principal;
	}
	
	public String[] getNthSymbol(int n)
	{
		return new String[] {
				baseName + "-" +
				"no" + (index + 1) +
				(principal ? "-circle" : "") + "-" +
				(n + 1)};
	}
	
	public String[] getSimpleSymbol()
	{
		return new String[] {
				baseName + "-" +
				"no" + (index + 1)  + 
				(principal ? "-circle" : "")};
	}
	
	public String getLegendSymbol()
	{
		return
			baseName + "-" +
			"no" + (index + 1)  + 
			(principal ? "-circle" : "");
	}
	
}

class VoyageRouteSymbolBegin extends VoyageRouteSimpleSymbol
{
	public VoyageRouteSymbolBegin()
	{
		super("pin-red-anchor", false);
	}	
}

class VoyageRouteSymbolPurchase extends VoyageRouteNumberedSymbol
{
	public VoyageRouteSymbolPurchase(int index, boolean principal)
	{
		super("pin-blue", index, principal);
	}
}

class VoyageRouteSymbolPurchasePrincipal extends VoyageRouteSimpleSymbol
{
	public VoyageRouteSymbolPurchasePrincipal()
	{
		super("pin-blue-blank", true);
	}
}

class VoyageRouteSymbolSell extends VoyageRouteNumberedSymbol
{
	public VoyageRouteSymbolSell(int index, boolean principal)
	{
		super("pin-green", index, principal);
	}
}

class VoyageRouteSymbolSellPrincipal extends VoyageRouteSimpleSymbol
{
	public VoyageRouteSymbolSellPrincipal()
	{
		super("pin-green-blank", true);
	}
}

class VoyageRouteSymbolEnd extends VoyageRouteSimpleSymbol
{
	public VoyageRouteSymbolEnd()
	{
		super("pin-yellow-anchor", false);
	}
}
