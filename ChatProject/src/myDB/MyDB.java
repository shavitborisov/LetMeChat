package myDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Statement;

import org.apache.commons.lang.StringEscapeUtils;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MyDB {
	Connection con;
	
	public MyDB() throws Exception {
		Properties prop = new Properties();
		String fileName = "C:\\Users\\Shavit\\workspace\\ChatProject\\config\\ChatProject.config";
		InputStream is = new FileInputStream(fileName);
		
		prop.load(is);
		
		String connectionURL = "jdbc:mysql://" +
				prop.getProperty("MySQLServer.Host", "localhost") + ":" +
				prop.getProperty("MySQLServerHost.port", "3306") + "/" +
				prop.getProperty("MySQLServerHost.schema");
		String user = prop.getProperty("MySQLServerHost.user");
		String password = prop.getProperty("MySQLServerHost.password");
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(connectionURL, user, password);
		} catch (Exception e) {
			System.out.println("error connecting to MySQL database");
			con = (Connection)null;
			throw(e);
		}
	}
	
	public boolean UsernameTaken(String user) {
		String fldname[] = { "username" };
		String fldvalue[] = { user };
		
		return runSQL(fldname, fldvalue, "SELECT", null);
	}
	
	public boolean MailTaken(String mail) {
		String fldname[] = { "mail" };
		String fldvalue[] = { mail };
		
		return runSQL(fldname, fldvalue, "SELECT", null);
	}
	
	public boolean UserExists(String user, String pass) {
		String enc_pass = Base64.getEncoder().encodeToString(pass.getBytes());
		String fldname[] = { "username", "password" };
		String fldvalue[] = { user, enc_pass };
		
		return runSQL(fldname, fldvalue, "SELECT", null);
	}
	
	public void fillUserInfo(HttpSession session) {
		try {
			String fldname[] = { "username", "sex", "name", "age", "ageDisp", "bio", "mailDisp", "mail" };
			String fldvalue[] = { (String) session.getAttribute("logged") };
			
			ResultSet rs = runSQLRes(fldname, fldvalue, "SELECT", null);
			
			if ((rs != null) && (rs.next())) {
				Date birthdate = rs.getDate("age");	
			    Date today = new Date((Calendar.getInstance().getTime()).getTime());
				
				long msecDiff = Math.abs(today.getTime() - birthdate.getTime());
				int age = (int) ((((((msecDiff / 1000) / 60) / 60)) / 24) / 365);
				
				UserInfo info = new UserInfo(rs.getString("name"), rs.getString("ageDisp").equals("yes"),
								rs.getString("bio"), age, rs.getString("sex"), rs.getString("mailDisp").equals("yes"),
								rs.getString("mail"));
				session.setAttribute("info", info);
			}
		} catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
	}
	
	public boolean checkUserOnOff(String user) {
		String fldname[] = { "username", "is_online" };
		String fldvalue[] = { user, "yes" };
		
		return runSQL(fldname, fldvalue, "SELECT", null);
	}
	
	public void setUserOnOff(HttpSession session, String state) {
		String user = (String) session.getAttribute("logged");
		String fldname[] = { "is_online" };
		String fldvalue[] = { state };
		
		runSQL(fldname, fldvalue, "UPDATE", user);
	}
	
	private static String getField(HttpServletRequest req, String param) {
		return (req.getParameter(param) != null ? req.getParameter(param) : "");
	}
	
	private String createSQLString(String fldname[], String fldvalue[], String sqlAction, String user) {
		String sqlString = "";
		int i;
		
		switch (sqlAction) {
			case "UPDATE":
				sqlString += sqlAction + " users SET ";
				
				for (i = 0; i < fldname.length; ++i) {
					sqlString += fldname[i] + "='" + fldvalue[i] + "'";
					if (i != fldname.length - 1)
						sqlString += ", ";
				}
				
				break;
			case "INSERT":
				sqlString += sqlAction + " INTO users (";
				
				for (i = 0; i < fldname.length; ++i) {
					sqlString += fldname[i];
					if (i != fldname.length - 1)
						sqlString += ", ";
					else
						sqlString += ") VALUES ('";
				}
				
				for (i = 0; i < fldvalue.length; ++i) {
					sqlString += fldvalue[i] + "'";
					if (i != fldvalue.length - 1)
						sqlString += ", '";
					else
						sqlString += ")";
				}
				
				break;
			case "DELETE":
				sqlString += sqlAction + " FROM users"; break;
			
			case "SELECT":
				sqlString += sqlAction + " ";
				
				for (i = 0; i < fldname.length; ++i) {
					sqlString += fldname[i];
					if (i != fldname.length - 1)
						sqlString += ", ";
					else
						sqlString += " FROM users";
				}
				
				break;
		}
		
		switch (sqlAction) {
			case "UPDATE":
				sqlString += " WHERE username='" + user + "'"; break;
			case "DELETE":
			case "SELECT":
				if (fldvalue != null) {
					sqlString += " WHERE ";
					for (i = 0; i < fldvalue.length; ++i) {
						sqlString += fldname[i] + "='" + fldvalue[i] + "'";
						if (i != fldvalue.length - 1)
							sqlString += " AND ";
					}
				}
		}
		
		return sqlString;
	}
	
	private boolean runSQL(String fldname[], String fldvalue[], String sqlAction, String user) {
		String sqlString = createSQLString(fldname, fldvalue, sqlAction, user);
		try {
			Statement statement = (Statement) con.createStatement();
			ResultSet rs;
			boolean returnValue = true;
			if (sqlAction.equals("SELECT")) {
				rs = statement.executeQuery(sqlString);
				if (!((rs != null) && (rs.next())))
					returnValue = false;
			}
			else
				statement.executeUpdate(sqlString);
			statement.close();
			
			return returnValue;
		} catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage()); return false;}
	}
	
	private ResultSet runSQLRes(String fldname[], String fldvalue[], String sqlAction, String user) {
		String sqlString = createSQLString(fldname, fldvalue, sqlAction, user);
		try {
			Statement statement = (Statement) con.createStatement();
			return statement.executeQuery(sqlString);
			
		} catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage()); return null;}
	}
	
	public void AddNewUser(HttpServletRequest request) {
		String enc_pass = Base64.getEncoder().encodeToString(getField(request, "pass").getBytes());
		String fldname[] = { "mail", "username", "password", "age", "sex" };
		String fldvalue[] = { getField(request, "mail"), getField(request, "username"),
				enc_pass, getField(request, "bday"), getField(request, "sex") };
		
		runSQL(fldname, fldvalue, "INSERT", null);
	}
	
	public void updatePersonal(HttpServletRequest request, HttpSession session) {		
		String name_fixed = StringEscapeUtils.escapeSql(getField(request, "name"));
		String bio_fixed = StringEscapeUtils.escapeSql(getField(request, "bio"));
		
		String user = (String) session.getAttribute("logged");
		String fldname[] = { "name", "ageDisp", "bio", "mailDisp" };
		String fldvalue[] = { name_fixed, getField(request, "ageDisp"), bio_fixed, getField(request, "mailDisp") };
		
		runSQL(fldname, fldvalue, "UPDATE", user);
	}
	
	public void updateMail(HttpServletRequest request, HttpSession session) {
		String user = (String) session.getAttribute("logged");
		String fldname[] = { "mail" };
		String fldvalue[] = { getField(request, "newMail") };
		
		runSQL(fldname, fldvalue, "UPDATE", user);
	}
	
	public void updatePass(HttpServletRequest request, HttpSession session) {
		String user = (String) session.getAttribute("logged");
		String enc_pass = Base64.getEncoder().encodeToString(getField(request, "newPass").getBytes());
		String fldname[] = { "password" };
		String fldvalue[] = { enc_pass };
		
		runSQL(fldname, fldvalue, "UPDATE", user);
	}
	
	public void terminateMe(HttpSession session) {
		String user = (String) session.getAttribute("logged");
		String fldname[] = { "username" };
		String fldvalue[] = { user };
		
		runSQL(fldname, fldvalue, "DELETE", null);
	}
	
	public void terminateAccount(HttpSession session, String user_id) {
		String fldname[] = { "id_users" };
		String fldvalue[] = { user_id };
		
		runSQL(fldname, fldvalue, "DELETE", null);
	}
	
	public void updateUsername(HttpSession session, String user_id, String newUsername) {
		try {
			String fldname[] = { "id_users", "username" };
			String fldvalue[] = { user_id };
			
			ResultSet rs = runSQLRes(fldname, fldvalue, "SELECT", null);
			
			String user = "";
			if ((rs != null) && (rs.next())) {
				user = rs.getString("username");
			
				String fldname2[] = { "username" };
				String fldvalue2[] = { newUsername };
				
				runSQL(fldname2, fldvalue2, "UPDATE", user);
			}
		} catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
	}
	
	public String printUsers() {
		String table = "";
		try {
			String fldname[] = { "id_users", "username", "mail", "sex", "name", "bio", "is_online" };
			
			ResultSet rs = runSQLRes(fldname, null, "SELECT", null);
			
			if (rs != null) {
				int numOfCols = fldname.length;
				
				table = "<form id='administration' method='POST'><table id='adminTable'><tr>";
			    
			    for (int i = 0; i < numOfCols; ++i) // Creates table header
			    	table += "<th>" + fldname[i].substring(0, 1).toUpperCase() + fldname[i].substring(1) + "</th>";
			    table += "<th>Terminate</th></tr>";
			    
			    while (rs.next()) {
			    	table += "<tr>";
			    	for (int i = 1; i <= numOfCols; ++i) {
			    		table += "<td><span style='float: left'>" + rs.getString(i) + "</span>";
			    		if ((i == 2) && !rs.getString(1).equals("1")) // case column is username field and not admin
			    			table += "<input style='float: right' onclick='updateUsername(" + rs.getString(1) +
			    			")' type='submit' class='adminTableButton' title='Edit' value='&#x270E;'>";
			    		else
			    			table += "</td>";
			    	}
			      	if (!rs.getString(1).equals("1")) // if not admin
			      		table += "<td><input onclick='deleteUser(" + rs.getString(1) +
			      		")' class='adminTableButton' type='submit' title='Terminate User' value='&#x2702;'></td></tr>";
			      	else
			      		table += "<td></td></tr>";
			    }
			    
			    table += "</table><input type='text' id='delete' name='delete' style='display: none'>" +
			    		 "<input type='text' id='update' name='update' style='display: none'></form>";
			} return table;
		} catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage()); return table;}
	}
}