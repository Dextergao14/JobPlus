package db;

public class MySQLDBUtil {
	private static final String INSTANCE = "db-instance-dexter-jobplus.cj7r2c6o59d4.us-east-2.rds.amazonaws.com";
	private static final String PORT_NUM = "3306";
	public static final String DB_NAME = "dexterjobplus";
	private static final String USERNAME = "dextergao14";
	private static final String PASSWORD = "xyQ107908?";
	public static final String URL = "jdbc:mysql://"
			+ INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&useSSL=false"
			+ "&autoReconnect=true&serverTimezone=UTC";
}
