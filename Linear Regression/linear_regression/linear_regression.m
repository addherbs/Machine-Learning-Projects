function [result]=linear_regression(commandLine_input_file,commandLine_degree,commandLine_lamda)

programmed_degree=commandLine_degree+1;

input_file=load(commandLine_input_file);
X_coordinate=input_file(:,1);
Y_coordinate=input_file(:,2);

for p=1:size(X_coordinate,1)
for q=1:programmed_degree
phi_vector(p,q)=power(X_coordinate(p,1),q-1);
end 
end

phi_vector_transpose = phi_vector';

if commandLine_lamda==0
column_vector_W=pinv(phi_vector)*Y_coordinate;
column_vector_W=[column_vector_W;0];
else
column_vector_W=inv(commandLine_lamda*eye(programmed_degree) + (phi_vector_transpose*phi_vector))*phi_vector_transpose*Y_coordinate;
end


fprintf('w0=%.4f\n', double(column_vector_W(1,1)));
fprintf('w1=%.4f\n', double(column_vector_W(2,1)));
fprintf('w2=%.4f\n',double(column_vector_W(3,1)));

end