package com.denisfesenko.util;

/**
 * This class provides a set of constant values that are commonly used in various parts of the application.
 */
public interface Constants {
    /**
     * The key for setting the width of an element.
     */
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";

    /**
     * The key for setting the style of an element.
     */
    public static final String STYLE = "style";

    /**
     * The default style for a table that has no borders.
     */
    public static final String TABLE_BORDERLESS_STYLE = "border: none;";

    /**
     * The white color in hexadecimal format.
     */
    public static final String HEX_WHITE_COLOR = "#ffffff";

    /**
     * The black color in hexadecimal format.
     */
    public static final String HEX_BLACK_COLOR = "#000000";

    String SEMICOLON =";";

    String COLON = ":";

    String ATTR_KEY_TEXT_INDENT = "text-indent";
    String ATTR_KEY_ALIGN = "align";

    String ATTR_KEY_BACKGROUND_COLOR = "background-color";

    String ATTR_KEY_COLOR = "color";

    String ATTR_KEY_TEXT_EMPHASIS = "text-emphasis";
    String ATTR_KEY_SRC = "src";



    interface TextDecoration{
        String KEY_TEXT_DECORATION = "text-decoration";
        String KEY_TEXT_DECORATION_LINE = "text-decoration-line";
        String KEY_TEXT_DECORATION_STYLE = "text-decoration-style";

        String VALUE_UNDERLINE = "underline";

        String VALUE_LINE_THROUGH = "line-through";

        String VALUE_STYLE_SOLID = "solid";
        String VALUE_STYLE_WAVY = "wavy";
        String VALUE_STYLE_DOUBLE = "double";
    }
}
