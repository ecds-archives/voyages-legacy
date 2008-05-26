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
