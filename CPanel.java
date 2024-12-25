import java.awt.*;  
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.Timer;

public class CPanel extends JPanel implements KeyListener{

   private static final int SIZE = 800;      //size of screen
   private static final int textSize = 25;   //font size

   private static Timer t;	
   
   private Graphics g;
   
   private boolean reply; //user selects yes or no on game screen
   
   private ImageIcon lilkt = new ImageIcon("lilktBR.gif"); //decorative items
   private ImageIcon bubble = new ImageIcon("bubble.png");
   private ImageIcon man = new ImageIcon("plain..png"); 
   
   private ImageIcon star = new ImageIcon("star..png"); //images for the top
   private ImageIcon tank = new ImageIcon("tank..png");
   private ImageIcon striped = new ImageIcon("striped..png");//vert
   private ImageIcon stripes = new ImageIcon("stripes..png");//horiz
   private ImageIcon dot = new ImageIcon("dot..png");
   private ImageIcon sleeved = new ImageIcon("sleeved..png");//short sleeves
   
   ImageIcon[] topImages = new ImageIcon[]{star, tank, striped, stripes, dot, sleeved};
   
   private ImageIcon jeans = new ImageIcon("jeans..png");
   private ImageIcon longdress = new ImageIcon("longdress.PNG");
   private ImageIcon longskirt = new ImageIcon("longskirt..png");
   private ImageIcon shortdress = new ImageIcon("shortdress.PNG");
   private ImageIcon shorts = new ImageIcon("shorts..png");
   private ImageIcon shortskirt = new ImageIcon("shortskirt..png");
   private ImageIcon slacks = new ImageIcon("slacks.PNG");
   
   ImageIcon[] bottomImages = new ImageIcon[]{jeans, longdress, longskirt, shortdress, shorts, shortskirt, slacks};
                          
   private Button [] homeButtons = new Button[2];                  
   private Button [] gameButtons = new Button[2];
   
   private static final int HOME = 0, TOPS = 1, BOTTOMS = 2, OUTFIT = 3;
   private static int gameMode;
   
   private static final int DELAY = 1;	
   
   private int commandNum = 0; //for seeing which button the user is hovering over/selecting
   
   private boolean waiting; //boolean variable for if we are waiting for the user to select a choice
   
   private static String[] tops;
   private static String[] bottoms;
   private static int index;
   private static ArrayList<String> askedQ = new ArrayList<String>();
   
   private static String curt; //current top
   private static String curb; //current bottom
   private static String[] curtSplit;
   private static String[] curbSplit;
   
   private static int topIx;
   
   private static String msg; //message displayed after user selects yes or no
   private static boolean bottomrdy = false; //if ready to choose bottom

   
   public CPanel() //basic set ups
   {
      gameMode = HOME;
      JPanel panel = new JPanel();  
      panel.setBounds(0,0,SIZE,SIZE); 
      
      setFocusable(true);
      requestFocusInWindow(); 
      index = 1;
   
      msg = "";     
      Shape r1 = new Rectangle(200,SIZE-300,150, 100);
      Shape r2 = new Rectangle(400,SIZE-300,150, 100);
   
      ImageIcon buttonImage1 = new ImageIcon("start.png");
      ImageIcon buttonImage2 = new ImageIcon("quit (2).png");
      
      homeButtons[0] = new Button(r1, "start", buttonImage1, buttonImage1); //home buttons
      homeButtons[1] = new Button(r2, "quit", buttonImage2, buttonImage2);
   
      ImageIcon buttonImage3 = new ImageIcon("yes1.png");
      ImageIcon buttonImage4 = new ImageIcon("no1.png");
      
      gameButtons[0] = new Button(r1, "yes", buttonImage3, buttonImage3); //game buttons
      gameButtons[1] = new Button(r2, "no", buttonImage4, buttonImage4); 
      
      
      try{
         tops = readFile("tops.txt"); //text files with questions
         bottoms = readFile("bottoms.txt");
      }
      catch (IOException e)
      {
      }//for IOException
   
      t = new Timer(DELAY, new Listener());	     
      t.start();
   
   }  
   
