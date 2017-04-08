function output = neural_network (input_training_file,input_test_file,inputLayers,inputUnitsPerLayer,inputRounds)
    
	%Loading Test Files	
    train_matrix = load(input_training_file);
    
	%Initializing  rows and colums
	train_matrix_rows = size(train_matrix,1);
    train_matrix_cols = size(train_matrix,2);
	
	%%Initializing  final output class
	class_target = train_matrix(:,end);
    count=0;
	
	%getting all the unique class variables
    unique_class_lables = unique(class_target);
    output_perceptron = [1 ; unique_class_lables];
	[total_classes,number_classes_c] = size(unique_class_lables);
    
	%remobving last column from the training matrix
	train_matrix = train_matrix(:,1:end-1);
	
	%getting max value and normalizing it by dividing throughout matrix
    final_max_val = max(max(train_matrix));
    train_matrix = train_matrix./final_max_val;
    
	%getting ones and appending it to the matrix as bias
	appending_ones = ones(size(train_matrix,1),1);
	train_matrix=[appending_ones train_matrix];   


    dimensions = train_matrix_cols-1;
    total_no_input_layers = inputLayers;
    vector_layers = zeros(inputLayers,1);
    vector_layers(1) = dimensions+1;
    
	%creating a row vector which has the number of perceptrons in each layer
	if inputUnitsPerLayer > 0 && inputLayers > 2
        for i = 2:total_no_input_layers-1
           vector_layers(i) =  inputUnitsPerLayer+1;
        end
    end
    vector_layers(total_no_input_layers) = total_classes+1;
    
	%gets the maximum value from the row vector above
	[has_maximum_column idx] = max(vector_layers);

	%creating weight matrix and initializing it with random values between -0.05 and 0.05
    w = zeros(total_no_input_layers,has_maximum_column,has_maximum_column);
	rand_a=-0.05;
	rand_b=0.05;
    w(:,:,:) = -rand_a + (rand_b-rand_a).*rand(total_no_input_layers,has_maximum_column,has_maximum_column);
    
	z_vector = zeros(total_no_input_layers,has_maximum_column);
    z_vector(:,1) = 1;
    a = zeros(total_no_input_layers,has_maximum_column);
    a(:,1) = 1;
   
	%creating delta for each output
	Pj_delta = zeros(total_no_input_layers,has_maximum_column);
    Pj_delta(:,1) = 1;

	%setting eta with 1
    eta = 1;
	
	
	%will run the entire feedforward and back propogation for the number of rounds
    for current_round = 1:inputRounds
        for current_row = 1:train_matrix_rows		%for each row
            z_vector(1,1:17) = train_matrix(current_row,:);
          
            for current_layer = 2:total_no_input_layers	%for each layer
                nextLayer_perceptronCount = vector_layers(current_layer);
                number_of_perceptron_previous_layer = vector_layers(current_layer-1);

                previous_z_vector = z_vector(current_layer-1,1:number_of_perceptron_previous_layer);

                for thisPerceptron = 2:nextLayer_perceptronCount	%for each perceptron
                    weight_of_perceptron = w(current_layer,thisPerceptron,1:number_of_perceptron_previous_layer);
                    a_vector_val = previous_z_vector*(squeeze(weight_of_perceptron));
                    a(current_layer,thisPerceptron) = a_vector_val;
                    z_vector(current_layer,thisPerceptron) = 1/(1+exp(-1*a_vector_val));
                end
            end
            
            for current_layer = total_no_input_layers:-1:2	%reverse loop from output to input layer
			
			%this is the back propogation
                currentLayer_perceptronCount = vector_layers(current_layer);
                if current_layer == total_no_input_layers
                    
                    for thisPerceptron = 2:currentLayer_perceptronCount
                        z_output_perceptron = z_vector(current_layer,thisPerceptron);
                        if output_perceptron(thisPerceptron) == class_target(current_row,1)
                            target_val = 1;
                        else
                            target_val = 0;
                        end
                        Pj_delta(current_layer,thisPerceptron) = (z_output_perceptron-target_val)*z_output_perceptron*(1-z_output_perceptron);
                    end
                    number_of_perceptron_previous_layer = vector_layers(current_layer-1);

                    for Jth_perceptron_in_layer = 2:currentLayer_perceptronCount
                        delta_output = Pj_delta(current_layer,Jth_perceptron_in_layer); 
                        for Ith_perceptron_in_previous_layer = 1:number_of_perceptron_previous_layer
                            output_prev_perceptron = z_vector(current_layer-1,Ith_perceptron_in_previous_layer);
                            w(current_layer,Jth_perceptron_in_layer,Ith_perceptron_in_previous_layer) = w(current_layer,Jth_perceptron_in_layer,Ith_perceptron_in_previous_layer) - (eta * delta_output * output_prev_perceptron);
                        end
                    end
                else
                    nextLayer_perceptronCount = vector_layers(current_layer+1);
                    for Jth_perceptron_in_layer = 2:currentLayer_perceptronCount
                        z_curr_lay = z_vector(current_layer,Jth_perceptron_in_layer);
                        summation_for_each_perceptron = 0;
                        for perceptron_uth = 2:nextLayer_perceptronCount
                            summation_for_each_perceptron = summation_for_each_perceptron + (Pj_delta(current_layer+1,perceptron_uth) * w(current_layer+1,perceptron_uth,Jth_perceptron_in_layer));
                        end
                        Pj_delta(current_layer,Jth_perceptron_in_layer) = summation_for_each_perceptron * z_curr_lay * (1-z_curr_lay);
                    end
                    
					
                    number_of_perceptron_previous_layer = vector_layers(current_layer-1);

                    for Jth_perceptron_in_layer = 2:currentLayer_perceptronCount
                        delta_output_1 = Pj_delta(current_layer,Jth_perceptron_in_layer); 
                        for Ith_perceptron_in_previous_layer = 1:number_of_perceptron_previous_layer
                            z_previous_layer_perceptron_1 = z_vector(current_layer-1,Ith_perceptron_in_previous_layer);
                            w(current_layer,Jth_perceptron_in_layer,Ith_perceptron_in_previous_layer) = w(current_layer,Jth_perceptron_in_layer,Ith_perceptron_in_previous_layer) - (eta * delta_output_1 * z_previous_layer_perceptron_1);
                        end
                    end
                end
            end
         
        end
        eta = eta * 0.98;
		
    end

	%training part ends
	
	%testing part starts
	
	%initializing test matrix with test file
	%getting rows, columns, and final output class from the matrix
    test_matrix = load(input_test_file);
    [test_matrix_rows, test_matrix_cols] = size(test_matrix);
    given_class = test_matrix(:,test_matrix_cols);
    will_predict_Class = zeros(size(given_class));
    will_predict_Class(:,1) = -1;

    test_matrix = test_matrix(:,1:test_matrix_cols-1);
   
    test_final_max_val = max(max(test_matrix));
    test_matrix = test_matrix./test_final_max_val;

	
	    test_matrix = [ones(size(test_matrix,1),1) test_matrix];
	
	%initializing z and a vectors
    test_z_vector = zeros(total_no_input_layers,has_maximum_column);
    test_z_vector(:,1) = 1;
    test_z_vector(total_no_input_layers,1) = 0;

    test_a_vector = zeros(total_no_input_layers,has_maximum_column);
    test_a_vector(:,1) = 1;

   %feedforward for only one round for each row
    for current_row = 1:test_matrix_rows
        test_z_vector(1,1:17) = test_matrix(current_row,:);
        for current_layer = 2:total_no_input_layers% for each layer
            nextLayer_perceptron = vector_layers(current_layer);
            number_of_perceptron_previous_layer_t = vector_layers(current_layer-1);
            for thisPerceptron = 2:nextLayer_perceptron	%for each perceptron
                previous_z_vector = test_z_vector(current_layer-1,1:number_of_perceptron_previous_layer_t);
                weight_of_this_perceptron = w(current_layer,thisPerceptron,1:number_of_perceptron_previous_layer_t);
                a_val_t = previous_z_vector*(squeeze(weight_of_this_perceptron));
                test_a_vector(current_layer,thisPerceptron) = a_val_t;
                test_z_vector(current_layer,thisPerceptron) = 1/(1+exp(-1*a_val_t));
            end
        end
        [M,I] = max(test_z_vector(total_no_input_layers,:));
        if I == 1
            will_predict_Class(current_row) = 0;
        else
            will_predict_Class(current_row) = unique_class_lables(I-1);
        end
		
    end
	
	what_we_classify = (will_predict_Class==given_class);
    
	%printing output
    for current_row = 1:test_matrix_rows
        fprintf('ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f \n',current_row-1, will_predict_Class(current_row), given_class(current_row), what_we_classify(current_row));
    end

	%final accuracy
    fprintf('classification accuracy=%6.4f \n', sum(what_we_classify)/test_matrix_rows);
end