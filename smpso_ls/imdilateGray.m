function g= imdilateGray(f, ele)
ele = double(ele);
[FI,CI]=size(f);
[M, N]=size(ele);
c=floor(N/2);
r=floor(M/2);
h= zeros(FI+c*2, CI+r*2);
h(c+1:FI+c, r+1:CI+r)=f;
for i=1:FI,
    for j=1:CI,
           %pixeles que caen en la mascara
           ma= h(i:(i+N-1),j:(j+M-1));           
           g(i,j)= max(max(ma + ele));          
    end
end
g= double(g);
%figure, imshow([f, g]);
end