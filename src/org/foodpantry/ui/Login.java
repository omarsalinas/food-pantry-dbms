package org.foodpantry.ui;
import java.sql.SQLException;
import java.sql.Statement;

import org.foodpantry.db.*; //import class to create connection objs
import java.sql.ResultSet;
/**
 * Borrowed from:
 * http://www.javaswing.org/jdialog-login-dialog-example.aspx
 * TODO need functionality for a database connection timeout
 */

public class Login {
	
	private static Statement stmt = null;
	private static ResultSet rs = null;

    public static boolean authenticate(String username, String password) throws SQLException {
    	
        String query = "select * from Pantry_Security where User_Name='"+username+"'" +
        		"and password='"+password+"'";
        
        String userNameDB = null;//holds user id returned from query
        String userPwDB = null;//holds user password returned from query
        
    	//create connection to database
    	DBConnection conn = new DBConnection();
    	
    	//proceed only if connection was successful
    	if(conn.Success)
    	{
    		//execute user authentication query
    		stmt = conn.getDBConnection().createStatement();
			rs = stmt.executeQuery(query);
    	  		
    		//if there is at least one row returned, get it
    		if ( rs.next() ){
    			//store credentials from db in local vars
    			userNameDB = rs.getString(1);
    			userPwDB = rs.getString(2);
    			
    			System.out.println(userNameDB);
    			System.out.println(userPwDB);
    			
    			//fail if more than one record for username is found
    			if(rs.next()){
    				conn.closeConnection();
    			return false;}

    			//authenication is sucessfull
    			else if(username.equals(userNameDB) && password.equals(userPwDB)){
    				conn.closeConnection();
    				return true;}

    			//user name or password don't match.
    			else{
    				conn.closeConnection();
    				return false;}			
    		}
    		else{
    			//user password combo not found. Return false
    			return false;
    		}
    		
    	}
    	else {//crapout
    		//need to show that user could not log on due to db error?
    		return false;
    	}
    		
    	// hardcoded username and password
      /*  if (username.equals("bob") && password.equals("secret")) {
            return true;
        }
        return false;*/
    }
}
