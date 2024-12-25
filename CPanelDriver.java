import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class CPanelDriver{
   public static CPanel screen;
   
   public static void main(String[]args)
   {
      screen = new CPanel();
      JFrame frame = new JFrame("Cluless Outfit Generator");	//window title
      frame.setSize(800, 800);			            //Size of game window
      frame.setLocation(1, 1);				         //location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);	
      
      frame.addKeyListener(screen);
   
      frame.setVisible(true);
      
      frame.setFocusable(true);
   }
}