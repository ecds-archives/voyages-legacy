package edu.emory.library.tas.web.maps;

public class TestMapBean
{
	
	public PointOfInterest[] getPoints()
	{
		return new PointOfInterest[] {
			new PointOfInterest(0, 0, "New York", "<div style=\"white-space: nowrap\"><b>New York</b><br>Total: 141,205 km2<br>Population: 18,976,457<br>Governor: George Pataki</div>"),	
			new PointOfInterest(-90, 0, "Washington", "<div style=\"white-space: nowrap\"><b>Washington</b><br>Total: 184,824 km2<br>Population: 5,894,121<br>Governor: Christine Gregoire</div>"),	
		};
	}

}