   public void showBoard(Graphics g) 
   {  
      if(gameMode == HOME){ //home screen
         g.setColor(Color.pink);              
         g.fillRect(0, 0, SIZE, SIZE);       
         g.drawImage(lilkt.getImage(), 200, SIZE-780, 354, 530, null);    //decorative items
         g.drawImage(bubble.getImage(), 500, SIZE-760, 208, 147, null);                  					          
      
         g.setColor(Color.white);
         g.setFont(new Font("Monospaced", Font.PLAIN,textSize));
         g.setFont(g.getFont().deriveFont(Font.BOLD, 96F));
         
         if(commandNum == 0){
            g.drawString("^",  250, 700);  //arrow to show which button user is pointing to
         }
         else if(commandNum == 1){
            g.drawString("^",  450, 700); 
         }
         g.setFont(g.getFont().deriveFont(Font.BOLD,  24F));
         g.setColor(Color.red);
         g.drawString("Use 'A' and 'D' to flip and  'Enter' to choose", 100, 720);
         
      
         for(Button b:homeButtons) //home buttons
         {
            b.drawButton(g);
         }
      }
      else if(gameMode == TOPS){ //tops game screen
         g.setColor(Color.blue);              
         g.fillRect(0, 0, SIZE, SIZE);
               
         g.setColor(Color.white);
         g.setFont(new Font("Monospaced", Font.PLAIN,textSize));
         g.setFont(g.getFont().deriveFont(Font.BOLD, 96F));
         if(commandNum == 0){
            g.drawString("^",  250, 700);
         }
         else if(commandNum == 1){
            g.drawString("^",  450, 700); 
         }
         g.drawImage(man.getImage(), 40, SIZE-640, 78, 327, null);
         
         g.setFont(g.getFont().deriveFont(Font.BOLD,  24F));
         g.setColor(Color.orange);
         g.drawString("Use 'A' and 'D' to flip and  'Enter' to choose", 100, 720);
         g.drawString("Click 'Enter' if nothing is on screen'",120, 100);
           
         if(!topsDone())
         {
            String s = tops[index];
            if(s != null && s.length() > 0)
               drawQ(g, s);
         }
         
         if(msg.length() > 0)
         {
            drawQ(g, msg);
         }
         
         for(Button b:gameButtons) //buttons
         {  
            b.drawButton(g);
         }  
         if(bottomrdy){
            gameMode = BOTTOMS;
            repaint();
         }   
      }
      else if(gameMode == BOTTOMS){ //bottoms game screen
         g.setColor(Color.pink);              
         g.fillRect(0, 0, SIZE, SIZE);
               
         g.setColor(Color.white);
         g.setFont(new Font("Monospaced", Font.PLAIN,textSize));
         g.setFont(g.getFont().deriveFont(Font.BOLD, 96F));
         
         if(commandNum == 0){
            g.drawString("^",  250, 700); 
         }
         else if(commandNum == 1){
            g.drawString("^",  450, 700); 
         }
         
         g.drawImage(man.getImage(), 40, SIZE-640, 78, 327, null);
         
         g.setFont(g.getFont().deriveFont(Font.BOLD, 24F));
         g.setColor(Color.orange);
         g.drawString("Use 'A' and 'D' to flip and 'Enter' to choose", 100, 720);
         
           
         if(!bottomsDone())
         {
            String s = bottoms[index];
            if(s != null && s.length() > 0)
               drawQ(g,  s);
         }
         
         if(msg.length() > 0)
         {
            drawQ(g, msg);
         }
         for(Button b:gameButtons) //buttons
         {  
            b.drawButton(g);
         }   
      }
      else if(gameMode == OUTFIT){ //generating outfit
         g.setColor(Color.LIGHT_GRAY);              
         g.fillRect(0, 0, SIZE, SIZE);
         //g.drawImage(man.getImage(), 300, SIZE-780, 170,531, null); 
         
         String[] curtSplit = curt.split(" ");
         String[] curbSplit = curb.split(" ");
                  
         for(int i = 0; i < curtSplit.length; i++){
            if(curtSplit[i].contains("?")){
               if(curtSplit[i].contains("top?")){
                  curt = curtSplit[i-1];//gets rid of " top?"
               }
               else{
                  curt = curtSplit[i].substring(0, curtSplit[i].length() - 1);
               }
            }
         }
         for(int i = 0; i < curbSplit.length; i++){
            if(curbSplit[i].contains("?")){
               curb = curbSplit[i].substring(0, curbSplit[i].length() - 1);
               
               if(curb.contains("dress") || curb.contains("skirt")){
                  curb = curbSplit[i - 1] + curb;
               }
            }
         }
         if(!curt.contains("..png")){
            curt = curt + "..png";
            curb = curb + "..png";
         }
         
         if(!curb.contains("dress")){
            for(int i = 0; i < topImages.length; i++){
               if(curt.equals(topImages[i].getDescription())){
                  g.drawImage(topImages[i].getImage(), 300, SIZE - 740, 156, 654, null);
               }
            } 
         }
         for(int i = 0; i < bottomImages.length; i++){
            if(curb.equals(bottomImages[i].getDescription())){
               g.drawImage(bottomImages[i].getImage(), 337, SIZE - 387, 84, 270, null);
               drawQ(g, "Here's your outfit!!");
            }
         } 
      }
   }
   
