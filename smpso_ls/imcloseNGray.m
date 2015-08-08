function g = imcloseNGray(f, ele, n)
disp(strcat('[imcloseNGray, n= ',num2str(n),']'));
ID = imdilateNGray(f, ele, n);
g = imerodeNGray(ID, ele, n);
g = double(g);
%figure, imshow([f, g]);
end