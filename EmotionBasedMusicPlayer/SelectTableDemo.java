package expressionrecognition;
import java.sql.*;
public class SelectTableDemo {

		public static void main(String[] args) {
	    try{
		    Class.forName("oracle.jdbc.driver.OracleDriver");//fully qualified class name
		    Connection con=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt=con.createStatement();
		    
                    while(2==2)
                    {
                       Thread.sleep(50);
                       String q= "Select *from output where srno=(Select max(srno) from output) ";
                       ResultSet rs =  stmt.executeQuery(q);
                        System.out.println("DATA  OF EMP TABLE ");//assume that EMP is already created;
                        rs.next() ;
                        
                            int a= rs.getInt(1);
                            System.out.print(a+ "  " );
                            int b=rs.getInt(2);
                            System.out.print(b + "      ");
                            System.out.println("\n");
                    }                   }
			catch(ClassNotFoundException e)
			{
				System.out.println(e);
			} catch (SQLException e) {
                            System.out.println(e);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
		}
}

	