
import java.net.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


public class Task5 {

    public static void main(String[] args) throws IOException {
        
        final int MAX_CLASS = 50;
        final int MAX_DIMENSION = 100;
        
        
        int classesCount[] = new int[MAX_CLASS];
        
        double linkArray[][] = new double[MAX_CLASS][MAX_DIMENSION];
        
        double meanTask6[][]= new double[MAX_CLASS][MAX_DIMENSION];
        
        for(int j =0 ; j< MAX_CLASS ; j++){
        	 
                 classesCount[j] = 0;
        }
        
       
        
        for(int i =0 ; i< MAX_CLASS ; i++)
            for(int j =0 ; j< MAX_DIMENSION ; j++){
        	 linkArray[i][j] = 0.0;
                  meanTask6[i][j] = 0.0;
            }
        
        
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader newBuffer = new BufferedReader(is);
        
        String ip = newBuffer.readLine();
        String[] inputText = ip.split("\\s+");
        String line;
        String filename = inputText[0];
        
  
        
        URL path = Task5.class.getResource(filename);
        File f = new File(path.getFile());
        BufferedReader br = new BufferedReader(new FileReader(f));
//        BufferedReader br = new BufferedReader(new FileReader("\\yeast.txt"));
        BufferedReader newBr = new BufferedReader(new FileReader(f));
        
        
       
        line = br.readLine();
        String []splited = line.split("\\s+");
        int classIndexVariable = splited.length -1;
        
        int maximumClassNumber =0;
        
       int flag=0;
               if(  splited[0].isEmpty())
               {
                    flag =1;
                String newString = line;  
                newString = newString.trim();
                 splited = newString.split("\\s+");
                 classIndexVariable = splited.length -1;
               }
        
        while(  line != null) {
                
            if(  flag ==1)
               {
                   line = line.trim();
               }
                
               splited = line.split("\\s+");
               int currentClass = Integer.parseInt(splited[classIndexVariable]);
               
               if(currentClass>maximumClassNumber)
                   maximumClassNumber=currentClass;
               
            double first = Double.parseDouble(splited[0]);
            double second =  Double.parseDouble(splited[1]);
                
       
               
            meanTask6[currentClass][0] = meanTask6[currentClass][0] + first;
            meanTask6[currentClass][1] = meanTask6[currentClass][1] + second;
            classesCount[currentClass]++;
         
            line = br.readLine();
        }
     
        double finalmean[][] = new double[MAX_CLASS][2];
                for(int j =0 ; j< MAX_CLASS ; j++){
                    finalmean[j][0] = 0;
                    finalmean[j][1] = 0;
                }
        for(int j =1 ; j<= classIndexVariable ; j++){
            
        	 finalmean[j][0] = meanTask6[j][0] / classesCount[j] ;
                  finalmean[j][1] = meanTask6[j][1] /classesCount[j] ;
        }
        
//        for(int i =1; i<=10 ; i++)
//            {
//            System.out.print("count[" +i+"] = " + classesCount[i]);
//            System.out.print(" mean[" +i+"] = " + finalmean[i][0] );
//            System.out.println(" mean[" +i+"] = " +finalmean[i][1]);
//            }
        
        String newLine;
        
        double varianceX[] = new double[MAX_CLASS];
        double varianceY[] = new double[MAX_CLASS];
        double coVarianceXY[] = new double[MAX_CLASS];
        for(int j =0 ; j< MAX_CLASS ; j++){
                    varianceX[j] = 0;
                    varianceY[j] = 0;
                    coVarianceXY[j]=0;
                }        
        
        while(  (newLine = newBr.readLine() ) != null){
            
            if(  flag ==1)
               {
                   newLine = newLine.trim();
               }
            
            newLine = newLine.trim();
            splited = newLine.split("\\s+");
            int currentClass = Integer.parseInt(splited[classIndexVariable]);
            
            
            
            double first = Double.parseDouble(splited[0]);
            double second =  Double.parseDouble(splited[1]);
            
            double newfirst = first - finalmean[currentClass][0];
            double newsecond = second - finalmean[currentClass][1];
            
            varianceX[currentClass] = varianceX[currentClass] + ( newfirst * newfirst) ;
            varianceY[currentClass] = varianceY[currentClass] + ( newsecond * newsecond );
            coVarianceXY[currentClass]=coVarianceXY[currentClass] +  (newfirst * newsecond) ;
            
        }
        
        
        for(int i=1; i<= maximumClassNumber; i++ ){
            if(coVarianceXY[i] != 0.0)
            System.out.println( "Class " + i + ", mean = ["+ new DecimalFormat("#0.00").format( finalmean[i][0]) + "," + 
                    new DecimalFormat("#0.00").format(finalmean[i][1]) + " ], sigma = [" + 
                            new DecimalFormat("#0.00").format( varianceX[i]/classesCount[i]) +
                                            ","+ new DecimalFormat("#0.00").format( coVarianceXY[i]/classesCount[i])  +  ","+ 
                                                    new DecimalFormat("#0.00").format( coVarianceXY[i]/classesCount[i] ) +  ","+ 
                                                            new DecimalFormat("#0.00").format( varianceY[i]/classesCount[i])+ "]");
        }
        
    }
    
}