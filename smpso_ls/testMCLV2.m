function [e, c] = testMCLV2(img,n,m,alfa,type,dim)
% test de mejora de contraste
clc;
close all
I = imread(img); 
% mejora de contraste local
%tic
disp(strcat('-----test mejora de contraste local-----'));
[e, c] = mclV2(I, getElemento(type, dim), n , m, alfa);
%toc
disp(strcat('-----test mejora de contraste local-----'));