   public boolean topsDone(){
     // String s = tops[index]; 
      return (tops == null || index >= tops.length || tops[index].equals("0") || msg.equals("Top Generated!"));
     // return (s.contains("mesh") || s.contains("polka") || s.contains("horiz") || s.contains("vertical") || s.contains("short") || s.contains("sweater"));
   }  
   public boolean bottomsDone(){
     // String s = bottoms[index];
      return (bottoms == null || index >= bottoms.length || bottoms[index].equals("0") || msg.equals("Bottom Generated!"));
     // return (s.contains("long") || s.contains("short") || s.contains("overalls") || s.contains("slacks") || s.contains("jeans"));
   
   } 
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      showBoard(g);			      
   }
   
   public static int getFileSize(String fileName)throws IOException
   {
      Scanner input = new Scanner(new FileReader(fileName));
      int size = 0;
      while (input.hasNextLine())				
      {
         size++;										
         input.nextLine();							
      }
      input.close();									
      return size;
   }
   
   public static String[] readFile(String fileName)throws IOException
   {
      int size = getFileSize(fileName);		
      String[] list = new String[size];		
      Scanner input = new Scanner(new FileReader(fileName));
      int i = 0;											//index for placement in the array
      String line;	
      while (input.hasNextLine())				
      {
         line = input.nextLine();					
         list[i] = line;								
         i++;											        
      }
      input.close();	
      return list;					
   }
   
   public void drawQ(Graphics g, String s) //display question on screen
   {
      if(s.length() > 0){
         g.setFont(g.getFont().deriveFont(Font.BOLD, 32F));
         g.setColor(Color.yellow);
         
         if(s.contains("Restarting")){ 
            g.setFont(g.getFont().deriveFont(Font.BOLD, 24F));
            g.drawString(s,85,150);
         }
         else if(s.contains("Generated")){
            g.drawString(s,190,100);
            bottomrdy = true;
            //g.drawString("Press 'Enter' to choose your bottom",100,500);
         }
         else{
            g.drawString(s, 190, 300);
            repaint();
         }
      }
      if(!s.contains("Generated")){
         askedQ.add(s);
      }
   }

   public void keyTyped(KeyEvent e){}
   
   public void keyReleased(KeyEvent e){}
   
   public void keyPressed(KeyEvent e){ // 0 left  , 1 right
      int code = e.getKeyCode();
      if(code == KeyEvent.VK_ESCAPE)	         //End the program	
         System.exit(1);
      if(code == KeyEvent.VK_A){
         commandNum--;
            
         if(commandNum < 0){
            commandNum = 0;
         }
      }
         
      if(code == KeyEvent.VK_D){
         commandNum++;
            
         if(commandNum > 1){
            commandNum = 1;
         }
      }
      if(gameMode == HOME){ //on Home screen
         if(code == KeyEvent.VK_ENTER){
            
            if(commandNum == 0){ //start
               gameMode = TOPS;
               repaint();
            }
            
            if(commandNum == 1){//quit
               System.exit(0);
            }
         }   
      }  
      else if(gameMode == TOPS){ //on Tops screen
         if(code == KeyEvent.VK_ENTER){
            if(commandNum == 0){ //user selects yes
               if(topsDone()){
                  msg = "Top Generated! Time for bottoms";
                  gameMode = BOTTOMS;
                  curt = askedQ.get(askedQ.size() - 1);
                  askedQ.clear();
                  index = 1;
               }
               
               else if(!topsDone()){
                  index = index * 2 + 1;
               }
            }
            
            else if(commandNum == 1){ //user selects no
               if(!topsDone()){
                  index = index * 2;
               }
               if(topsDone()){ //no and past available options
                  msg = "Restarting the questions for your top options now.";
                  index = 1;
               }
            }
         }
      }
      else if(gameMode == BOTTOMS){ //on Tops screen
         if(code == KeyEvent.VK_ENTER){
            if(bottomsDone()){
               msg = "Bottoms Generated!";
               curb = askedQ.get(askedQ.size() - 1);
               gameMode = OUTFIT;
            }
            else if(commandNum == 0){ //user selects yes
               if(!bottomsDone()){
                  index = index * 2 + 1;
               }
            }
            else if(commandNum == 1){ //user selects no
               if(!bottomsDone()){
                  index = index * 2;
               }
               if(bottomsDone()){ //no and past available options
                  msg = "Restarting the questions for your top options now.";
                  index = 1;
               }
            }
         }
      }
      repaint();
   }   
   
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)	   //this is called for each timer iteration
      {
         repaint();                                   
      }
   }
}