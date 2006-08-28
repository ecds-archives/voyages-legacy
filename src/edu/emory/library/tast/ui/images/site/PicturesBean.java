package edu.emory.library.tast.ui.images.site;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.util.HibernateUtil;

public class PicturesBean {
	private PictureGalery pictureGalery;
	private SearchBean searchBean;
	
	public PicturesBean() {
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Image[] images = (Image[])Image.getImagesList(session).toArray(new Image[] {});
		GaleryImage[] galeryImage = new GaleryImage[images.length];
		for (int i = 0; i < images.length; i++) {
			Image image = images[i];
			Person[] persons = (Person[])image.getPeople().toArray(new Person[] {});
			Port[] ports = (Port[])image.getPorts().toArray(new Port[] {});
			Region[] regions = (Region[])image.getRegions().toArray(new Region[] {});
			galeryImage[i] = new GaleryImage();
			galeryImage[i].setImage(image);
			galeryImage[i].setPeople(persons);
			galeryImage[i].setPorts(ports);
			galeryImage[i].setRegions(regions);
		}
		
		this.pictureGalery = new PictureGalery(galeryImage);
		
		transaction.commit();
		session.close();

	}
	
	public PictureGalery getPictureGalery() {
		return pictureGalery;
	}

	public void setPictureGalery(PictureGalery pictures) {
		this.pictureGalery = pictures;
	}

	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}
	
}
