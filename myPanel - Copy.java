import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer; 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

public class myPanel extends JPanel
{
   private static final int SIZE = 500;
   private static final int textSize = 25;
   private static final int DELAY = 0;
   private static Timer t;
   private static boolean [] keys;
   
   public myPanel()
   {
      keys = new boolean[526];
      t = new Timer(DELAY, new Listener());
      t.start();
   }
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      showBoard(g);
   }
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         repaint();
      }
   } 
   public void showBoard(Graphics g)
   {
      g.setColor(Color.pink);
      g.fillRect(0, 0, SIZE, SIZE);
      int x = textSize, y = textSize;
      g.setColor(Color.white);
      g.setFont(new Font("Monospaced", Font.PLAIN, textSize));
      for(int i = 0; i < keys.length; i++)
      {
         if(keys[i] == true)
         {
            String keyHit = intToKey(i);
            g.drawString(keyHit, x, y);
            x += (keyHit.length() * textSize);
         }
      }
   }
   public static String intToKey(int kc)
   {
      if(kc == KeyEvent.VK_SPACE)
         return "SPACE";
      if(kc == KeyEvent.SHIFT)
         return "SHIFT";
      if(kc == KeyEvent.ENTER)
         return "ENTER";
      if(kc == KeyEvent.CONTROL)
         return "CONTROL";
      if(kc == KeyEvent.ALT)
         return "ALT";
      if(kc == KeyEvent.UP)
         return "UP";
   }
   public void hitKey(int kc)
   {
      if(kc >=0 && kc < keys.length)
      {
         keys[kc] = true;
      }
      repaint();
   }
   public void releaseKey(int kc)
   {
      if(kc >=0 && kc < keys.length)
      {
         keys[kc] = false;
      }
      repaint(); 
}


}