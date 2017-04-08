%This function calculates the logisticRegrssion%
function [output]=logistic_regression(inputTraining,inputDegree,inputTest)

%Initializing Values from the input Values
trainingMatrix=load(inputTraining);
testingMatrix=load(inputTest);
finalDegree=inputDegree;

%Stores the Last decision column of the trainig matrix
givenClass=trainingMatrix(:,size(trainingMatrix,2));

%Modifies the last column and keeps it 0/1
for i=1:size(givenClass,1)
    if givenClass(i)~=1
        givenClass(i)=0;
    end
end


withoutClassMatrix=trainingMatrix(:,1:end-1);
tempVal=1;
initialOnesColumn=ones(size(trainingMatrix,1),1);

%Initializes Phi Matrix
if finalDegree==1
    phi_matrix_vector=[initialOnesColumn withoutClassMatrix];
elseif finalDegree==2
    for i=1:size(withoutClassMatrix,2)
        finalForValue=tempVal+1;
        tempSquaredValue=withoutClassMatrix(:,i).^2;
        phi_matrix_vector(:,tempVal:finalForValue)=[withoutClassMatrix(:,i) tempSquaredValue ];
        tempVal=finalForValue+1;
    end
    phi_matrix_vector=[initialOnesColumn phi_matrix_vector];
    
end

%Calculates Px vaetoc
%Initialize Weight Vector
phiXVectorTrans=phi_matrix_vector';
sizeWeight=size(phi_matrix_vector,2);
initialWeightMatrix=zeros(1,sizeWeight);
oldWeight=initialWeightMatrix';


%Calculates cross entropy error
%Compare weight
%Calculates final weight
count = 0;
while 1
    tempCalculation=oldWeight'*phiXVectorTrans;
    for i=1:size(tempCalculation,2)
        ySigmoidalMatrix(i)=1/(1+ exp(-tempCalculation(i)));
    end
    onesVector=ones(size(ySigmoidalMatrix,1),size(ySigmoidalMatrix,2));
    temporaryDifference=onesVector-ySigmoidalMatrix;
    temp=(ySigmoidalMatrix.*temporaryDifference);
    rDiagonalMatrix=diag(temp);
    
    gradientE=phiXVectorTrans*(ySigmoidalMatrix' - givenClass);
    HessianMatrix=phiXVectorTrans*rDiagonalMatrix*phi_matrix_vector;
    newWeight=oldWeight-(pinv(HessianMatrix)*gradientE);
    differenceInWeight=oldWeight-newWeight;
    sum2=abs(sum(differenceInWeight));
    if sum2<0.001
        oldWeight=newWeight;
        break;
    end
    pnew=newWeight'*phiXVectorTrans;
    for i=1:size(pnew,2)
        newYSigmoidalVector(i)=1/(1+ exp(-pnew(i)));
    end
    newE=phiXVectorTrans*(newYSigmoidalVector'-givenClass);
    deltaE=gradientE-newE;
    secondSum=abs(sum(deltaE));
    if secondSum<0.001
        oldWeight=newWeight;
        break;
    end
    oldWeight=newWeight;
    count = count + 1;
end

%Prints the output for the training phase
for i=1:size(oldWeight,1)
    fprintf('w%d=%0.4f\n', i-1,oldWeight(i,1));
end
 

%Second Phase of the Assignment Starts here

%Initializes testing class column
    testingClassColumn=testingMatrix(:,size(testingMatrix,2));
    for i=1:size(testingClassColumn,1)
        if testingClassColumn(i)~=1
            testingClassColumn(i)=0;
        end
    end
    
    tempVal=1;
    
    %Initializes testing matrix
    testingMatrixVector=testingMatrix(:,1:end-1);
    testingOnesColumn=ones(size(testingMatrix,1),1);
    
    %Calculate phi matrix for test data
    if finalDegree==1
        testingPhiMatrix=[testingOnesColumn testingMatrixVector];
    elseif finalDegree==2
        for i=1:size(testingMatrixVector,2)
            finalForValue=tempVal+1;
            tempTestingSquaredValue=testingMatrixVector(:,i).^2;
            testingPhiMatrix(:,tempVal:finalForValue)=[testingMatrixVector(:,i) tempTestingSquaredValue ];
            tempVal=finalForValue+1;
        end
        testingPhiMatrix=[testingOnesColumn testingPhiMatrix];
    end
    
    %Calculate Px for test data
    testingPhiXVectorTrans=testingPhiMatrix';
    testingValues=oldWeight'*testingPhiXVectorTrans;
    
    %Using Sigmoidal Function to generate Y vector
    for i=1:size(testingValues,2)
        testingYSigmoidalVector(i)=1/(1+ exp(-testingValues(i)));
    end
    
    
    testingValues(testingValues>0)=1;
    testingValues(testingValues<0)=0;
    
    testingValues = testingValues';
    testingYSigmoidalVector = testingYSigmoidalVector';
    
    n_pred_class = 0;
    
    %This loop will run for each row and will calculate the following:
    %Row's ID, Predicted Class, Probability, Accuracy
    %We just use data as a cache to print and dont store it
    for rowID = 1: size(testingValues,1)
        predictedClass = testingValues(rowID);
        probability = testingYSigmoidalVector(rowID);
        actualClass = testingClassColumn(rowID);
        
        
        accuracy = 0.0;
        
        
        if predictedClass==0
            probability = 1-probability;
        end
        
        if predictedClass == actualClass
            accuracy = 1.0;
            n_pred_class = n_pred_class + 1;
        end
        
        fprintf('ID=%5d, predictedClass=%3d, probability = %.4f, true=%3d, accuracy=%4.2f \n', rowID-1, predictedClass, probability,actualClass, accuracy);
    end
    
    %Prints final output of the Testing phase
    classification_accuracy = n_pred_class/size(testingValues,1);
    fprintf('classification accuracy=%6.4f \n', classification_accuracy);
    
end