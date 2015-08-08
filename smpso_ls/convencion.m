function g = convencion(g)
[f,c] = size(g);
for i=1:f,
    for j=1:c,
        if g(i,j) == 0
            g(i,j) = -inf;
        else
            g(i,j) = 0;
        end
    end
end
end