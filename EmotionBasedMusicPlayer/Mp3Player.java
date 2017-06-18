package expressionrecognition;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;
public class Mp3Player {
public static void main(String[] args) {
        Player player; 

        System.out.println("1");

        try {   
     BufferedInputStream buffer = new BufferedInputStream(new FileInputStream("D:\\a.mp3"));
     player = new Player(buffer);
     player.play();
            }
        
        
     catch (Exception e) {
     System.out.println(e);
     }

}
}

