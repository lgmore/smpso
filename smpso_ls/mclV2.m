function [e, c] = mclV2(I, ele, n, m, alfa)
disp(strcat('Parametros [n= ',num2str(n), ', m= ' , num2str(m), ', alfa=' ,num2str(alfa),']'));
%whos I ele n m alfa
disp(strcat('1. calcula alfa * sumatoria de FBright..'));
fBright = FBright(I,ele,n,m);
disp(strcat('2. calcula alfa * sumatoria de FDark.. '));
fDark = FDark(I,ele,n,m);
disp(strcat('3. calcula i + alfa * FBright - alfa * FBright...'));
i = double(I);
a = alfa * fBright;
b = alfa * fDark;
f =  i + a - b;
%whos f
% resultado
%figure(1)
%subplot(1,2,1)
%imshow(uint8(a)); title('alfa * FBright');
%subplot(1,2,2)
%imshow(uint8(b)); title('alfa * FDark');

figure(2)
subplot(1,2,1)
[eo, co] = metricas(I);
disp(strcat('original. entropia= ',num2str(eo), ', contraste= ' , num2str(co)));
imshow(uint8(I)); title(strcat('original. entropia= ',num2str(eo), ', contraste= ' , num2str(co)));
subplot(1,2,2)
[e, c] = metricas(f);
disp(strcat('con mejora local. entropia= ',num2str(e), ', contraste= ' , num2str(c)));
imshow(uint8(f)); title(strcat('con mejora local. entropia=',num2str(e), ', contraste= ' , num2str(c)));
% guarda el resultado
figuresdir = pwd; 
saveas(figure(2), strcat(figuresdir,'/Resultados/','resultado',datestr(now,'mm-dd-yyyy-HHMMSS'),'.png'));
disp(strcat('Fin de la ejecucion de MCL'));
end