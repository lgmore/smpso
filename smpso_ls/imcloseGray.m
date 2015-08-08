function g = imcloseGray(f, ele)
ID = imdilateGray(f, ele);
g = imerodeGray(ID, ele);
%figure, imshow([f, g]);
end