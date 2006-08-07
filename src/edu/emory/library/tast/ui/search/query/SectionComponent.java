package edu.emory.library.tast.ui.search.query;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * <p>
 * This compoment is supposed to be used only an a child of
 * {@link edu.emory.library.tast.ui.search.query.SectionGroupComponent}. In general,
 * {@link edu.emory.library.tast.ui.search.query.SectionGroupComponent} can contain several
 * of these which then form tabs. Each section has a title (displayed in the
 * tabs) and a section ID (inteded for accessing and manimulating sections).
 * Otherwise, this components does not do anything, nor it renders any output.
 * </p>
 * 
 * @author Jan Zich
 * 
 */
public class SectionComponent extends UIComponentBase
{
	
	private String title;
	private String sectionId;
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = sectionId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		sectionId = (String) values[2];
	}

	public String getFamily()
	{
		return null;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String text)
	{
		this.title = text;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

}
