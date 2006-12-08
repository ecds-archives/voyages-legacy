package edu.emory.library.tast.dm;

import edu.emory.library.tast.dm.attributes.Attribute;


/**
 * Superclass for any dictionary in the application.
 * @author Pawel Jurczyk
 *
 */
public abstract class Dictionary 
{

	private Long id;
	private String name;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object that)
	{
		if (that instanceof Dictionary)
		{
			Dictionary dict = (Dictionary)that;
			return dict.getId().equals(this.getId());
		}
		return false;
	}
	
	public int hashCode()
	{
		return id.hashCode();
	}
	
}