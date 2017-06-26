package com.pyj.chatClient0213.clientIO.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.security.auth.login.LoginContext;

import com.pyj.chatClient0213.clientIO.dao.DBManager;
import com.pyj.chatClient0213.clientIO.event.LoginController;
import com.pyj.chatClient0213.clientIO.event.SignUpController;

public class DBManager {
	final static int ERROR_CODE_DUPLICATE_ID=1062;
	
	static ResultSet rs;
	
	private static DBManager inst;
	private static DBManager dbm = new DBManager();


	private Connection con = null;
	private Statement state = null;
	
	private String username = "root";
	private String password = "12345";
	private String dbName = "mydb";
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	private String dbUrl = "jdbc:mysql://localhost:3306/" + "mydb?autoReconnect=true&useSSL=false";

	public DBManager() {
	}

	public DBManager getInstance() {
		if (inst == null) {
			inst = new DBManager();
		}
		return inst;
	}

	public static DBManager getInst() {
		return inst;
	}

	public static void setInst(DBManager inst) {
		DBManager.inst = inst;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Statement getState() {
		return state;
	}

	public void setState(Statement state) {
		this.state = state;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public void connectDB() {
		// DB 접속
		try {
			System.out.println("이니셜라이저 시작");
			Class.forName(dbm.getInstance().getJdbcDriver()).newInstance();
			dbm.setCon(DriverManager.getConnection(dbm.getDbUrl(),
					dbm.getUsername(), dbm.getPassword()));
			if (dbm.getCon() != null) {
				System.out.println("MySQL DB 접속!");
				dbm.setState(dbm.getCon().createStatement());
				System.out.println("Statement 생성!");
			}
			System.out.println("디비 접속 종료");

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean login(String id, String password){
		String query = "select * from userinfo";
		try {
			ResultSet rs = dbm.state.executeQuery(query);
			while (rs.next()) {
				if(id.equals(rs.getString("id")) && password.equals(rs.getString("password"))){
					return true;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public int addUser(String id, String password){
		String query = "insert into userinfo values('%s', '%s',null, null,null,null)";
		query = String.format(query, id, password);
		try {
			dbm.state.executeUpdate(query);
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return e.getErrorCode();
		}
	}
	

}
