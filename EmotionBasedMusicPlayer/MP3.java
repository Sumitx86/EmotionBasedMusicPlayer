package expressionrecognition;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.*;
import javazoom.jl.player.Player;
import static org.jdesktop.application.Application.launch;
public class MP3 {
    private String filename;
    private Player player; 
    
    public MP3(String filename) {
        this.filename = filename;
    }
    public void close() { if (player != null) player.close(); }

    public void play() {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }
        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
   }
    
    public static void main(String[] args) throws Exception  {
              int b2=0;
              Class.forName("oracle.jdbc.driver.OracleDriver");
                    try
                   {
                    Connection con=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt=con.createStatement();
                    String q0= "Select *from output where srno=(Select min(srno) from output) ";
                    ResultSet rs1 =  stmt.executeQuery(q0);
                    rs1.next();
                    int a =rs1.getInt(1);
                    con.close();
                    
                    if(a == 1)
                    {
                        String q1= "delete from output  ";
                        stmt.executeQuery(q1);
                        System.out.println("The Music Player Is Started Now ...");
                        Thread.sleep(5000);
                        String q01= "insert into output values(1,10) ";
                        stmt.executeQuery(q01);
                        System.out.println("Default Values is Added");
                    }
                    else{System.out.println("Table Not Deleted"+ "Because the value of a is > Than" + a);}
                    con.close(); 
                    System.out.println("The Connection is terminated");
                    }catch(SQLException e1){System.out.println(e1);} catch (InterruptedException e1) {
                        System.out.println(e1);
        }
                  
       while(true)
       { 
                    try
                    {
                    Connection conn1=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt=conn1.createStatement();
                    String q2= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs2 =  stmt.executeQuery(q2);
                    rs2.next();
                    b2=rs2.getInt(2);
                    conn1.close();
                    }catch(Exception e4){System.out.println("The b variable initialization"+e4);}
                    
                if( b2 < 20  ){
                expressionrecognition.MP3 mp31 = new expressionrecognition.MP3("D:\\a1.mp3");
                System.out.println("\n");
                System.out.println("********************************************************");
                System.out.println("Play Song - 1 Emotion: "+ b2);
                mp31.play();
                int limit=b2;
                while(limit<20){
                    System.out.println("(a) Play Song - 1 Emotion: " + limit);
                    try{
                    Thread.sleep(2000);
                    Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt1=conn.createStatement();
                    String q10= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs3 =  stmt1.executeQuery(q10);
                    rs3.next();
                    limit = rs3.getInt(2);
                    System.out.println("(b) Play Song - 1 Emotion: " + limit);
                    Thread.sleep(5000);
                    conn.close();
                    }catch(Exception e3){System.out.println(e3);}
                }Thread.sleep(100);mp31.close();}

                
                else if(b2 > 20 && b2<50)
                {
                expressionrecognition.MP3 mp31 = new expressionrecognition.MP3("D:\\a2.mp3");
                mp31.play();
                System.out.println("\n");
                System.out.println("********************************************************");
                System.out.println("Play Song - 2 Emotion: "+ b2 );
                int limit = b2;
                while(limit > 20 && limit<50){
                    System.out.println("(a) Play Song - 2 Emotion: " + limit );
                    try{   Thread.sleep(2000);
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt1=conn.createStatement();
                    String q10= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs3 =  stmt1.executeQuery(q10);
                    rs3.next();
                    limit =rs3.getInt(2);
                    System.out.println("(b) Play Song - 2 Emotion: " + limit);
                    Thread.sleep(5000);
                    conn.close();
                  }catch(Exception e3){System.out.println(e3);}
                }Thread.sleep(100);mp31.close();}
                
             else if(b2 >50){
                expressionrecognition.MP3 mp31 = new expressionrecognition.MP3("D:\\a3.mp3");
                mp31.play();
                System.out.println("\n");
                System.out.println("********************************************************");
                System.out.println("Play Song - 3 Emotion: "+b2);
                int limit=b2;
                while(limit >50 ){
                    System.out.println("(a) Play Song - 3 Emotion: " + limit);
                    try{
                    Thread.sleep(2000);
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt1=conn.createStatement();
                    String q10= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs4 =  stmt1.executeQuery(q10);
                    rs4.next();
                    limit =rs4.getInt(2);
                    System.out.println("(b) Play Song - 3 Emotion: " + limit);
                    Thread.sleep(5000);
                    conn.close();
                   }catch(Exception e3){System.out.println(e3);}
             //Thread.sleep(500);
                }Thread.sleep(100);mp31.close();}
                
                
                else{Thread.sleep(5000);
                System.out.println("No Choice");}
               /*     System.out.println("Goes to Connected -1");
                    String q01= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs01 =  stmt.executeQuery(q01);
                    rs01.next();
                    b2=rs01.getInt(2);
            
                    while(b2 > 0 && b2 < 10);{ 
                    Thread.sleep(5000);
                    }  
        mp3.close();
        
                    expressionrecognition.MP3 mp31 = new expressionrecognition.MP3("D:\\a2.mp3");
                    mp31.play();
                    Thread.sleep(500);
                    
                    System.out.println("Goes to Connected -2");
                    String q02= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs02 =  stmt.executeQuery(q02);
                    rs02.next();
                    b2=rs02.getInt(2);
                    while(b2 > 0 && b2 < 10){ 
                    System.out.println("\n");
                    System.out.println("Connected -2");
                    Thread.sleep(5000);
                    }  
      mp31.close();
                    
                    expressionrecognition.MP3 mp32 = new expressionrecognition.MP3("D:\\a3.mp3");
                    mp32.play();
                    Thread.sleep(500);
                    String q03= "Select *from output where srno=(Select max(srno) from output) ";
                    ResultSet rs03 =  stmt.executeQuery(q03);
                    rs03.next();
                    b2=rs03.getInt(2);
                    while(b2 > 0 && b2 < 10){ 
                    System.out.println("\n");
                    System.out.println("Connected -3");
                    Thread.sleep(5000);
                   }  
       mp32.close();*/
      
                }             
                   }}
    
