package edu.emory.library.tast.db;

import org.hibernate.Hibernate;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class TastPostgreSQLDialect extends PostgreSQLDialect
{

	public TastPostgreSQLDialect()
	{
		super();

		registerFunction("date_trunc", new StandardSQLFunction("date_trunc", Hibernate.TIMESTAMP));
		registerFunction("date_part", new StandardSQLFunction("date_part", Hibernate.INTEGER));
		registerFunction("estimate", new StandardSQLFunction("estimate", Hibernate.FLOAT));
		registerFunction("round_to_multiple", new StandardSQLFunction("round_to_multiple", Hibernate.INTEGER));
		registerFunction("crop_to_0_100", new StandardSQLFunction("crop_to_0_100", Hibernate.FLOAT));
		registerFunction("coalesce_to_0_100", new StandardSQLFunction("coalesce_to_0_100", Hibernate.BIG_INTEGER));
		registerFunction("remove_accents", new StandardSQLFunction("remove_accents", Hibernate.STRING));
		registerFunction("period", new StandardSQLFunction("period", Hibernate.STRING));
	}

}
