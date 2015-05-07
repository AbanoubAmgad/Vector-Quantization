/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vector.quantization;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Abanoub
 */
public class VectorQuantization {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
       
       String [] choices = {"Quantize" , "De-Quantize"} ;
        int response = JOptionPane.showOptionDialog(
                               null                       // Center in window.
                             , "What Do you Want To Do ?!"       // Message
                             , "Vector Quantization"                // Title in titlebar
                             , JOptionPane.YES_NO_OPTION  // Option type
                             , JOptionPane.PLAIN_MESSAGE  // messageType
                             , null                       // Icon (none)
                             , choices                    // Button text as above.
                             , "Quantize"                    // Default button's label
                           ); 
        
        
        if ( response == 0 ){       
//               ArrayList <vector> Pic = new ArrayList<> () ;
//               Pic.add(new vector(6, 2)) ;
//               Pic.add(new vector(6, 3)) ;
//               Pic.add(new vector(7, 6)) ;
//               Pic.add(new vector(7, 7)) ;
//               Pic.add(new vector(2, 2)) ;
//               Pic.add(new vector(2, 3)) ;
//               Pic.add(new vector(1, 6)) ;
//               Pic.add(new vector(1, 7)) ;
//               Pic.add(new vector(5, 1)) ;
//               Pic.add(new vector(5, 2)) ;
//               Pic.add(new vector(6, 6)) ;
//               Pic.add(new vector(6, 7)) ;
//               Pic.add(new vector(1, 2)) ;
//               Pic.add(new vector(1, 3)) ;
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pic.jpg", "jpg");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null) ;
        
 
        File file = chooser.getSelectedFile();
        BufferedImage image=null;

        try {
        image=ImageIO.read(file);
        } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        int width=image.getWidth();
        int height=image.getHeight();
        int[][] pixels=new int[height][width];
        ArrayList <vector> Pic = new ArrayList <> () ;
        Pic.add(new vector ());
        boolean xDone = false , yDone = false ; 
        int vectorsCounter = 0 ;
        for(int x=0;x<width;x++) {
        for(int y=0;y<height;y++) {
        int rgb=image.getRGB(x, y);
        int alpha=(rgb >> 24) & 0xff;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8 )& 0xff;
        int b = (rgb >> 0) & 0xff;
         if (!xDone) {
            Pic.get(vectorsCounter).x = r ;
            xDone = true ; 
        }
        else if (!yDone) {
            Pic.get(vectorsCounter).y = r ;
            xDone = false ; yDone = false ; Pic.add(new vector()); vectorsCounter++ ;
        }


        pixels[y][x]=r;

        }
        }

//        for(int x=0;x<width;x++) {
//        for(int y=0;y<height;y++) { 
//            //System.out.print(pixels[y][x] + " ");
//        }
//        }
//
//        for ( int i = 0 ; i < Pic.size() ; i++) {
//           // System.out.print(Pic.get(i).x + " " + Pic.get(i).y + " ");
//        }
        
        int numbits = Integer.parseInt(JOptionPane.showInputDialog("Please Enter the Number of bits you wanna quantize to :  ") );
        String Compressed = "" ;

         vector sum = new vector() ;
        
         for ( int i = 0 ; i < Pic.size() ; i++) {
             sum.x += Pic.get(i).x  ;
             sum.y += Pic.get(i).y  ;
        }
        
         vector firstAvg = new vector();
         firstAvg.x = Math.round(sum.x / Pic.size()) ;
         firstAvg.y = Math.round(sum.y / Pic.size()) ;
        // System.out.println("First Avg : " + firstAvg.x + "," + firstAvg.y );

