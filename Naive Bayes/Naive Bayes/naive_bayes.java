import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
@SuppressWarnings("unchecked")
public class naive_bayes {

    final double pi = Math.PI;

    public static void main(String[] args) throws IOException {

	
		
        final int MAX_CLASS = 20;
        final int MAX_DIMENSION = 40;

        int classesCount[] = new int[MAX_CLASS];

        double mean[][] = new double[MAX_CLASS][MAX_DIMENSION];
        double leastOfAll[][] = new double[MAX_CLASS][MAX_DIMENSION];
        double highestOfAll[][] = new double[MAX_CLASS][MAX_DIMENSION];
        double G[][] = new double[MAX_CLASS][MAX_DIMENSION];

        for (int j = 0; j < MAX_CLASS; j++) {

            classesCount[j] = 0;
        }

        for (int i = 0; i < MAX_CLASS; i++) {
            for (int j = 0; j < MAX_DIMENSION; j++) {
                mean[i][j] = 0.0;
                highestOfAll[i][j] = 0.0;
                leastOfAll[i][j] = 50000.0;
                G[i][j] = 0.0;
            }
        }

        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader newBuffer = new BufferedReader(is);

        String ip = newBuffer.readLine();
        String[] inputText = ip.split("\\s+");

        String line;
        String trainingFile = inputText[0];
        String testingFile = inputText[1];
        String typeFile = inputText[2];
        //String trainingFile = inputText[0];

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (typeFile.equals("histograms")) {

            int numberOfBins = 0;
            numberOfBins = Integer.parseInt(inputText[3]);
            //.println(numberOfBins);

//      System.out.println(inputText[0] + " " + inputText[1] + " " + inputText[2]) ;
            URL path = naive_bayes.class.getResource(trainingFile);
            File f = new File(path.getFile());
            BufferedReader br = new BufferedReader(new FileReader(f));

//      BufferedReader br = new BufferedReader(new FileReader("\\yeast.txt"));
            BufferedReader newBr = new BufferedReader(new FileReader(f));

            line = br.readLine();
            String[] splited = line.split("\\s+");
            int classIndexVariable = splited.length - 1;
            int maximumClassNumber = 0;

            int flag = 0;
            if (splited[0].isEmpty()) {

                flag = 1;
                String newString = line;
                newString = newString.trim();
                splited = newString.split("\\s+");
                classIndexVariable = splited.length - 1;
            }

            while (line != null) {

                if (flag == 1) {
                    line = line.trim();
                }

                splited = line.split("\\s+");

                int currentClass = Integer.parseInt(splited[classIndexVariable]);

                if (currentClass > maximumClassNumber) {
                    maximumClassNumber = currentClass;
                }

                // System.out.println(splited[0] + " " + splited[1]);
                for (int i = 0; i < classIndexVariable; i++) {

                    double first = Double.parseDouble(splited[i]);

                    if (highestOfAll[currentClass][i] < first) {
                        highestOfAll[currentClass][i] = first;
                    }

                    if (leastOfAll[currentClass][i] > first) {
                        leastOfAll[currentClass][i] = first;
                    }

                }

                classesCount[currentClass]++;
                line = br.readLine();
            }

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {

                    G[p][q] = (highestOfAll[p][q] - leastOfAll[p][q]) / (numberOfBins - 3);

                    if (G[p][q] < 0.0001) {
                        G[p][q] = 0.0001;
                    }
                }
            }

            double lowRangeOfEach[][][] = new double[maximumClassNumber + 1][classIndexVariable][numberOfBins];
            double maxRangeOfEach[][][] = new double[maximumClassNumber + 1][classIndexVariable][numberOfBins];
            int countOfEach[][][] = new int[maximumClassNumber + 1][classIndexVariable][numberOfBins];
            double probabilityHist[][][] = new double[maximumClassNumber + 1][classIndexVariable][numberOfBins];

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {
                    for (int r = 0; r < numberOfBins; r++) {
                        if (r == 0) {
                            //System.out.println(" p = , q = , r= "+ p + " " + q+ " " + r );
                            lowRangeOfEach[p][q][r] = -500000;
                            maxRangeOfEach[p][q][r] = leastOfAll[p][q] - G[p][q] / 2;
                            countOfEach[p][q][r] = 0;
                            probabilityHist[p][q][r] = 0.0;
                        } else if (r == numberOfBins - 1) {
                            lowRangeOfEach[p][q][r] = maxRangeOfEach[p][q][r - 1];
                            maxRangeOfEach[p][q][r] = 500000;
                            probabilityHist[p][q][r] = 0.0;
                            countOfEach[p][q][r] = 0;
                        } else {
                            probabilityHist[p][q][r] = 0.0;
                            lowRangeOfEach[p][q][r] = maxRangeOfEach[p][q][r - 1];
                            maxRangeOfEach[p][q][r] = leastOfAll[p][q] + (r - 1) * G[p][q] + G[p][q] / 2;
                            countOfEach[p][q][r] = 0;
                        }

                    }

                }
            }

