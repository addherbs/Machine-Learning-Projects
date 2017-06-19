

import java.io.IOException;
import java.text.DecimalFormat;


public class Task2 {
final static int MAX = 3100;
final static int MAX1 = 10000;
    
 public static void main(String[] args) throws IOException {
            
     double probability=0;
     int count1 =0, count2 = 0,  count3 = 0, count4 =0 , count5 =0;
     
     
     for(int i=0; i< MAX1 ; i++){
         probability = simulation();
         
         if(probability < 0.08)
             count1++;
         else
             if(probability < 0.09)
                 count2++;
             else
                 if(probability >= 0.09 && probability <= 0.11)
                 count3++;
                 else
                     if(probability > 0.11)
                 count4++;
                 else
                     if(probability > 0.12)
                 count5++;
         
     }
     
     System.out.println("In "+ count1 + " of the simulations p(c = 'a') < 0.08.");
     System.out.println("In "+ count2 + " of the simulations p(c = 'a') < 0.09.");
     System.out.println("In "+ count3 + " of the simulations p(c = 'a') is in the interval [0.09, 0.11].");
     System.out.println("In "+ count4 + " of the simulations p(c = 'a') > 0.11.");
     System.out.println("In "+ count5 + " of the simulations p(c = 'a') > 0.12.");

        }

 static double simulation(){
     String main_string="";
            
            double temporary_number ;
            int temporary_number1;
            int count = 0;
            
            for(int i=0;i < MAX ; i++){
                
            temporary_number = Math.random();     
            temporary_number1 = (int)(temporary_number*100);  
            
            if(temporary_number1 <= 10){
                main_string = main_string + "a";
                count++;
            }
            else
                main_string = main_string + "b";
                       
            }
           
            //System.out.println("a= " + count );
            double  probability = (double)count / main_string.length() ;
           // System.out.println("p(c = 'a') = " +  new DecimalFormat("#0.0000").format(probability));
            
            return probability;
     
 }
 
}
