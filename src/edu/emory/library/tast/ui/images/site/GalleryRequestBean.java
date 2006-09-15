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
	}
	
	public static final String PICT = "pict";
	public static final String SET = "set";
	public static final String ID = "id";
	public static final String GALLERY_TYPE = "obj";
	
	private GalleryParams params = null;
	
	public GalleryRequestBean() {
		params = new GalleryParams();
	}
	
	public GalleryRequestBean.GalleryParams getGalleryParams() {
		return params;
	}
	
}
