package edu.emory.library.tast.ui.images.site;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

public class GalleryRequestBean {
	
	public class GalleryParams {
		
		private String galleryType;
		private String id;
		private String visibleSet;
		private String visiblePicture;
		private String vid;
		
		public GalleryParams() {
			Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			if (params.containsKey(GALLERY_TYPE)) {
				galleryType = (String)params.get(GALLERY_TYPE);
			}
			if (params.containsKey(ID)) {
				id = (String)params.get(ID);
			}
			if (params.containsKey(SET)) {
				visibleSet = (String)params.get(SET);
			}
			if (params.containsKey(PICT)) {
				visiblePicture = (String)params.get(PICT);
			}
		}
		
		public void saveState() {
			picturesBean.saveGalleryType(galleryType);
			picturesBean.saveId(id);
			picturesBean.saveVisibleSet(visibleSet);
			picturesBean.saveVisiblePicture(visiblePicture);
		}
		
		public void restoreState() {
			if (galleryType == null && id == null && visibleSet == null &&
					visiblePicture == null && vid == null) {
				galleryType = picturesBean.restoreGalleryType();
				id = picturesBean.restoreId();
				visibleSet = picturesBean.restoreVisibleSet();
				visiblePicture = picturesBean.restoreVisiblePicture();
			}
			this.saveState();
		}
		
		public String getGalleryType() {
			return galleryType;
		}
		public String getId() {
			return id;
		}
		public String getVisiblePicture() {
			return visiblePicture;
		}
		public String getVisibleSet() {
			return visibleSet;
		}

		public String getVid() {
			return vid;
		}
	}
	
	public static final String PICT = "pict";
	public static final String SET = "set";
	public static final String ID = "id";
	public static final String GALLERY_TYPE = "obj";
	public static final String VID = "vid";
	
	private PicturesBean picturesBean;
	
	private GalleryParams params = null;
	
	public GalleryRequestBean() {
		params = new GalleryParams();
	}
	
	public GalleryRequestBean.GalleryParams getGalleryParams() {
		return params;
	}
	
	public PicturesBean getPicturesBean() {
		return picturesBean;
	}

	public void setPicturesBean(PicturesBean picturesBean) {
		this.picturesBean = picturesBean;
		this.picturesBean.setGalleryBean(this);
	}
	
}
