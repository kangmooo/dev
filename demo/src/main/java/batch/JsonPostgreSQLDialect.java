package batch;

import org.hibernate.dialect.PostgreSQL9Dialect;

import java.sql.Types;

@SuppressWarnings("unused")
public class JsonPostgreSQLDialect extends PostgreSQL9Dialect {
	public JsonPostgreSQLDialect() {
		super();
		this.registerColumnType(Types.JAVA_OBJECT, "json");
		this.registerHibernateType(Types.OTHER, "JsonUserType");
//		registerFunction("json_array_elements_text", new SQLFunctionTemplate((Type) new JSONArray() ,"json_array_elements_text(?1)"));
//		registerFunction("json_array_length", new SQLFunctionTemplate((Type) new JSONArray() ,"json_array_elements_text(?1)"));
	}
}