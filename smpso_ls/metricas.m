function [e, c] = metricas(I)
e = entropia(uint8(I));
c = contrast(uint8(I));
end