            String newLine;

            int cutecount = 0;
            while ((newLine = newBr.readLine()) != null) {
                cutecount++;

                if (flag == 1) {
                    newLine = newLine.trim();
                }

                splited = newLine.split("\\s+");
                int currentClass = Integer.parseInt(splited[classIndexVariable]);

                for (int i = 0; i < classIndexVariable; i++) {
                    for (int j = 0; j < numberOfBins; j++) {

                        double first = Double.parseDouble(splited[i]);

                        if (j == 0) {
                            if (lowRangeOfEach[currentClass][i][j] < first && first < maxRangeOfEach[currentClass][i][j]) {
                                countOfEach[currentClass][i][j]++;
                            }
                        } else if (j == numberOfBins - 1) {
                            if (lowRangeOfEach[currentClass][i][j] <= first && first < maxRangeOfEach[currentClass][i][j]) {
                                countOfEach[currentClass][i][j]++;
                            }
                        } else if (lowRangeOfEach[currentClass][i][j] <= first && first < maxRangeOfEach[currentClass][i][j]) {
                            countOfEach[currentClass][i][j]++;
                        }

                    }
                }

            }

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {
                    for (int r = 0; r < numberOfBins; r++) {

                        if (classesCount[p] != 0) {
                            // System.out.printf("bincount %d, classcount %d, G= %.2f\n", countOfEach[p][q][r], classesCount[p], G[p][q]);
                            probabilityHist[p][q][r] = countOfEach[p][q][r] / (classesCount[p] * G[p][q]);
                            System.out.printf("Class %d, attribute %d, bin %d, P(bin | class) = %.2f\n", p, q, r, probabilityHist[p][q][r]);
                        }
                    }
                }
            }

            int finalClassCount = 0;
            for (int i = 1; i <= maximumClassNumber; i++) {
                //      System.out.printf(" classnumber %d , current count %d\n", i, classesCount[i]);
                finalClassCount = finalClassCount + classesCount[i];
            }
            //  System.out.printf("\n\n\n\nClass count %d \n\n\n", finalClassCount);

            URL path1 = naive_bayes.class.getResource(testingFile);
            File f1 = new File(path1.getFile());
            BufferedReader br1 = new BufferedReader(new FileReader(f1));

            line = br1.readLine();
            splited = line.split("\\s+");

            flag = 0;
            if (splited[0].isEmpty()) {

                flag = 1;
                String newString = line;
                newString = newString.trim();
                splited = newString.split("\\s+");
                classIndexVariable = splited.length - 1;
            }

            int objectID = 0;
            double accuracy[] = new double[finalClassCount + 1];
            double eachClassProbability[] = new double[maximumClassNumber + 1];
            double sumEachClassProbability = 0;
            double finalEachClassProbability[] = new double[maximumClassNumber + 1];

            int temp = 0;
            while (line != null) {

                if (flag == 1) {
                    line = line.trim();
                }

                splited = line.split("\\s+");

                int currentClass = Integer.parseInt(splited[classIndexVariable]);
                for (int i = 1; i <= maximumClassNumber; i++) {
                    eachClassProbability[i] = (double) classesCount[i] / finalClassCount;
                    finalEachClassProbability[i] = 0;

                }

                double currentClassProbability;
                currentClassProbability = (double) classesCount[currentClass] / finalClassCount;

                for (int i = 1; i <= maximumClassNumber; i++) {
                    for (int j = 0; j < classIndexVariable; j++) {
                        double first = Double.parseDouble(splited[j]);
                        int currentBinIndex = 0;
                        for (int k = 0; k < numberOfBins; k++) {

                            if (k == 0) {
                                if (lowRangeOfEach[i][j][k] < first && first < maxRangeOfEach[i][j][k]) {
                                    currentBinIndex = k;
                                    break;
                                }
                            } else if (k == numberOfBins - 1) {
                                if (lowRangeOfEach[i][j][k] <= first && first < maxRangeOfEach[i][j][k]) {
                                    currentBinIndex = k;
                                    break;
                                }
                            } else if (lowRangeOfEach[i][j][k] <= first && first < maxRangeOfEach[i][j][k]) {
                                currentBinIndex = k;
                                break;
                            }
                        }

                        eachClassProbability[i] *= probabilityHist[i][j][currentBinIndex];

                    }
                    sumEachClassProbability += eachClassProbability[i];
                }

                //
                //
                //
                for (int i = 1; i <= maximumClassNumber; i++) {
                    finalEachClassProbability[i] = eachClassProbability[i] / sumEachClassProbability;
                }

                double maxValue = 0;
                int maxIndex = 0;
                for (int i = 0; i < finalEachClassProbability.length; i++) {
                    double newNumber = finalEachClassProbability[i];
                    if ((newNumber > finalEachClassProbability[maxIndex])) {
                        maxIndex = i;
                        maxValue = newNumber;
                    }
                }
                ArrayList<Integer> newIndexList = new ArrayList();
                for (int i = 0; i < finalEachClassProbability.length; i++) {
                    double newNumber = finalEachClassProbability[i];
                    if ((newNumber == maxValue)) {
                        newIndexList.add(i);
                    }
                }

                if (newIndexList.size() == 1 && currentClass == maxIndex) {
                    accuracy[objectID] = 1;
                } else if (newIndexList.contains(currentClass)) {
                    accuracy[objectID] = 1 / newIndexList.size();
                } else {
                    accuracy[objectID] = 0;
                }

                System.out.printf("ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n",
                        objectID, maxIndex, finalEachClassProbability[maxIndex], currentClass, accuracy[objectID]);

                newIndexList.clear();
                objectID++;
                line = br1.readLine();
            }

            double finalAccuracy = 0;
            for (int i = 0; i < objectID; i++) {
                finalAccuracy += accuracy[i];
            }
            finalAccuracy /= objectID;

            //  System.out.printf("ObjID = %d \n", objectID);
            System.out.printf("classification accuracy=%6.4f\n", finalAccuracy);

        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // When the 3rd input is Gaussians
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (typeFile.equals("gaussians")) {

            //  System.out.println(trainingFile + " " + testingFile);
            URL path = naive_bayes.class.getResource(trainingFile);
            File f = new File(path.getFile());
            BufferedReader br = new BufferedReader(new FileReader(f));
//          BufferedReader br = new BufferedReader(new FileReader("\\yeast.txt"));
            BufferedReader newBr = new BufferedReader(new FileReader(f));

            line = br.readLine();
            String[] splited = line.split("\\s+");
            int classIndexVariable = splited.length - 1;
            int maximumClassNumber = 1;

            int flag = 0;
            if (splited[0].isEmpty()) {
                flag = 1;
                String newString = line;
                newString = newString.trim();
                splited = newString.split("\\s+");
                classIndexVariable = splited.length - 1;
            }
            
            while (line != null) {

                if (flag == 1) {
                    line = line.trim();
                }

                splited = line.split("\\s+");
                int currentClass = Integer.parseInt(splited[classIndexVariable]);

                if (currentClass > maximumClassNumber) {
                    maximumClassNumber = currentClass;
                }

                // System.out.println(splited[0] + " " + splited[1]);
                for (int i = 0; i < classIndexVariable; i++) {

                    double first = Double.parseDouble(splited[i]);
                    mean[currentClass][i] = mean[currentClass][i] + first;
                }

                classesCount[currentClass]++;
                line = br.readLine();
            }

            double finalmean[][] = new double[maximumClassNumber + 1][classIndexVariable + 1];

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {
                    finalmean[p][q] = 0;
                }
            }

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {

                    finalmean[p][q] = mean[p][q] / classesCount[p];
                }
            }

            String newLine;

            double varianceDimension[][] = new double[maximumClassNumber + 2][MAX_DIMENSION];
            double StandardDiviationDimension[][] = new double[maximumClassNumber + 2][MAX_DIMENSION];

            for (int i = 1; i <= maximumClassNumber + 1; i++) {
                for (int j = 0; j < MAX_DIMENSION; j++) {
                    varianceDimension[i][j] = 0.0;
                }
            }

            while ((newLine = newBr.readLine()) != null) {

                if (flag == 1) {
                    newLine = newLine.trim();
                }

                splited = newLine.split("\\s+");
                int currentClass = Integer.parseInt(splited[classIndexVariable]);

                //System.out.println(splited[1]);
                for (int j = 0; j < classIndexVariable; j++) {

                    double first = Double.parseDouble(splited[j]);
                    double newfirst = first - finalmean[currentClass][j];
                    varianceDimension[currentClass][j] = varianceDimension[currentClass][j] + (newfirst * newfirst);

                }

            }

            for (int i = 1; i <= maximumClassNumber; i++) {
                for (int j = 0; j < classIndexVariable; j++) {

                    varianceDimension[i][j] = varianceDimension[i][j] / classesCount[i];

                    if (varianceDimension[i][j] < 0.0001) {
                        varianceDimension[i][j] = 0.0001;
                    }

                    StandardDiviationDimension[i][j] = Math.sqrt(varianceDimension[i][j]);

                }
            }

            for (int i = 1; i <= maximumClassNumber; i++) {
                for (int j = 0; j < classIndexVariable; j++) {

                    System.out.printf("Class %d, attribute %d, mean = %.2f, std = %.2f\n", i, j, finalmean[i][j], StandardDiviationDimension[i][j]);

                }
            }

            int finalClassCount = 0;
            for (int i = 1; i <= maximumClassNumber; i++) {
                // System.out.printf(" classnumber %d , current count %d\n", i, classesCount[i]);
                finalClassCount = finalClassCount + classesCount[i];
            }
            //System.out.printf("\n\n\n\nClass count %d \n\n\n", finalClassCount);

            URL path1 = naive_bayes.class.getResource(testingFile);
            File f1 = new File(path1.getFile());
            BufferedReader br1 = new BufferedReader(new FileReader(f1));

            line = br1.readLine();
            splited = line.split("\\s+");

            flag = 0;
            if (splited[0].isEmpty()) {

                flag = 1;
                String newString = line;
                newString = newString.trim();
                splited = newString.split("\\s+");
                classIndexVariable = splited.length - 1;
            }
            double variable1;
            int objectID = 0;
            double accuracy[] = new double[finalClassCount + 1];

            double finalGaussianPredict[][] = new double[maximumClassNumber][5];

            for (int p = 0; p < maximumClassNumber; p++) {
                for (int q = 0; q < 5; q++) {

                    finalGaussianPredict[p][q] = 0.0;
                }
            }

            double eachClassProbability[] = new double[maximumClassNumber + 1];
            double sumEachClassProbability = 0;
            double finalEachClassProbability[] = new double[maximumClassNumber + 1];

            while (line != null) {

                if (flag == 1) {
                    line = line.trim();
                }

                splited = line.split("\\s+");

                int currentClass = Integer.parseInt(splited[classIndexVariable]);
                for (int i = 1; i <= maximumClassNumber; i++) {
                    eachClassProbability[i] = (double) classesCount[i] / finalClassCount;

                    finalEachClassProbability[i] = 0;

                }

                double currentClassProbability = (double) classesCount[currentClass] / finalClassCount;

                for (int i = 1; i <= maximumClassNumber; i++) {
                    for (int j = 0; j < classIndexVariable; j++) {

                        double first = Double.parseDouble(splited[j]);
                        double firstTerm = 1 / Math.sqrt(2 * Math.PI * varianceDimension[i][j]);
                        double secondTerm = Math.exp((-1) * (first - finalmean[i][j]) * (first - finalmean[i][j]) / (2 * varianceDimension[i][j]));

                        eachClassProbability[i] = eachClassProbability[i] * firstTerm * secondTerm;

                    }
                    sumEachClassProbability += eachClassProbability[i];
                }

                for (int i = 1; i <= maximumClassNumber; i++) {
                    finalEachClassProbability[i] = eachClassProbability[i] / sumEachClassProbability;
                }

                double maxValue = 0;
                int maxIndex = 0;
                for (int i = 1; i < finalEachClassProbability.length; i++) {
                    double newNumber = finalEachClassProbability[i];
                    if ((newNumber > finalEachClassProbability[maxIndex])) {
                        maxIndex = i;
                        maxValue = newNumber;
                    }
                }
                ArrayList<Integer> newIndexList = new ArrayList();
                for (int i = 1; i < finalEachClassProbability.length; i++) {
                    double newNumber = finalEachClassProbability[i];
                    if ((newNumber == maxValue)) {
                        newIndexList.add(i);
                    }
                }

                if (newIndexList.size() == 1 && currentClass == maxIndex) {
                    accuracy[objectID] = 1;
                } else if (newIndexList.contains(currentClass)) {
                    accuracy[objectID] = 1 / newIndexList.size();
                } else {
                    accuracy[objectID] = 0;
                }

                System.out.printf("ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n",
                        objectID, maxIndex, finalEachClassProbability[maxIndex], currentClass, accuracy[objectID]);

                newIndexList.clear();
                objectID++;
                line = br1.readLine();
            }

            double finalAccuracy = 0;
            for (int i = 0; i < objectID; i++) {
                finalAccuracy += accuracy[i];
            }
            finalAccuracy /= objectID;

            // System.out.printf("ObjID = %d \n", objectID);
            System.out.printf("classification accuracy=%6.4f\n", finalAccuracy);

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // When the 3rd input is mistures
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (typeFile.equals("mixtures")) {

            int numberOfGaussians = Integer.parseInt(inputText[3]);;

//      System.out.println(inputText[0] + " " + inputText[1] + " " + inputText[2]) ;
            URL path = naive_bayes.class.getResource(trainingFile);
            File f = new File(path.getFile());
            BufferedReader newBr1 = new BufferedReader(new FileReader(f));
            BufferedReader br = new BufferedReader(new FileReader(f));

//      BufferedReader br = new BufferedReader(new FileReader("\\yeast.txt"));
            BufferedReader newBr = new BufferedReader(new FileReader(f));

            line = br.readLine();
            String[] splited = line.split("\\s+");
            int classIndexVariable = splited.length - 1;
            int maximumClassNumber = 0;

            int flag = 0;
            if (splited[0].isEmpty()) {

                flag = 1;
                String newString = line;
                newString = newString.trim();
                splited = newString.split("\\s+");
                classIndexVariable = splited.length - 1;
            }

            double mixtureTrainMatrix[][] = new double[20000][classIndexVariable + 1];
            for (int p = 1; p <= 15; p++) {
                for (int q = 0; q < classIndexVariable; q++) {

                    mixtureTrainMatrix[p][q] = 0;
                }
            }

            int rowCount = 0;
            while (line != null) {
                rowCount++;
                if (flag == 1) {
                    line = line.trim();
                }

                splited = line.split("\\s+");

                int currentClass = Integer.parseInt(splited[classIndexVariable]);

                if (currentClass > maximumClassNumber) {
                    maximumClassNumber = currentClass;
                }

                // System.out.println(splited[0] + " " + splited[1]);
                int i = 0;
                for (; i < classIndexVariable; i++) {

                    double first = Double.parseDouble(splited[i]);

                    if (highestOfAll[currentClass][i] < first) {
                        highestOfAll[currentClass][i] = first;
                    }

                    if (leastOfAll[currentClass][i] > first) {
                        leastOfAll[currentClass][i] = first;
                    }

                    mixtureTrainMatrix[rowCount][i] = first;

                }

                mixtureTrainMatrix[rowCount][i] = Integer.parseInt(splited[i]);

                classesCount[currentClass]++;
                line = br.readLine();
            }

//            for (int p = 1; p <= rowCount; p++) {
//                for (int q = 0; q <= classIndexVariable; q++) {
//
//                    System.out.print(mixtureTrainMatrix[p][q] + "       ");
//
//                }
//                System.out.println();
//            }
            //System.out.println(rowCount);
            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {

                    G[p][q] = (highestOfAll[p][q] - leastOfAll[p][q]) / numberOfGaussians;
                    // System.out.println(G[p][q]);
                }
            }

            int countOfEach[][][] = new int[maximumClassNumber + 1][classIndexVariable][numberOfGaussians];
            double probabilityHist[][][] = new double[maximumClassNumber + 1][classIndexVariable][numberOfGaussians];
            double[][][] meanGaussian = new double[maximumClassNumber + 1][classIndexVariable + 1][numberOfGaussians];
            double[][][] stdGaussian = new double[maximumClassNumber + 1][classIndexVariable][numberOfGaussians];
            double[][][] weightsGaussian = new double[maximumClassNumber + 1][classIndexVariable][numberOfGaussians];
            double Nij[][][] = new double[rowCount + 1][classIndexVariable][numberOfGaussians];
            double Pij[][][] = new double[rowCount + 1][classIndexVariable][numberOfGaussians];
            double numerator[][][] = new double[rowCount + 1][classIndexVariable][numberOfGaussians];

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {
                    for (int r = 0; r < numberOfGaussians; r++) {

                        meanGaussian[p][q][r] = leastOfAll[p][q] + r * G[p][q] + G[p][q] / 2;
                        stdGaussian[p][q][r] = 1;
                        weightsGaussian[p][q][r] = 1.0 / numberOfGaussians;

                    }
                }
            }

            for (int i = 0; i < 50; i++) {

                for (int p = 1; p <= rowCount; p++) {
                    for (int q = 0; q < classIndexVariable; q++) {

                        double sumNum = 0;
                        for (int r = 0; r < numberOfGaussians; r++) {

                            double tempX = mixtureTrainMatrix[p][q];

                            int currentRowClass = (int) mixtureTrainMatrix[p][classIndexVariable];
                            // System.out.println( numberOfGaussians+ " " + currentRowClass+" "+ q +" "+r);
                            double tempMean = meanGaussian[currentRowClass][q][r];
                            double tempVariance = stdGaussian[currentRowClass][q][r] * stdGaussian[currentRowClass][q][r];

                            Nij[p][q][r] = calculateGaussian(tempX, tempMean, tempVariance);
                            numerator[p][q][r] = weightsGaussian[currentRowClass][q][r] * Nij[p][q][r];
                            //System.out.println(weightsGaussian[currentRowClass][q][r]);
                            sumNum += numerator[p][q][r];

                            //if(currentRowClass==1 && q==0 && r==2)
                            // System.out.println("Step : "+ i   +"\t x=" + tempX +"\t Class "+currentRowClass+"\t Init Mean=" + tempMean+" \t NIJ "+Nij[p][q][r]);
                        }

                        for (int r = 0; r < numberOfGaussians; r++) {
                            Pij[p][q][r] = numerator[p][q][r] / sumNum;
                        }

                    }
                }

//            for (int p = 1; p <= rowCount; p++) {
//                for (int q = 0; q < classIndexVariable; q++) {
//                    for (int r = 0; r < numberOfGaussians; r++) {
//
//                        System.out.println(q + " " + r + " " + Pij[p][q][r]);
//                    }
//                }
//            }
                double numerator1;
                double denominator;
                for (int p = 1; p <= maximumClassNumber; p++) {
                    for (int q = 0; q < classIndexVariable; q++) {

                        for (int r = 0; r < numberOfGaussians; r++) {
                            numerator1 = 0;
                            denominator = 0;
                            for (int row = 0; row < rowCount; row++) {
                                if (mixtureTrainMatrix[row][classIndexVariable] == p) {
                                    numerator1 += mixtureTrainMatrix[row][q] * Pij[row][q][r];
                                    denominator += Pij[row][q][r];
//                                   
//                                         if(p==1 && q==0 && r==0)
//                                                System.out.println("Step : "+ i   +"\t x=" + mixtureTrainMatrix[row][q]
//                                                +"\t Pij=" + Pij[p][q][r]);
                                }
                            }
//                            if(p==1 && q==0 && r==0)
//                                                System.out.println("Step : "+ i   +"\tnumerator1=" + numerator1
//                                                +"\t denominator=" + denominator);
                            meanGaussian[p][q][r] = numerator1 / denominator;
//                            if(p==1 && q==0 && r==0)
//                                System.out.println("Step : "+ i   +"  : mean=" + numerator1 / denominator);
                        }

                        denominator = 0;
                        for (int r = 0; r < numberOfGaussians; r++) {
                            numerator1 = 0;
                            denominator = 0;

                            for (int row = 0; row < rowCount; row++) {
                                if (mixtureTrainMatrix[row][classIndexVariable] == p) {
                                    numerator1 += Pij[row][q][r] * sqr(mixtureTrainMatrix[row][q] - meanGaussian[p][q][r]);
                                    denominator += Pij[row][q][r];
                                }
                            }
                            stdGaussian[p][q][r] = Math.sqrt(numerator1 / denominator);
                            if (stdGaussian[p][q][r] < 0.01) {
                                stdGaussian[p][q][r] = 0.01;

                            }
                            //  System.out.print("mean=" + stdGaussian[p][q][r]);

                        }
                        denominator = 0;

                        for (int r = 0; r < numberOfGaussians; r++) {

                            for (int row = 0; row < rowCount; row++) {

                                if (mixtureTrainMatrix[row][classIndexVariable] == p) {
                                    denominator += Pij[row][q][r];
                                    // System.out.println("q = " +denominator);
                                }

                            }

                        }
                        for (int r = 0; r < numberOfGaussians; r++) {
                            numerator1 = 0;
                            for (int row = 0; row < rowCount; row++) {
                                if (mixtureTrainMatrix[row][classIndexVariable] == p) {
                                    numerator1 += Pij[row][q][r];
                                    // System.out.println("p = " +numerator1);
                                }

                            }
                            //System.out.println("p = " + p + " q = " + q+" r = " +r + " num = " + numerator1 + " dem = " +denominator);
                            weightsGaussian[p][q][r] = numerator1 / denominator;
                            //System.out.print("weight = " + weightsGaussian[p][q][r]);
                        }

                    }

                }

//                for (int x = 1; x <= maximumClassNumber; x++) {
//                for (int y = 0; y< classIndexVariable; y++) {
//                    for (int z = 0; z < numberOfGaussians; z++) {
//
//                        //   System.out.print(meanGaussian[p][q][r] + " " + stdGaussian[p][q][r]);
//                        System.out.printf("Step %d Class %d, attribute %d, Gaussian %d, mean = %.2f, std = %.2f, wieght %f \n", i, x, y, z,meanGaussian[x][y][z],
//                                stdGaussian[x][y][z],weightsGaussian[x][y][z]);
//
//                            }
//
//                        }
//
//                    }
            }

            for (int p = 1; p <= maximumClassNumber; p++) {
                for (int q = 0; q < classIndexVariable; q++) {
                    for (int r = 0; r < numberOfGaussians; r++) {
                        if (classesCount[p] != 0) //   System.out.print(meanGaussian[p][q][r] + " " + stdGaussian[p][q][r]);
                        {
                            System.out.printf("Class %d, attribute %d, Gaussian %d, mean = %.2f, std = %.2f\n", p, q, r, meanGaussian[p][q][r],
                                    stdGaussian[p][q][r]);
                        }

                    }

                }

            }

        }
    }

    private static double sqr(double d) throws IOException {

        return d * d;
    }

    public static double calculateGaussian(double x, double mean, double variance) throws IOException {

        return (Math.exp(-1 * (sqr(x - mean) / (2 * variance)))) / (Math.sqrt(2 * Math.PI * variance));
    }
}
