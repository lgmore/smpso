function g = imerodeNGray(img, ele, n)
   nele = nElementoGrayV2(ele, n);
   g =  imerodeGray(img,nele);
end