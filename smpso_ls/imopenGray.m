function g = imopenGray(f, ele)
E = imerodeGray(f,ele);
g = imdilateGray(E, ele);
%figure, imshow([f, g]);
end