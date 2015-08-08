function g = imdilateNGray(img, ele, n)
 nele = nElementoGrayV2(ele, n);
 g = imdilateGray(img, nele);
end