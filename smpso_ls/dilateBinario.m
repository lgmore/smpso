function g= dilateBinario(f, ele)
%f = [0 1 0; 1 1 1; 0 1 0];
%ele = [0 1 0; 1 1 1; 0 1 0];
[FI, CI] = size(f);
[FE, CE] = size(ele);
ui = zeros(2,FI*CI);
ue = zeros(2,FE*CE);
ci = 0;
% extraer las coordenadas de los 1 de la imagen
for i=1:FI,
    for j=1:CI,        
        if f(i,j) == 1
            ci = ci + 1;
            ui(1,ci) = i; ui(2,ci) = j; 
        end
    end
end
% extraer las coordenadas de los 1 de la ele
ce = 0;
for i=1:FE,
    for j=1:CE,        
        if ele(i,j) == 1
            ce = ce + 1;
            ue(1,ce) = i; ue(2,ce) = j; 
        end
    end
end
% calcula la suma
r = zeros(2,ci*ce);
c = 0;
for i=1:ci,
    for j=1:ce,
        c = c +1;
        r(1,c) = ui(1,i)+ ue(1,j);
        r(2,c) = ui(2,i)+ ue(2,j);
    end
end
% llena 1 
g = zeros(max(r(1,1:end)),max(r(2,1:end)));
for i=1:ci*ce
    g(r(1,i),r(2,i)) = 1;
end
end