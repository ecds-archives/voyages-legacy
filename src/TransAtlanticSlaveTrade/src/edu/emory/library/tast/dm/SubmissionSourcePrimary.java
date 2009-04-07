package edu.emory.library.tast.dm;

import edu.emory.library.tast.util.StringUtils;

public class SubmissionSourcePrimary extends SubmissionSource
{
	
	private String note;
	private String location;
	private String series;
	private String name;
	private String volume;
	private String details;
	
	public boolean equals(Object obj)
	{

		// if both have id, the comparision is done based on ids
		if (!super.equals(obj))
			return false;
		
		if (!(obj instanceof SubmissionSourcePrimary))
			return false;
		
		final SubmissionSourcePrimary that = (SubmissionSourcePrimary) obj;
		
		// this is used only if both are new
		return
			StringUtils.compareStrings(this.note, that.note);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

}