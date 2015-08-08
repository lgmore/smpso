function c = contrast(I)        % I = imagen
hist = imhist(I);               % histograma de la imagen
IM = median(hist(:));           % intensidad media de la imagen
P = hist / sum(hist(:));        % probabilidad
% calcula la sumatoria
s = 0;
for k=1:length(hist)            % L = length(hist)
    s = s + (k - IM)^2 * P(k);
end
% aplica la raiz cuadrada de la sumatoria
c = sqrt(s);
end
