package edu.emory.library.tast.db;

import org.hibernate.Hibernate;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class TastHSQLDialect extends HSQLDialect
{

	public TastHSQLDialect()
	{
		super();
		
		// This is just a temporary version for performance evaluations. The
		// custom functions are not ported at all. I just needed to make the
		// remove_accents function to work, because it's used in the main
		// queries. As you may tell, it's not now doing whatever it claims
		// to do.

		registerFunction("date_trunc", new StandardSQLFunction("date_trunc", Hibernate.TIMESTAMP));
		registerFunction("date_part", new StandardSQLFunction("date_part", Hibernate.INTEGER));
		registerFunction("estimate", new StandardSQLFunction("estimate", Hibernate.FLOAT));
		registerFunction("round_to_multiple", new StandardSQLFunction("round_to_multiple", Hibernate.INTEGER));
		registerFunction("crop_to_0_100", new StandardSQLFunction("crop_to_0_100", Hibernate.FLOAT));
		registerFunction("coalesce_to_0_100", new StandardSQLFunction("coalesce_to_0_100", Hibernate.BIG_INTEGER));
		registerFunction("remove_accents", new StandardSQLFunction("upper", Hibernate.STRING));
		registerFunction("period", new StandardSQLFunction("period", Hibernate.STRING));
	}

}
