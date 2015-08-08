function e = entropia(I)
hist = imhist(I);               % histograma de la imagen
P = hist / sum(hist(:));        % probabilidad
% calcula la sumatoria
s = 0;
for k=1:length(hist)            % L = length(hist)
    if P(k) > 0
         s = s + P(k) * log2(P(k));
    end
end
% entropia
e = -s;

end

