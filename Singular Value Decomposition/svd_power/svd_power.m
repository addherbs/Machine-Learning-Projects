%{
Name: Aditya
Surname: Pandey
Student Id: 1001405034
Course Name: Machine Learning
Course Number: 6363
%}
function [output]=svd_power(inputTrainingMatrix,inputDimensions,inputIterations)

    %Initializing all the variables
    trainingMatrix=dlmread(inputTrainingMatrix);
    row	= size(trainingMatrix,1);
    col = size(trainingMatrix,2);
    numberOfDimensions = inputDimensions;
    numberOfIterations = inputIterations;
    
    %Generating Initial U matrix and U matrix also S Matrix
    temp_u_mat = trainingMatrix * trainingMatrix';
    temp_v_mat = trainingMatrix'  * trainingMatrix;
    final_SMatrix = zeros(numberOfDimensions,numberOfDimensions);

    %Main loop which generates U V and S matrix
    %It uses power method 
    for i=1:numberOfDimensions
        eigenVectorU = ones(row,1);
        eigenVectorV = ones(col,1);
        
        %Generating Eigen Vectors
        for j=1:numberOfIterations
            temporary_U_Vector = temp_u_mat*eigenVectorU;
            temporary_V_Vector = temp_v_mat * eigenVectorV;
            normalizedU = sum(temporary_U_Vector.^2);
            normalizedV = sum(temporary_V_Vector.^2);
            eigenVectorU = temporary_U_Vector./sqrt(normalizedU);
            eigenVectorV = temporary_V_Vector./sqrt(normalizedV);
        end
        
        %Updating U matrix
        for k=1 : size(temp_u_mat,1)
            temp_u_mat(k,:)  = temp_u_mat(k,:) - ( (eigenVectorU' * temp_u_mat(k,:)') * eigenVectorU') ;
        end
        
        %Updating V matrix
        for k=1 : size(temp_v_mat,1)
            temp_v_mat(k,:)  = temp_v_mat(k,:) - ( (eigenVectorV' * temp_v_mat(k,:)') * eigenVectorV') ;
        end

        final_UMatrix(:,i)=transpose(eigenVectorU);
        final_VMatrix(:,i)=transpose(eigenVectorV);
        final_SMatrix(i,i) = sqrt(sqrt(normalizedU));

    end

    %Printing U matrix
    fprintf("Matrix U:\n");
    row	= size(final_UMatrix,1);
    col = size(final_UMatrix,2);
    for i=1:row
        fprintf("Row\t%d:",i);
        for j=1:col
            fprintf("%8.4f\t",  final_UMatrix(i,j));
        end
        fprintf("\n");
    end

    %Printing S matrix
    fprintf("\nMatrix S:\n");
    row	= size(final_SMatrix,1);
    col = size(final_SMatrix,2);
    for i=1:row
        fprintf("Row\t%d:",i);
        for j=1:col
            fprintf("%8.4f\t",  final_SMatrix(i,j));
        end
        fprintf("\n");
    end

    %Printing V matrix
    fprintf("\nMatrix V:\n");
    row	= size(final_VMatrix,1);
    col = size(final_VMatrix,2);
    for i=1:row
        fprintf("Row\t%d:",i);
        for j=1:col
            fprintf("%8.4f\t",  final_VMatrix(i,j));
        end
        fprintf("\n");
    end

    A = final_UMatrix*final_SMatrix*final_VMatrix';
    %Printing R matrix
    fprintf("\nReconstruction (U*S*V'):\n");
    row	= size(A,1);
    col = size(A,2);
    for i=1:row
        fprintf("Row %d:",i);
        for j=1:col
            fprintf("%8.4f  ",  A(i,j));
        end
        fprintf("\n");
    end
	
end