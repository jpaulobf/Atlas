package util;

import java.awt.geom.Path2D;

public class NewRoundRectangle2D extends Path2D.Double {

    public NewRoundRectangle2D(double x, double y, double w, double h, 
                                double atlw, double atlh, // Top Left (w, h)
                                double atrw, double atrh, // Top Right (w, h)
                                double ablw, double ablh, // Bottom Left (w, h)
                                double abrw, double abrh) // Bottom Right (w, h) 
    {
        // 1. Mover para o ponto logo após o canto superior esquerdo
        moveTo(x + atlw, y);

        // 2. Linha superior e Canto Superior Direito
        lineTo(x + w - atrw, y);
        if (atrw > 0 || atrh > 0) {
            quadTo(x + w, y, x + w, y + atrh);
        } else {
            lineTo(x + w, y);
        }

        // 3. Linha lateral direita e Canto Inferior Direito
        lineTo(x + w, y + h - abrh);
        if (abrw > 0 || abrh > 0) {
            quadTo(x + w, y + h, x + w - abrw, y + h);
        } else {
            lineTo(x + w, y + h);
        }

        // 4. Linha inferior e Canto Inferior Esquerdo
        lineTo(x + ablw, y + h);
        if (ablw > 0 || ablh > 0) {
            quadTo(x, y + h, x, y + h - ablh);
        } else {
            lineTo(x, y + h);
        }

        // 5. Linha lateral esquerda e Canto Superior Esquerdo
        lineTo(x, y + atlh);
        if (atlw > 0 || atlh > 0) {
            quadTo(x, y, x + atlw, y);
        } else {
            lineTo(x, y);
        }

        closePath();
    }
}