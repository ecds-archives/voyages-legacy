package edu.emory.library.tast.ui.images.site;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.ui.search.query.SearchBean;

public class PicturesBean {
	private PictureGalery pictureGalery;
	private SearchBean searchBean;
	
	public PicturesBean() {
		this.pictureGalery = new PictureGalery(Image.getImagesArray());
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
