Lightweight SQL wrapper
=====================


Lightweight mysql driver wrapper which let you connect faster and easier to an existing database.

If your project is built with Maven add following to your pom file:
```
<dependency>
    <groupId>com.vladimanaev</groupId>
    <artifactId>lightweight-sql-wrapper</artifactId>
    <version>2.4</version>
</dependency>
```

If your project is built with Gradle add following to your gradle setting file:
```
compile 'com.vladimanaev:lightweight-sql-wrapper:2.4'
```

Dependencies
=============
```
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>6.0.2</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
    <version>2.1.1</version>
</dependency>
```

Example
============
```java

//result object class example
public class TestResultObj {

    @ColumnDetails(name = "test_column")
    private String column;

}

// code example

//initialize once
DriverManagerWrapperImpl driverManagerWrapper = DriverManagerWrapperImpl.createDefaultConnectionPool("db_url", "test_user", "password");
MySQLConnector mySQLConnector = new MySQLConnector(driverManagerWrapper);

//on each query do following
Query query = new Query("SELECT * FROM somewhere WHERE everything_is_good = ?");
query.addParameter("true", JDBCType.BOOLEAN);

List<TestResultObj> testResultObjs = mySQLConnector.getConnection().executeSelectQuery(TestResultObj.class,
                                                                                       TestResultObj::new, (f) -> {
    ColumnDetails annotation = f.getAnnotation(ColumnDetails.class);
    return annotation != null ? annotation.name() : null;
}, query);

```