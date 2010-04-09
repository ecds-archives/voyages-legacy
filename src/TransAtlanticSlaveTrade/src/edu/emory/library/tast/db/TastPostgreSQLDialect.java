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
package edu.emory.library.tast.db;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class TastPostgreSQLDialect extends PostgreSQLDialect
{

	public TastPostgreSQLDialect()
	{
		super();
		
		// registerColumnType(Types.OTHER, "number($p,$s)");
		registerHibernateType(Types.REAL, "double");
		// registerHibernateType(Types.OTHER, "double");

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
