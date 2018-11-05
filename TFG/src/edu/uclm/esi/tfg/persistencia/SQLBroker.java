package edu.uclm.esi.tfg.persistencia;

import java.sql.*;

public class SQLBroker {
	private String driver = "com.mysql.jdbc.Driver";
	private String host = "jdbc:mysql://localhost:3306/TFG";
	private String user = "root";
	private String pwd = "root";

	private Connection con=null;

	public SQLBroker() {
		try {
			
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(host,user,pwd);
			
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
			
			System.out.println("SQLException: " + ex.getMessage());

		}

	}
	public Connection getConex() {
		return con;
	}
}
