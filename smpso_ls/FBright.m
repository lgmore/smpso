function fB = FBright(I, ele, n, m)
I = double(I);
ele = getnhood(ele);
[M N] = size(I);
fB = zeros(M,N);
for i=n:m    
    IO = imopenNGray(I,ele,i);    
    fB = fB + (I - IO);
end
end