        ArrayList <Level> lvl = new ArrayList<>();
        lvl.add(new Level());
        lvl.get(0).avgs.add(new avg(firstAvg.x, firstAvg.y));
        lvl.get(0).avgs.get(0).elements = Pic ;    
       // System.out.println("Elements : " );
        for ( int h = 0 ; h < lvl.get(0).avgs.get(0).elements.size() ; h++) {
         //   System.out.println(lvl.get(0).avgs.get(0).elements.get(h).x + " " + lvl.get(0).avgs.get(0).elements.get(h).y + " " );
        }
        for ( int i = 1 ; i <= numbits ; i++) {
            lvl.add(new Level());
            int c = 0 ;
            for ( int j = 0 ; j < lvl.get(i-1).avgs.size() ; j++) {
                vector  right = new vector ()  ;
                vector  left = new vector ()  ;
                ArrayList <vector> leftElements = new ArrayList<>();
                ArrayList <vector> rightElements = new ArrayList<>();
                left.x =  lvl.get(i-1).avgs.get(j).x - 1 ;
                left.y =  lvl.get(i-1).avgs.get(j).y - 1 ;
                right.x = lvl.get(i-1).avgs.get(j).x + 1 ;
                right.y = lvl.get(i-1).avgs.get(j).y + 1 ;
              
                
                vector sum1 = new vector() ;
                vector sum2 = new vector() ;
                for ( int k = 0 ; k < lvl.get(i-1).avgs.get(j).elements.size() ; k++) {
                    float change1 = 0 ,  change2 = 0;
                    change1 = (lvl.get(i-1).avgs.get(j).elements.get(k).x - left.x) * (lvl.get(i-1).avgs.get(j).elements.get(k).x - left.x ) 
                            + (lvl.get(i-1).avgs.get(j).elements.get(k).y - left.y) * (lvl.get(i-1).avgs.get(j).elements.get(k).y - left.y ) ;
                    change2 = (lvl.get(i-1).avgs.get(j).elements.get(k).x - right.x) * (lvl.get(i-1).avgs.get(j).elements.get(k).x - right.x ) 
                            + (lvl.get(i-1).avgs.get(j).elements.get(k).y - right.y) * (lvl.get(i-1).avgs.get(j).elements.get(k).y - right.y ) ;

                    if (  change1 >= change2 ) {
                        rightElements.add(lvl.get(i-1).avgs.get(j).elements.get(k));
                    }
                    
                    else  leftElements.add(lvl.get(i-1).avgs.get(j).elements.get(k));       
                }
               
                for ( int z = 0 ; z < leftElements.size() ; z++) {
                    sum1.x += leftElements.get(z).x ;
                    sum1.y += leftElements.get(z).y ;
                }
                
                for ( int z = 0 ; z < rightElements.size() ; z++) {
                    sum2.x += rightElements.get(z).x ;
                    sum2.y += rightElements.get(z).y ;
                }
                lvl.get(i).avgs.add(new avg(Math.round(sum1.x/leftElements.size() ), Math.round(sum1.y/leftElements.size()) ));
               //  System.out.println("left avg : " + Math.round(sum1.x/leftElements.size() ) + " " + Math.round(sum1.y/leftElements.size())  );
                lvl.get(i).avgs.get(c).elements = leftElements ;
                for ( int h = 0 ; h < lvl.get(i).avgs.get(c).elements.size() ; h ++) {
                 //   System.out.println(lvl.get(i).avgs.get(c).elements.get(h).x + " " + lvl.get(i).avgs.get(c).elements.get(h).y + " ") ;
                }
                c++ ;
                lvl.get(i).avgs.add(new avg(Math.round(sum2.x/rightElements.size()) ,Math.round(sum2.y/rightElements.size()) ));
                //System.out.println("right avg : " + Math.round(sum2.x/rightElements.size()) + " " + Math.round(sum2.y/rightElements.size())  );
                lvl.get(i).avgs.get(c).elements = rightElements ;
                for ( int h = 0 ; h < lvl.get(i).avgs.get(c).elements.size() ; h ++) {
                //    System.out.println(lvl.get(i).avgs.get(c).elements.get(h).x + " " + lvl.get(i).avgs.get(c).elements.get(h).y + " ") ;
                }
                c++ ;
            }
            
            int curLevel = i ;
            if ( i == numbits) {
                System.out.println("Finished , Starting Optimizing Now ! ");
                optimize : for (;;) {
                    
                    
                lvl.add(new Level());
                curLevel++;
                System.out.println("Added a level with new averages ");
                for ( int l = 0 ; l < lvl.get(curLevel - 1).avgs.size() ; l++) {
                    avg summ = new avg ();
                    for ( int b = 0 ;b < lvl.get(curLevel - 1).avgs.get(l).elements.size() ; b++) {
                        summ.x += lvl.get(curLevel - 1).avgs.get(l).elements.get(b).x ;
                        summ.y += lvl.get(curLevel - 1).avgs.get(l).elements.get(b).y ;
                    }
                    lvl.get(curLevel).avgs.add(new avg(Math.round(summ.x /lvl.get(curLevel - 1).avgs.get(l).elements.size()) , Math.round(summ.y / lvl.get(curLevel - 1).avgs.get(l).elements.size()) ));
                }
                System.out.println("Putting vectors ... ");
                float changee = 0 ; 
                int bestError = 0  ;
                for ( int b = 0 ; b < Pic.size() ; b++) {
                for ( int l = 0 ; l < lvl.get(curLevel).avgs.size() ; l++) {
                   if ( l == 0) {
                       changee = (Pic.get(b).x - lvl.get(curLevel).avgs.get(l).x) * (Pic.get(b).x - lvl.get(curLevel).avgs.get(l).x) 
                               + (Pic.get(b).y - lvl.get(curLevel).avgs.get(l).y) * (Pic.get(b).y - lvl.get(curLevel).avgs.get(l).y);
                       bestError = 0 ;
                   }
                   else {
                       if (changee > ((Pic.get(b).x - lvl.get(curLevel).avgs.get(l).x) * (Pic.get(b).x - lvl.get(curLevel).avgs.get(l).x) 
                                     +(Pic.get(b).y - lvl.get(curLevel).avgs.get(l).y) * (Pic.get(b).y - lvl.get(curLevel).avgs.get(l).y) ) ) {
                           
                           changee = ((Pic.get(b).x - lvl.get(curLevel).avgs.get(l).x) * (Pic.get(b).x - lvl.get(curLevel).avgs.get(l).x) 
                                     +(Pic.get(b).y - lvl.get(curLevel).avgs.get(l).y) * (Pic.get(b).y - lvl.get(curLevel).avgs.get(l).y) );
                           bestError = l ;
                       }
                   }
                }
                lvl.get(curLevel).avgs.get(bestError).elements.add(Pic.get(b));
                }
                //System.out.println("Checking if they are the same .. ");
                boolean same = true ;
                 for ( int u = 0 ; u <lvl.get(curLevel).avgs.size() ; u++ ){
                    if (lvl.get(curLevel).avgs.get(u).x == lvl.get(curLevel-1).avgs.get(u).x && lvl.get(curLevel).avgs.get(u).y == lvl.get(curLevel-1).avgs.get(u).y) { 
                    }
                    else {
                        same = false ;
                       // System.out.println("Optimization not done ! ");
                    }
                }
                
                if (same) {
                    break optimize ;
                }
                }
                
                PrintStream ps1 = new PrintStream("D:\\dictionary.txt");
                ps1.println(width + " " + height);
                for ( int g = 0 ; g < lvl.get(lvl.size()-1).avgs.size() ; g++){
                    ps1.println( (int) lvl.get(lvl.size()-1).avgs.get(g).x + " " + (int) lvl.get(lvl.size()-1).avgs.get(g).y );
                }
                ps1.close();
                
                PrintStream ps2 = new PrintStream("D:\\codeBook.txt");
                
                for ( int m = 0 ; m < Pic.size() ; m++) {
                for ( int g = 0 ; g < lvl.get(lvl.size()-1).avgs.size() ; g++){
                    if ( lvl.get(lvl.size()-1).avgs.get((int)g).elements.contains(Pic.get(m))  ) {
                        ps2.print( g + " ");
                    }
                    
                }
                }
                ps2.close();

            }
           
        }
        

          }
        //////////////////////////////////////////////////////////////////////////////////
        else if (response == 1){
                    
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("dictionary.txt", "txt");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null) ;
        
        File file = chooser.getSelectedFile();
        
        
        String dic = "";    
        try {
            dic = new Scanner( file ).useDelimiter("\\Z").next();
        } 
        catch (FileNotFoundException ex) {
           JOptionPane.showMessageDialog(null, "Couldn't Find The File ! ");
        }
        String dicSplitted[] = dic.split("\\r?\\n");
        String sizeSplitted[] = dicSplitted[0].split("\\s+") ;
          JFileChooser chooser2 = new JFileChooser();
          int height , width ; 
          width = Integer.parseInt(sizeSplitted[0]) ; 
          height =  Integer.parseInt(sizeSplitted[1]) ; 
          
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("codeBook.txt", "txt");
        chooser2.setFileFilter(filter2);
        chooser2.showOpenDialog(null) ;
        
        File file2 = chooser2.getSelectedFile();
        String codeBook = "" ;
        codeBook = new Scanner( file2 ).useDelimiter("\\Z").next();
        Scanner ss = new Scanner( codeBook ) ;
        int[][] pixels = new int [height] [width] ;
        for(int x=0;x<width ;x++){
            for(int y=0;y<height;y+= 2) {
            int vec = ss.nextInt();
            String vectorCode [] = dicSplitted[vec +1].split("\\s+") ;
            pixels[y][x] = Integer.parseInt(vectorCode[0]) ;
            pixels[y+1][x] = Integer.parseInt(vectorCode[1]) ;
            }
        }
        
//        for(int x=0;x<width;x++) {
//        for(int y=0;y<height;y++) { 
//            System.out.print(pixels[y][x] + " ");
//        }
//        }
        
        writeImage(pixels, height, width, "D:\\acQuantized.jpg");
            
       
    }
    
    
    
}
     public static void writeImage(int[][] pixels,int height,int width,String outputFilePath) {

            File fileout=new File(outputFilePath);

            BufferedImage image2=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB );


            for(int x=0;x<width ;x++)
            for(int y=0;y<height;y++) {
            image2.setRGB(x,y,(pixels[y][x]<<16)|(pixels[y][x]<<8)|(pixels[y][x]));
            }

            try {
            ImageIO.write(image2, "jpg", fileout);
            } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            
      
               }

}
