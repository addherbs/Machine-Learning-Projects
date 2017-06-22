
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;

public class Task4 {

    public static void main(String[] args) throws IOException {
        
        final int MAX_CLASS = 50;
        final int MAX_DIMENSION = 100;
        
       
        int classesCount[] = new int[MAX_CLASS];
       
        double linkArray[][] = new double[MAX_CLASS][MAX_DIMENSION];
        
        double meanTask4[][]= new double[MAX_CLASS][MAX_DIMENSION];
        
        for(int j =0 ; j< MAX_CLASS ; j++){
        	
                 classesCount[j] = 0;
        }
        
       
        
        for(int i =0 ; i< MAX_CLASS ; i++)
            for(int j =0 ; j< MAX_DIMENSION ; j++){
        	 linkArray[i][j] = 0.0;
                  meanTask4[i][j] = 0.0;
            }
        
        
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader newBuffer = new BufferedReader(is);
        
        String ip = newBuffer.readLine();
        String[] inputText = ip.split("\\s+");
        String line;
        String filename = inputText[0];
        
  
        
        URL path = Task4.class.getResource(filename);
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
               
          // System.out.println(splited[0] + " " + splited[1]);
          
          for(int i=0;i<classIndexVariable;i++){
                
                double first = Double.parseDouble(splited[i]);
                meanTask4[currentClass][i] = meanTask4[currentClass][i] + first;
            }

            classesCount[currentClass]++;
            line = br.readLine();
        }
    
        double finalmean[][] = new double[maximumClassNumber+1][classIndexVariable+1];
    
        for(int p =1 ; p<= maximumClassNumber ; p++)
            for(int q=0;q<classIndexVariable; q++)
                finalmean[p][q]= 0;
                   
         
        for(int p =1 ; p<= maximumClassNumber ; p++)
            for(int q=0;q<classIndexVariable; q++){
            
        	 finalmean[p][q] = meanTask4[p][q] / classesCount[p] ;
            }
        
//        for(int i =1; i<=10 ; i++)
//            {
//            System.out.print("count[" +i+"] = " + classesCount[i]);
//            System.out.print(" mean[" +i+"] = " + finalmean[i][0] );
//            System.out.println(" mean[" +i+"] = " +finalmean[i][1]);
//            }
        
        String newLine;
        
        double varianceDimension[][] = new double[maximumClassNumber+2][MAX_DIMENSION];
        
        for(int i =1 ; i<=maximumClassNumber+1; i++)
            for(int j =0 ; j< MAX_DIMENSION ; j++){
                    varianceDimension[i][j] = 0.0;
                }        
        
        while(  (newLine = newBr.readLine() ) != null){
            
            if(  flag ==1)
               {
                   newLine = newLine.trim();
               }
            
            splited = newLine.split("\\s+");
            int currentClass = Integer.parseInt(splited[classIndexVariable]);
            
            //System.out.println(splited[1]);
            
            for(int j =0 ; j< classIndexVariable ; j++){
                
                    double first = Double.parseDouble(splited[j]);
                    double newfirst = first - finalmean[currentClass][j];
                    varianceDimension[currentClass][j] = varianceDimension[currentClass][j] + ( newfirst * newfirst) ;
                } 
            
        }
        
        for(int i =1 ; i<= maximumClassNumber ; i++)
            for(int j=1; j<= classIndexVariable; j++ ){
             
            System.out.println( "Class " + i + ", dimension "+ j+ ", mean = "+ new DecimalFormat("#0.00").format( finalmean[i][j-1])+
                            ", variance =  "+ new DecimalFormat("#0.00").format(  varianceDimension[i][j-1]/classesCount[i]));
        }
        

    }
    
}
