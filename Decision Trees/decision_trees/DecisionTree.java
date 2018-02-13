
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class DecisionTree {

	private class TreeObject {

        public int objectType;
        double objectInformationGain;

        public int objectID = 0;

        public double objectAttribute;
        public double objectThreshold;

        public int objectLable;
        public TreeObject objectLeftTree;
        public TreeObject objectRightTree;
        ArrayList<Double> objectClassProbabilityDistribution = new ArrayList<Double>();
    }

    /*
    Main method calls the learning and classification phase.
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {

        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader newBuffer = new BufferedReader(is);

        String ip = newBuffer.readLine();   //Reads input
        String[] inputText = ip.split("\\s+");  //splits
        String line;
        //String filename = inputText[0]; 

//        System.out.println(inputText[0] + " " + inputText[1] + " " + inputText[2] + " " + inputText[3]);
        String trainMatrix = inputText[0];
        String testMatrix = inputText[1];
        String typeArgument = inputText[2];
        int pruningThreshold = Integer.parseInt(inputText[3]);

        ArrayList<Double> target = new ArrayList();
        ArrayList<ArrayList<Double>> trainMarix = new ArrayList<ArrayList<Double>>();

        URL path = DecisionTree.class.getResource(trainMatrix);
        File f = new File(path.getFile());
        Scanner input = new Scanner(new FileReader(f));

        ArrayList<Double> minAttr = new ArrayList();
        ArrayList<Double> maxAttr = new ArrayList();

        /*
        Reads input matrix
         */
        while (input.hasNextLine()) {
            Scanner colReader = new Scanner(input.nextLine());
            ArrayList<Double> row = new ArrayList();
            while (colReader.hasNextDouble()) {
                row.add(colReader.nextDouble());
            }
            trainMarix.add(row);
            target.add(row.get(row.size() - 1));
            //row.remove(row.size() - 1);

        }

        DecisionTree obj = new DecisionTree();    //creates object of the class

        ArrayList<Double> currentAttributes = new ArrayList<Double>();
        for (int i = 0; i < 16; i++) {
            currentAttributes.add((double) i);
        }

        ArrayList<TreeObject> decisionTree = new ArrayList<TreeObject>();

        if (typeArgument.equals(
                "optimized") || typeArgument.equals("randomized")) {
            //     final long startTime = System.currentTimeMillis();
            TreeObject node = obj.learningDTL(trainMarix, currentAttributes, null, pruningThreshold, typeArgument);

            ArrayList<TreeObject> nodes = obj.mainBFS(node, 0);

            decisionTree.add(node);
            //    final long endTime = System.currentTimeMillis();
            //    System.out.println("Total execution time: " + (endTime - startTime));
        }

        if (typeArgument.equals(
                "forest3")) {
            final long startTime = System.currentTimeMillis();
            for (int i = 0; i < 3; i++) {
                TreeObject node = obj.learningDTL(trainMarix, currentAttributes, null, pruningThreshold, typeArgument);

                ArrayList<TreeObject> nodes = obj.mainBFS(node, i);

                decisionTree.add(node);

            }
            final long endTime = System.currentTimeMillis();
            //  System.out.println("Total execution time: " + (endTime - startTime));
        }

        if (typeArgument.equals(
                "forest15")) {
            final long startTime = System.currentTimeMillis();
            for (int i = 0; i < 15; i++) {
                //System.out.println("Tree "+i);
                TreeObject node = obj.learningDTL(trainMarix, currentAttributes, null, pruningThreshold, typeArgument);

                ArrayList<TreeObject> nodes = obj.mainBFS(node, i);

                decisionTree.add(node);
            }
            final long endTime = System.currentTimeMillis();
            //    System.out.println("Total execution time: " + (endTime - startTime));
        }

        //Testing
        ArrayList<Double> target_test = new ArrayList();

        ArrayList<ArrayList<Double>> testMarix = new ArrayList<ArrayList<Double>>();

        URL path1 = DecisionTree.class.getResource(testMatrix);
        File f1 = new File(path1.getFile());
        Scanner test = new Scanner(new FileReader(f1));

        //Scanner test = new Scanner(new File("C:\\Users\\Cyther\\Documents\\NetBeansProjects\\DT\\src\\dt\\" + testMatrix));
        while (test.hasNextLine()) {
            Scanner eachColumn = new Scanner(test.nextLine());
            ArrayList<Double> row = new ArrayList<Double>();
            while (eachColumn.hasNextDouble()) {
                row.add(eachColumn.nextDouble());
            }
            testMarix.add(row);
            target_test.add(row.get(row.size() - 1));

        }

        //Classification
        int correct_classification_count = 0;
        int number_of_record = testMarix.size();
        for (int object_id = 0;
                object_id < number_of_record;
                object_id++) {

            ArrayList<ArrayList<Double>> dists = new ArrayList<ArrayList<Double>>();
            double predicted_class;
            double actual_class = target_test.get(object_id);
            double accuracy = 0;

            for (TreeObject node : decisionTree) {
                ArrayList<Double> dist = obj.classification(testMarix.get(object_id), node);
                dists.add(dist);
            }

            if (dists.size() == 1) {
                ArrayList<Double> dist1 = dists.get(0);
                double max = Collections.max(dist1);
                //System.out.println(max);
                ArrayList<Integer> positions = occurance_of_dist(dist1, max);
                //System.out.println(positions);
                int last_index = positions.size() - 1;
                int count = positions.get(last_index);
                positions.remove(last_index);

                if (last_index >= 2) {
                    int r = (int) (Math.random() * 1000) % count;
                    predicted_class = positions.get(r);
                } else {
                    predicted_class = positions.get(0);
                }
            } else {
                int size_dist = dists.size();
                ArrayList<Double> sum_dist = new ArrayList<Double>();

                for (int i = 0; i < 10; i++) {
                    sum_dist.add(0.0);
                }

                for (int i = 0; i < dists.size(); i++) {
                    ArrayList<Double> dist_intr = dists.get(i);

                    for (int j = 0; j < dist_intr.size(); j++) {
                        double sum = sum_dist.get(j);
                        sum = sum + dist_intr.get(j);
                        sum_dist.set(j, sum);
                    }
                }

                for (Double sum : sum_dist) {
                    sum = sum / (double) size_dist;
                }

                double max = Collections.max(sum_dist);
                //System.out.println(max);
                ArrayList<Integer> positions = occurance_of_dist(sum_dist, max);
                //System.out.println(positions);
                int storesCountOfArrayIndex = positions.size() - 1;
                int count = positions.get(storesCountOfArrayIndex);
                positions.remove(storesCountOfArrayIndex);

                if (storesCountOfArrayIndex >= 2) {
                    int r = (int) (Math.random() * 1000) % count;
                    predicted_class = positions.get(r);
                } else {
                    predicted_class = positions.get(0);
                }

            }

            if (Double.compare(predicted_class, actual_class) == 0) {
                accuracy = 1;
                correct_classification_count++;
            }

            System.out.printf("ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f\n", object_id, (int) predicted_class, (int) actual_class, accuracy);
        }

        double classification_accuracy = correct_classification_count / (double) number_of_record;

        System.out.printf(
                "classification accuracy=%6.4f\n", classification_accuracy);

    }

    /*
    Calls the BFS from here which calls recursively to the same node.
     */
    public ArrayList<TreeObject> mainBFS(TreeObject node, int treeNumber) {
        int id = 1;
        ArrayList<TreeObject> ascendingNodesList = new ArrayList<TreeObject>();
        node.objectID = 1;
        ascendingNodesList.add(node);

        BFS(ascendingNodesList, treeNumber);

        return ascendingNodesList;
    }

    /*
    Generates the each level nodes object : tree
     */
    public void BFS(ArrayList<TreeObject> ascendingNodesList, int treeNumber) {

        ArrayList<TreeObject> node_ch = new ArrayList<TreeObject>();

        for (TreeObject node : ascendingNodesList) {
            int feature = (int) node.objectAttribute;
            double threshold = node.objectThreshold;
            if (node.objectType == 1 || node.objectType == 2) {
                feature = -1;
                threshold = -1;
            }
            System.out.printf("Tree=%2d, node=%3d, feature=%2d, thr=%6.2f, gain=%f\n", treeNumber, node.objectID, feature, threshold, node.objectInformationGain);

            if (node.objectType == 0) {
                node.objectLeftTree.objectID = 2 * node.objectID;
                node_ch.add(node.objectLeftTree);
                node.objectRightTree.objectID = 2 * node.objectID + 1;
                node_ch.add(node.objectRightTree);
            }
        }

        if (!node_ch.isEmpty()) {
            BFS(node_ch, treeNumber);
        }

    }

    /*
    selects the attribute column
     */
    public ArrayList<Double> generateAttributeColumn(ArrayList<ArrayList<Double>> currentMatrix, int currentAttribute) {

        ArrayList<Double> attributeValues = new ArrayList<Double>();
        for (int p = 0; p < currentMatrix.size(); p++) {
            attributeValues.add(currentMatrix.get(p).get((int) currentAttribute));
        }
        return attributeValues;
    }

    /*
            Generates where the max values are occured in the attribute values class
     */
    public static ArrayList<Integer> occurance_of_dist(ArrayList<Double> dist, Double max) {
        int count = 0;
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i = 0; i < dist.size(); i++) {
            Double array_element = dist.get(i);
            if (Double.compare(array_element, max) == 0) {
                count++;
                positions.add(i);
            }
        }
        positions.add(count);
        return positions;
    }

    /*
    This method classifies the testing object
     */
    public ArrayList<Double> classification(ArrayList<Double> row, TreeObject node) {

        ArrayList<Double> value;

        /*
        Based on the type of input
        if optimized or randomized
         */
        switch (node.objectType) {
            case 2: {

                int rowSize = 10;
                ArrayList<Double> dist = new ArrayList<Double>();
                for (int i = 0; i < 10; i++) {
                    dist.add(0.0);
                }

                dist.set(node.objectLable, 1.0);
                return dist;
            }
            case 1: {
                ArrayList<Double> dist = node.objectClassProbabilityDistribution;
                return dist;
            }
            default:
                int attribute = (int) node.objectAttribute;
                double threshold = (double) node.objectThreshold;
                double attribute_value = (double) row.get(attribute);
                TreeObject child;
                if (Double.compare(attribute_value, threshold) == -1) {
                    child = node.objectLeftTree;
                } else {
                    child = node.objectRightTree;
                }
                value = classification(row, child);
                break;
        }

        return value;

    }

    /*
    
     */
    public ArrayList<Double> generateCurrentAttributeColumn(ArrayList<ArrayList<Double>> currentMatrix, int attribute) {
        ArrayList<Double> attributeValues = new ArrayList<Double>();
        for (int p = 0; p < currentMatrix.size(); p++) {
            attributeValues.add(currentMatrix.get(p).get(attribute));
        }
        return attributeValues;
    }

    /*
    It is for the 1st case where we choose all the attribute and check for best case
     */
    public ArrayList<Double> chooseAttributeOptimized(ArrayList<ArrayList<Double>> currentMatrix,
            ArrayList<Double> currentAttributes) {

        ArrayList<Double> attributeValues;
        double maxGain = -1, bestAttribute = -1, bestThreshold = -1;
        double currentEntropy = generateEntropyForMatrix(currentMatrix);

        for (int i = 0; i < currentAttributes.size(); i++) {

            attributeValues = generateCurrentAttributeColumn(currentMatrix, (int) i);

            double min = attributeValues.get(0);
            double max = attributeValues.get(0);
            for (Double value : attributeValues) {
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }

            for (int thresholdFactor = 1; thresholdFactor < 50; thresholdFactor++) {
                double threshold = min + thresholdFactor * (max - min) / 51;
                double gain = generateInformationGain(currentMatrix, i, threshold, currentEntropy);

                if (gain > maxGain) {
                    maxGain = gain;
                    bestAttribute = i;
                    bestThreshold = threshold;
                }
            }
        }
        ArrayList<Double> returnValue = new ArrayList<Double>();
        returnValue.add(bestAttribute);
        returnValue.add(bestThreshold);
        returnValue.add(maxGain);

        return returnValue;
    }

    public ArrayList<Double> chooseAttributeRandomized(ArrayList<ArrayList<Double>> currentMatrix,
            ArrayList<Double> currentAttributes) {

        double maxGain = -1, bestAttribute = -1, bestThreshold = -1;
        double currentEntropy = generateEntropyForMatrix(currentMatrix);
        ////System.out.println("currentEntropy "+currentEntropy);
        //for (int attribute = 0; attribute < currentAttributes.size(); attribute++) {

        int bestAttribute1 = (int) (Math.random() * 1000) % 10;
        bestAttribute = (double) bestAttribute1;

        ArrayList<Double> attributeValues = generateCurrentAttributeColumn(currentMatrix, bestAttribute1);

        //        	if(attribute==0){
        //        		//System.out.println(attributeValues);
        //        	}
        double min = attributeValues.get(0);
        double max = attributeValues.get(0);
        for (Double value : attributeValues) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        for (int k = 1; k <= 50; k++) {

            double threshold = min + k * (max - min) / 51;

            double gain = generateInformationGain(currentMatrix, bestAttribute1, threshold, currentEntropy);
            ////System.out.println("Best Attribute selection for  attr "+attribute+" threshold "+threshold +" gain"+gain);
            if (gain > maxGain) {
                maxGain = gain;
                bestThreshold = threshold;
            }
        }
        //}
        ArrayList<Double> returnValue = new ArrayList<Double>();
        returnValue.add(bestAttribute);
        returnValue.add(bestThreshold);
        returnValue.add(maxGain);

        return returnValue;
    }

    /*
    Generates a matrix with rows whose value is less than the threshold value of the specified attribute passed as the argument
     */
    public ArrayList<ArrayList<Double>> generateLessThanThresholdExamples(ArrayList<ArrayList<Double>> currentMatrix,
            double bestAttribute, double bestThreshold) {

        //int newBestAttribute = Integer.parseInt(bestAttribute);
        int newBestAttribute = (int) bestAttribute;
        ArrayList<ArrayList<Double>> returnMatrix = new ArrayList<ArrayList<Double>>();
        for (int row = 0; row < currentMatrix.size(); row++) {
            if (currentMatrix.get(row).get(newBestAttribute) < bestThreshold) {
                returnMatrix.add(currentMatrix.get(row));
            }
        }
        return returnMatrix;
    }

    /*
    Generates a matrix with rows whose value is greater than or equal to the threshold value of the specified attribute passed as the argument
     */
    public ArrayList<ArrayList<Double>> generateGreaterThanThresholdEamples(ArrayList<ArrayList<Double>> currentMatrix,
            double bestAttribute, double bestThreshold) {

        int newBestAttribute = (int) bestAttribute;
        ArrayList<ArrayList<Double>> returnMatrix = new ArrayList<ArrayList<Double>>();
        for (int row = 0; row < currentMatrix.size(); row++) {
            if (currentMatrix.get(row).get(newBestAttribute) >= bestThreshold) {
                returnMatrix.add(currentMatrix.get(row));
            }
        }
        return returnMatrix;
    }

    /*
    This method takes in a parameter as entropy(Parent, l) , input matrix and inputThreshold and generates the information gain
    It generates left child and right child entropy too
     */
    public double generateInformationGain(ArrayList<ArrayList<Double>> inputMatrix, int inputAttribute,
            double inputThreshold, double parentEntropy) {

        double finalInformationGain;

        ArrayList<ArrayList<Double>> lessThanThresholdMatrix;
        ArrayList<ArrayList<Double>> greaterThanThresholdMatrix;

        lessThanThresholdMatrix = generateLessThanThresholdExamples(inputMatrix, (double) inputAttribute, inputThreshold);
        greaterThanThresholdMatrix = generateGreaterThanThresholdEamples(inputMatrix, (double) inputAttribute, inputThreshold);

        double lessThanMatrixEntropy = generateEntropyForMatrix(lessThanThresholdMatrix);
        double greaterThanMatrixEntropy = generateEntropyForMatrix(greaterThanThresholdMatrix);
        finalInformationGain = parentEntropy - (lessThanMatrixEntropy * (lessThanThresholdMatrix.size() / (double) inputMatrix.size())
                + greaterThanMatrixEntropy * (greaterThanThresholdMatrix.size() / (double) inputMatrix.size()));

        return finalInformationGain;
    }

    /*
    This method checks if all the examples have the same class or not
    returns a boolean value, if true has same class else false
     */
    public boolean returnSameOrDiffClass(ArrayList<ArrayList<Double>> inputMatrix) {
        boolean check = true;
        int columnSize = inputMatrix.get(0).size() - 1;
        double classValue = inputMatrix.get(0).get(columnSize);
        for (int i = 0; i < inputMatrix.size(); i++) {
            if (inputMatrix.get(i).get(columnSize) != classValue) {
                check = false;  //At least one is different
                break;
            }
        }
        return check;
    }

    public TreeObject learningDTL(ArrayList<ArrayList<Double>> inputMatrix, ArrayList<Double> currentAttributes,
            ArrayList<Double> ProbabilityDistributionDefaultValue, int inputPruningThreshold, String inputChoice) {

        /*
        if the number of objects is less than or equal 
        to the pruning value entered by the user
         */
        if (inputMatrix.size() < inputPruningThreshold) {
            TreeObject returnCurrentTree = new TreeObject();
            returnCurrentTree.objectClassProbabilityDistribution = ProbabilityDistributionDefaultValue;
            returnCurrentTree.objectType = 1;
            returnCurrentTree.objectID = 0;
            return returnCurrentTree;
        } else if (returnSameOrDiffClass(inputMatrix)) {     //if all have same class            
            //TODO
            int columnSize = inputMatrix.get(0).size() - 1;
            double classValue = inputMatrix.get(0).get(columnSize);
            TreeObject returnCurrentTree = new TreeObject();
            returnCurrentTree.objectLable = (int) classValue;
            returnCurrentTree.objectType = 2;
            returnCurrentTree.objectID = 0;
            return returnCurrentTree;
        } else {    //otherwise, in general  case
            ArrayList<Double> bestAttrbute_bestThreshold;
            ArrayList<ArrayList<Double>> leftTreeExamples;
            ArrayList<ArrayList<Double>> rightTreeExamples;

            TreeObject returnMidTree = new TreeObject();
            returnMidTree.objectType = 0;
            //      node_count++;
            returnMidTree.objectID = 0;

            /*
            check for choice, if optimized then generates best attr and best threshold
            otherwise sets the value to -1 as defined earlier
             */
            if (inputChoice.equals("optimized")) {
                bestAttrbute_bestThreshold = chooseAttributeOptimized(inputMatrix, currentAttributes);
            } else {
                bestAttrbute_bestThreshold = chooseAttributeRandomized(inputMatrix, currentAttributes);
            }

            /*
            Sets object values
             */
            returnMidTree.objectAttribute = bestAttrbute_bestThreshold.get(0);
            returnMidTree.objectThreshold = bestAttrbute_bestThreshold.get(1);
            returnMidTree.objectInformationGain = bestAttrbute_bestThreshold.get(2);

            /*
            generates left tree examples and right tree examples
             */
            leftTreeExamples = generateLessThanThresholdExamples(inputMatrix,
                    bestAttrbute_bestThreshold.get(0), bestAttrbute_bestThreshold.get(1));
            rightTreeExamples = generateGreaterThanThresholdEamples(inputMatrix,
                    bestAttrbute_bestThreshold.get(0), bestAttrbute_bestThreshold.get(1));

            /*
            If the examples are less than pruning threshold then we generate probability distribution matrix
             */
            ArrayList<Double> inputProbabilityDistributionDefaultValue = null;
            if (leftTreeExamples.size() < inputPruningThreshold || rightTreeExamples.size() < inputPruningThreshold) {
                inputProbabilityDistributionDefaultValue = GenerateProbabilityDistribution(inputMatrix);
            }

            /*
            Recursive calls generate left tree first and the right tree for every object
             */
            returnMidTree.objectLeftTree = learningDTL(leftTreeExamples, currentAttributes,
                    inputProbabilityDistributionDefaultValue, inputPruningThreshold, inputChoice);
            returnMidTree.objectRightTree = learningDTL(rightTreeExamples, currentAttributes,
                    inputProbabilityDistributionDefaultValue, inputPruningThreshold, inputChoice);

            return returnMidTree;
        }

    }

    /*
    Generates an ArrayList of the data type double which specifies the distribution of the data according to the respective class
     */
    public ArrayList<Double> GenerateProbabilityDistribution(ArrayList<ArrayList<Double>> currentMatrix) {

        ArrayList<Double> returnMatrix = new ArrayList<Double>();

        int classesCount[] = generateCountOfClasses(currentMatrix);

        /*
        Calculates distribution value for every unique class
         */
        for (int i : classesCount) {
            double finalValue = i / (double) currentMatrix.size();
            returnMatrix.add(finalValue);
        }

        return returnMatrix;
    }

    /*
    This method generates the count of each classes and returns the ArrayList
     */
    public int[] generateCountOfClasses(ArrayList<ArrayList<Double>> inputMatrix) {
        int classesCount[] = new int[10];
        int columnSize = inputMatrix.get(0).size() - 1;
        for (int row = 0; row < inputMatrix.size(); row++) {
            double currentClass = inputMatrix.get(row).get(columnSize);
            int myCurrentClass = (int) currentClass;
            classesCount[myCurrentClass]++;
        }
        return classesCount;

    }

    /*
    This function calculates the entropy for a given matrix
     */
    public double generateEntropyForMatrix(ArrayList<ArrayList<Double>> currentMatrix) {

        double entropy = 0.0;

        if (!currentMatrix.isEmpty()) {
            ////System.out.println(currentMatrix.size());
            //TODO Generalize the unique class label
            //ArrayList<Double> uniqueClasses = find_unique_class(currentMatrix);

            ////System.out.println(" uniqueClasses "+uniqueClasses);
            int classesCount[] = generateCountOfClasses(currentMatrix);

            int current_mat_size = currentMatrix.size();

            for (int i : classesCount) {
                double finalValue = i / (double) current_mat_size;

                if (finalValue != 0.0) {
                    entropy = entropy + (-1.0) * finalValue * (Math.log(finalValue));
                }

            }

            //entropy = entropy * (-1);
        }
        return entropy;
    }

    

}
