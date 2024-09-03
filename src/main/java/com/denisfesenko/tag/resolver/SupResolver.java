package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTVerticalAlignRun;
import org.docx4j.wml.STVerticalAlignRun;
import org.jsoup.nodes.Node;


public class SupResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        CTVerticalAlignRun vertAlign = RunUtils.getObjectFactory().createCTVerticalAlignRun();
        vertAlign.setVal(STVerticalAlignRun.SUPERSCRIPT);
        RunUtils.getCurrentRPr(wordMLPackage).setVertAlign(vertAlign);
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }
}
