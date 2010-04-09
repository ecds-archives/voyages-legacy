/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database.table;

import java.text.MessageFormat;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.table.TableBuilder;
import edu.emory.library.tast.common.table.TableBuilderAverage;
import edu.emory.library.tast.common.table.TableBuilderSimple;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;

public class CellVariable
{
	
	private Attribute[] nonNullAttributes;
	private String userLabel;
	private String id;
	private MessageFormat format;
	private TableBuilder[] tableBuilders;
	private String lastRowLabel;

	public CellVariable(String id, String userLabel, String lastRowLabel, TableBuilder[] tableBuilders, Attribute[] nonNullAttributes, MessageFormat format)
	{
		this.id = id;
		this.tableBuilders = tableBuilders;
		this.userLabel = userLabel;
		this.lastRowLabel = lastRowLabel;
		this.format = format;
		this.nonNullAttributes = nonNullAttributes;
	}
	
	public static CellVariable createSingleAverage(String userLabel, String attrName)
	{
		return new CellVariable(
				attrName + "Avg",
				userLabel,
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderAverage(
								null,
								new FunctionAttribute("sum", Voyage.getAttribute(attrName)),
								new FunctionAttribute("count", Voyage.getAttribute(attrName)))},
				new Attribute[] {Voyage.getAttribute(attrName)},
				new MessageFormat("{0,number,#,###,##0.0}"));
	}

	public static CellVariable createSinglePercentageAverage(String userLabel, String attrName)
	{
		return new CellVariable(
				attrName + "Avg",
				userLabel,
				TastResource.getText("database_tableview_averages"),
				new TableBuilder[] {
						new TableBuilderAverage(
								null,
								new FunctionAttribute("sum", new FunctionAttribute("crop_to_0_100", Voyage.getAttribute(attrName))),
								new FunctionAttribute("count", Voyage.getAttribute(attrName)))},
				new Attribute[] {Voyage.getAttribute(attrName)},
				new MessageFormat("{0,number,#,###,##0.0}%"));
	}
	
	public static CellVariable createSingleCount(String userLabel, String attrName)
	{
		return new CellVariable(
				attrName + "Cnt",
				userLabel,
				TastResource.getText("database_tableview_totals"),
				new TableBuilder[] {
						new TableBuilderSimple(null,
								new FunctionAttribute("count", Voyage.getAttribute(attrName)))},
				new Attribute[] {Voyage.getAttribute(attrName)},
				new MessageFormat("{0,number,#,###,###}"));
	}

	public static CellVariable createSingleSum(String userLabel, String attrName)
	{
		return new CellVariable(
				attrName + "Sum",
				userLabel,
				TastResource.getText("database_tableview_totals"),
				new TableBuilder[] {
						new TableBuilderSimple(null,
								new FunctionAttribute("sum", Voyage.getAttribute(attrName)))},
				new Attribute[] {Voyage.getAttribute(attrName)},
				new MessageFormat("{0,number,#,###,###}"));
	}

	public String getId()
	{
		return id;
	}

	public TableBuilder[] getTableBuilders()
	{
		return tableBuilders;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public MessageFormat getFormat()
	{
		return format;
	}

	public Attribute[] getNonNullAttributes()
	{
		return nonNullAttributes != null ? nonNullAttributes : new Attribute[] {};
	}

	public String getLastRowLabel()
	{
		return lastRowLabel;
	}

}
