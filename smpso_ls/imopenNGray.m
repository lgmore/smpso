function g = imopenNGray(f, ele, n)
disp(strcat('[imopenNGray, n= ',num2str(n),']'));
EN = imerodeNGray(f,ele,n);
g = imdilateNGray(EN, ele, n);
g = double(g);
%figure, imshow([f, g]);
end