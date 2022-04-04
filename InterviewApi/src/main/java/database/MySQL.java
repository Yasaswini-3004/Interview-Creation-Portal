package database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQL{
	Connection conn = null;

	public Connection connect() throws IOException, SQLException{
		String username="root";
		String password="D$m0@321#";
		String ip = "localhost";
		String port = "3306";
		String dbname = "interview";
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbname;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 conn = DriverManager.getConnection(url,username,password);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("wrong credentials or database name");
			}
	    if(conn!=null)
	    	System.out.println("Connected");
	    else
	    	System.out.println("Not Connected");
	    return conn;
		
	}
	
	public void disconnect() throws SQLException {
		if(conn!=null)
			try {
				conn.close();
				System.out.println("Disconnected");
			} catch (SQLException e) {
				
			}
	}
	
	public ResultSet executeQuery(String query, Connection conn) throws SQLException{
		Statement stmt = conn.createStatement();
		try{
		return stmt.executeQuery(query);
		}
		catch(Exception e){
			System.out.println("");
			}
		return null;

	}
	
	public ResultSet executeQuery(PreparedStatement ps) throws SQLException{
		return ps.executeQuery(); 
	
	}
	
	
	public int executeUpdate(String query, Connection conn) throws SQLException {
		int records = 0;
		Statement stmt = conn.createStatement();
		try {
		records = stmt.executeUpdate(query);}
		catch(Exception e){
			System.out.println("Incorrect Query");
			}
		
		return records;
	}
	
	public int executeUpdate(PreparedStatement ps) throws SQLException {
		int records = ps.executeUpdate();
		return records;
	}
	
	public PreparedStatement prepareStatement(String query,Connection con,String... params) throws SQLException
	{
		PreparedStatement ps = null;
		ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
		for(int i=0;i<params.length;i++)
		{
			ps.setString(i+1,params[i]);
		}
		return ps;
	}
	
	public PreparedStatement prepareStatement(String query,Connection con,Object... params) throws SQLException
	{
		PreparedStatement ps = null;
		ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
		for(int i=0;i<params.length;i++)
		{
			ps.setObject(i+1,params[i]);
		}
		return ps;
	}
	
	public PreparedStatement prepareStatement(String query,Connection con,int... params) throws SQLException
	{
		PreparedStatement ps = null;
		ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
		for(int i=0;i<params.length;i++)
		{
			ps.setInt(i+1,params[i]);
		}
		return ps;
	}


	

}