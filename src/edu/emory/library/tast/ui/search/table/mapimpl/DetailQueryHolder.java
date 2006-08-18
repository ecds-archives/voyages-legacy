package edu.emory.library.tast.ui.search.table.mapimpl;

import java.util.ArrayList;
import java.util.List;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageIndex;
import edu.emory.library.tast.ui.maps.AbstractTransformerQueryHolder;
import edu.emory.library.tast.ui.maps.AttributesMap;
import edu.emory.library.tast.ui.maps.AttributesRange;
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
		localConditions.addCondition(VoyageIndex.getApproved());
		
		QueryValue qValue = new QueryValue("VoyageIndex as v", localConditions);

		qValue.addPopulatedAttribute("v.voyage.portdep", true);
		qValue.addPopulatedAttribute("v.voyage.deptreg", true);
		
		qValue.addPopulatedAttribute("v.voyage.plac1tra", true);
		qValue.addPopulatedAttribute("v.voyage.plac2tra", true);
		qValue.addPopulatedAttribute("v.voyage.plac3tra", true);
		qValue.addPopulatedAttribute("v.voyage.regem1", true);
		qValue.addPopulatedAttribute("v.voyage.regem2", true);
		qValue.addPopulatedAttribute("v.voyage.regem3", true);
		qValue.addPopulatedAttribute("v.voyage.embport", true);
		qValue.addPopulatedAttribute("v.voyage.embport2", true);
		qValue.addPopulatedAttribute("v.voyage.embreg", true);
		qValue.addPopulatedAttribute("v.voyage.embreg2", true);
		qValue.addPopulatedAttribute("v.voyage.mjbyptimp", true);
		qValue.addPopulatedAttribute("v.voyage.majbyimp", true);

		qValue.addPopulatedAttribute("v.voyage.sla1port", true);
		qValue.addPopulatedAttribute("v.voyage.adpsale1", true);
		qValue.addPopulatedAttribute("v.voyage.adpsale2", true);
		qValue.addPopulatedAttribute("v.voyage.regdis1", true);
		qValue.addPopulatedAttribute("v.voyage.regdis2", true);
		qValue.addPopulatedAttribute("v.voyage.regdis3", true);
		qValue.addPopulatedAttribute("v.voyage.arrport", true);
		qValue.addPopulatedAttribute("v.voyage.arrport2", true);
		qValue.addPopulatedAttribute("v.voyage.regarrp", true);
		qValue.addPopulatedAttribute("v.voyage.regarrp2", true);
		qValue.addPopulatedAttribute("v.voyage.mjselimp", true);

		qValue.addPopulatedAttribute("v.voyage.portret", true);
		qValue.addPopulatedAttribute("v.voyage.retrnreg", true);
		
		
		qValue.addPopulatedAttribute("v.voyage.datedep", false);
		qValue.addPopulatedAttribute("v.voyage.d1slatr", false);
		qValue.addPopulatedAttribute("v.voyage.dlslatrb", false);
		qValue.addPopulatedAttribute("v.voyage.rrdata31", false);
		qValue.addPopulatedAttribute("v.voyage.datarr32", false);
		qValue.addPopulatedAttribute("v.voyage.datarr33", false);
		qValue.addPopulatedAttribute("v.voyage.ddepamb", false);
		qValue.addPopulatedAttribute("v.voyage.datarr4", false);

		AttributesMap attrsMap = new AttributesMap();
		List col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("portdep"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("deptreg"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("plac1tra"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("plac2tra"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("plac3tra"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regem1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regem2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regem3"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("embport"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("embport2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("embreg"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("embreg2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("mjbyptimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("majbyimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("sla1port"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("adpsale1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("adpsale2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regdis1"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regdis2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regdis3"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("arrport"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("arrport2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regarrp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("regarrp2"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("majselimp"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("portreg"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("retrnreg"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("datedep"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("d1slatr"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("dlslatrb"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("rrdata31"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("datarr32"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("datarr33"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("ddepamb"), 0 ,1));
		attrsMap.addColumn(col);
		col = new ArrayList();
		col.add(new AttributesRange(Voyage.getAttribute("datarr4"), 0 ,1));
		attrsMap.addColumn(col);
		
		this.setAttributesMap(attrsMap);
		this.addQuery("Voyage trail", new QueryValue[] {qValue});
	}
	
	protected void performExecuteQuery(QueryValue[] queries) {
		this.setRawQueryResponse(queries[0].executeQuery());
	}

}
