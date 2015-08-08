% method de 1 al 5 y dim de 1 a N
function ele = getElemento(method, dim)
switch method
    case 1
        disp(strcat('ElementoEstructurante: [disk,',num2str(dim),']'));
        ele = strel('disk',dim);
    case 2
        disp(strcat('ElementoEstructurante: [diamond,',num2str(dim),']'));
        ele = strel('diamond',dim);
    case 3         
        while mod(dim, 3) ~= 0
            dim = dim + 1;
        end
        disp(strcat('ElementoEstructurante: [octagon,',num2str(dim),']'));
        ele = strel('octagon',dim);
    case 4
        disp(strcat('ElementoEstructurante: [rectangle,',num2str(dim),']'));
        ele = strel('rectangle',[dim  dim]);
    otherwise
        disp(strcat('ElementoEstructurante: [square,',num2str(dim),']'));
        ele = strel('square',dim);
end
end

