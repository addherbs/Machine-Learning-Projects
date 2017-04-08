function [output]=pca_power(inputTrainingFile,inputTestingFile,input_OutputSpaceDimenion,inputIterations)

    %{
    It initializes all the input parameters
    %}
    trainingFile = inputTrainingFile;
    testingFile = inputTestingFile;
    outputSpaceDimenion = input_OutputSpaceDimenion;
    numberOfIterations = inputIterations;

    % This is the Training Phase
    
    %{
    It loads the training data and gets it dimensions
    %}
    trainingMatrix=load(trainingFile);
    trainingRows	= size(trainingMatrix,1);
    trainingCols 	= size(trainingMatrix,2);
    mainTrainingMatrix=trainingMatrix(:,1:end-1);
    

    numberOfDimensions = trainingCols-1;
    storeEigenVector = zeros(outputSpaceDimenion,numberOfDimensions);

    
    %{
    Main loop for training phase
    It generates the eigen vectors in each iteration and prints
    %}
    for i= 1:outputSpaceDimenion        %Till number of output space dimensions(M)
        
        fprintf("Eigenvector %d\n",i);
        
        temporaryEigenVectors = rand(numberOfDimensions,1);
        covarianceMarix = cov(mainTrainingMatrix);  %generates covariance matrix

        %{
        Till the number of iterations specified
        It generates a temporary vector and squares each value of it
        Now it adds all the values to generate a normalization factor
        now it divides each value by the normalization vector
        %}
        for j=1 : numberOfIterations
            temporaryVector = covarianceMarix * temporaryEigenVectors;
            normalizationScalar = sum(temporaryVector.^2);
            temporaryEigenVectors = temporaryVector./sqrt(normalizationScalar);
        end
        
        %{
        Stores each eigen vector in the original vector matrix, so that we
        can use this for testing purposes
        %}
        storeEigenVector(i,:) = temporaryEigenVectors;

           %{
        Prints eigen vector
        %}
        for j=1: numberOfDimensions
            fprintf("\t%d: %.4f \n ",j,storeEigenVector(i,j));
        end

        %{
        Updates the training matrix 
        %}
        for k=1 : trainingRows
             mainTrainingMatrix(k,:)  = mainTrainingMatrix(k,:) - ( (temporaryEigenVectors' * mainTrainingMatrix(k,:)') * temporaryEigenVectors') ;
        end

    end    

    %Testing phase

    %{
    It loads the testing data and gets it dimensions
    %}
    testingMatrix=load(testingFile);
    mainTestingMatrix=testingMatrix(:,1:end-1);
    testingRows	= size(testingMatrix,1);

    %{
    For each row in the testing matrix
    %}
    for i=1:testingRows
        
        fprintf("Test object %d \n",i);
        
        %{
        Stores the current row
        %}
        eachRow = mainTestingMatrix(i,:);
            
        %{
        for each M
        %}
        for j=1:outputSpaceDimenion
            currentEigenVector = storeEigenVector(j,:)';
            temporaryScalarValue = eachRow * currentEigenVector;
            fprintf("\t%d:  %.4f \n ",j,temporaryScalarValue);
            
        end

    end    

end