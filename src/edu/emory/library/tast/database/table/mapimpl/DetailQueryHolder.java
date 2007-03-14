package edu.emory.library.tast.database.table.mapimpl;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.maps.AttributesMap;
import edu.emory.library.tast.maps.AttributesRange;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class DetailQueryHolder extends AbstractTransformerQueryHolder {

	public DetailQueryHolder(Conditions conditions) {
		/**
		 * 1. portdep Port of Departure 2. deptreg Region of departure
		 * 
		 * 3. embport First place of intended embarkation 4. majbuyrg Principal
		 * region of embarkation 4a.majbyimp Imputed principal region of
		 * embarkation 5. regem1 First region of embarkation 6. regem2 Second
		 * region of embarkation 7. regem3 Third region of embarkation
		 * 
		 * 8. arrport First port of intended disembarkation 9. adpsale1 Second
		 * port of disembarkation 10.adpsale2 Third port of disembarkation
		 * 11.majselrg Principal region of disembarkation
		 * 
		 * 12.majselpt Imputed port of slave arrival 13.mjselimp Imputed
		 * principal region of sale
		 * 
		 * 14.portret Port at which voyage ended
		 */
		Conditions localConditions = (Conditions)conditions.clone();
		//localConditions.addCondition(VoyageIndex.getApproved());

		//localConditions.addCondition(VoyageIndex.getAttribute("remoteVoyageId"), new DirectValue(Voyage.getAttribute("iid")), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue(new String[] {"Voyage"}, new String [] {"v"}, localConditions);

		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("portdep").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("deptregimp").getAttributes()[0]);
		
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("plac1tra").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("plac2tra").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("plac3tra").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("regem1").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("regem2").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("regem3").getAttributes()[0]);
		//qValue.addPopulatedAttribute(Voyage.getAttribute("embport"));
		//qValue.addPopulatedAttribute(Voyage.getAttribute("embport2"));
		//qValue.addPopulatedAttribute(Voyage.getAttribute("embreg"));
		//qValue.addPopulatedAttribute(Voyage.getAttribute("embreg2"));
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("mjbyptimp").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("majbyimp").getAttributes()[0]);

		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("sla1port").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("adpsale1").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("adpsale2").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("regdis1").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("regdis2").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("regdis3").getAttributes()[0]);
		
		//qValue.addPopulatedAttribute(Voyage.getAttribute("arrport"));
		//qValue.addPopulatedAttribute(Voyage.getAttribute("arrport2"));
		//qValue.addPopulatedAttribute(Voyage.getAttribute("regarrp"));
		//qValue.addPopulatedAttribute(Voyage.getAttribute("regarrp2"));
		qValue.addPopulatedAttribute(Voyage.getAttribute("mjselimp"));

		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("portret").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("retrnreg").getAttributes()[0]);
		
		
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("datedep").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("datebuy").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("dateleftafr").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("dateland1").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("dateland2").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("dateland3").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("datedepam").getAttributes()[0]);
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("dateend").getAttributes()[0]);

		AttributesMap attrsMap = new AttributesMap();
		List col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("portdep"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("deptregimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("plac1tra"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("plac2tra"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("plac3tra"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("regem1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("regem2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("regem3"), 0 ,1));
		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("embport"), 0 ,1));
//		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("embport2"), 0 ,1));
//		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("embreg"), 0 ,1));
//		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("embreg2"), 0 ,1));
//		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("mjbyptimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("majbyimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("sla1port"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("adpsale1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("adpsale2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("regdis1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("regdis2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("regdis3"), 0 ,1));
		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("arrport"), 0 ,1));
//		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("arrport2"), 0 ,1));
//		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("regarrp"), 0 ,1));
//		attrsMap.addColumn(col);
//		col = new ArrayList();
//		col.add(new AttributesRange(Voyage.getAttribute("regarrp2"), 0 ,1));
//		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("mjselimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("portret"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("retrnreg"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("datedep"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("datebuy"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("dateleftafr"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("dateland1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("dateland2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("dateland3"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("datedepam"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(VisibleAttribute.getAttribute("dateend"), 0 ,1));
		attrsMap.addColumn(col);
		
		this.setAttributesMap(attrsMap);
		this.addQuery("Voyage trail", new QueryValue[] {qValue});
	}
	
	protected void performExecuteQuery(QueryValue[] queries) {
		this.setRawQueryResponse(queries[0].executeQuery());
	}

}
