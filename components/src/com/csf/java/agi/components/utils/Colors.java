package com.csf.java.agi.components.utils;

import java.awt.*;

public class Colors {
    private Colors() {
    }

    public static final Color TRANSPARENT = new Color(0x00000000, true);
    public static final Color NODE_HIGHLIGHT = new Color(210, 227, 252, 200);

    public static Color makeTransparent(Color source, int alpha) {
        return new Color(source.getRed(), source.getGreen(), source.getBlue(), Math.min(alpha, 0xFF));
    }

}
