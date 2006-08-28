package edu.emory.library.tast.ui.images.site;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;

public class GaleryImage {
	private Image image;
	private Person[] people;
	private Region[] regions;
	private Port[] ports;
	
	public GaleryImage() {
		
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Person[] getPeople() {
		return people;
	}
	public void setPeople(Person[] people) {
		this.people = people;
	}
	public Port[] getPorts() {
		return ports;
	}
	public void setPorts(Port[] ports) {
		this.ports = ports;
	}
	public Region[] getRegions() {
		return regions;
	}
	public void setRegions(Region[] regions) {
		this.regions = regions;
	}
}
