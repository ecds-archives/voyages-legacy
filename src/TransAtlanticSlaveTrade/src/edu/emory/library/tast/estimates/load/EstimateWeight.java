package edu.emory.library.tast.estimates.load;

public class EstimateWeight {
	public Long expRegion;
	public Long impRegion;
	public double weight;
	public double number;
	
	public boolean equals(Object o) {
		if (!(o instanceof EstimateWeight)) {
			return false;
		}
		EstimateWeight that = (EstimateWeight)o;
		return ((this.expRegion == null && that.expRegion == null) || this.expRegion.equals(that.expRegion)) &&
				((this.expRegion == null && that.expRegion == null) || this.impRegion.equals(that.impRegion));
	}
}
