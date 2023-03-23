package com.denisfesenko;

import com.denisfesenko.converter.HtmlToOpenXMLConverter;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        String html = "<p>Hello, <b>World</b>! This is an <span style=\"background-color: rgb(255, 0, 0);\"><i>example</i> of <b><i>mixed</i></span> formatting</b>.</p>";

        try {
            HtmlToOpenXMLConverter converter = new HtmlToOpenXMLConverter();
            WordprocessingMLPackage wordMLPackage = converter.convert(html);
            wordMLPackage.save(new File("output.docx"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
