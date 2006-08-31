package edu.emory.library.tast.ui.images.site;

import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.search.query.Query;
import edu.emory.library.tast.ui.search.query.QueryCondition;
import edu.emory.library.tast.ui.search.query.QueryConditionList;
import edu.emory.library.tast.ui.search.query.QueryConditionNumeric;
import edu.emory.library.tast.ui.search.query.QueryConditionRange;
import edu.emory.library.tast.ui.search.query.QueryConditionText;
import edu.emory.library.tast.ui.search.query.SearchBean;
import edu.emory.library.tast.ui.search.query.SearchParameters;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;

public class PicturesBean {

	public static final String PERSON_ENCODE = "person_";

	public static final String PORT_ENCODE = "port_";

	public static final String REGION_ENCODE = "region_";

	public static final String VOYAGE_ENCODE = "voyage_";

	private PictureGalery pictureGalery;

	private SearchBean searchBean;

	private String searchCondition;

	public PicturesBean() {

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		Image[] images = (Image[]) Image.getImagesList(session).toArray(
				new Image[] {});
		GaleryImage[] galeryImage = new GaleryImage[images.length];
		for (int i = 0; i < images.length; i++) {
			Image image = images[i];
			Person[] persons = (Person[]) image.getPeople().toArray(
					new Person[] {});
			Port[] ports = (Port[]) image.getPorts().toArray(new Port[] {});
			Region[] regions = (Region[]) image.getRegions().toArray(
					new Region[] {});
			galeryImage[i] = new GaleryImage();
			galeryImage[i].setImage(image);
			galeryImage[i].setPeople(persons);
			galeryImage[i].setPorts(ports);
			galeryImage[i].setRegions(regions);
		}

		this.pictureGalery = new PictureGalery(galeryImage);

		transaction.commit();
		session.close();

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

	public String showinfo() {

		System.out.println(">>>>>>>> " + searchCondition);

		if (searchCondition.startsWith(PERSON_ENCODE)) {
			int id = Integer.parseInt(searchCondition.substring(PERSON_ENCODE
					.length()));
			Person person = Person.loadById(id);

			Query query = new Query();
			QueryConditionText cond = new QueryConditionText("anyperson");
			cond.setValue(person.getLastName());
			query.addCondition(cond);
			showReport(query);
		} else if (searchCondition.startsWith(PORT_ENCODE)) {
			int id = Integer.parseInt(searchCondition.substring(PORT_ENCODE
					.length()));
			Port port = Port.loadById(id);
			Dictionary[] dem = (Dictionary[]) Dictionary
					.loadDictionaryByName("FirstDemPort", port.getName());

			if (dem.length != 0) {
				Query query = new Query();
				QueryConditionList condRegion = new QueryConditionList(
						"majselpt");
				condRegion.addId(dem[0].getId().toString());
				query.addCondition(condRegion);
				showReport(query);
			}
		} else if (searchCondition.startsWith(REGION_ENCODE)) {
			int id = Integer.parseInt(searchCondition.substring(REGION_ENCODE.length()));
			Region port = Region.loadById(id);
			Dictionary[] dem = (Dictionary[]) Dictionary
					.loadDictionaryByName("DepartureRegion", port.getName());
			
			if (dem.length != 0) {
				Query query = new Query();
				QueryConditionList condRegion = new QueryConditionList(
						"majselrg");
				condRegion.addId(dem[0].getId().toString());
				query.addCondition(condRegion);
				showReport(query);
			}			
		} else if (searchCondition.startsWith(VOYAGE_ENCODE)) {
			int id = Integer.parseInt(searchCondition.substring(VOYAGE_ENCODE.length()));
			Query query = new Query();
			QueryConditionNumeric cond = new QueryConditionNumeric("voyageId", QueryConditionRange.TYPE_EQ);
			cond.setEq(id + "");
			query.addCondition(cond);
			showReport(query);
		} else {
			throw new RuntimeException("Unexpected condition: "
					+ this.searchCondition);
		}

		return "showinfo";
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	private void showReport(Query query) {

		// build db conditions
		Conditions conditions = new Conditions();
		for (Iterator iterQueryCondition = query.getConditions().iterator(); iterQueryCondition
				.hasNext();) {
			QueryCondition queryCondition = (QueryCondition) iterQueryCondition
					.next();
			queryCondition.addToConditions(conditions, false);
		}

		// display first tab
		searchBean.setMainSectionId("listing");

		// set query so that it display in the query builder
		searchBean.setWorkingQuery(query);

		// set search parameters so that we really search
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setConditions(conditions);
		searchParameters.setMapElements(SearchParameters.NOT_SPECIFIED);
		searchParameters.setValuesType(SearchParameters.NOT_SPECIFIED);
		searchBean.setSearchParameters(searchParameters);

	}

}
