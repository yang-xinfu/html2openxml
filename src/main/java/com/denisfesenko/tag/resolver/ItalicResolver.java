package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.RPr;
import org.jsoup.nodes.Node;


public class ItalicResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        RPr rPr = RunUtils.getCurrentRPr(wordMLPackage);
        rPr.setI(RunUtils.createBooleanDefaultTrue());
        rPr.setICs(RunUtils.createBooleanDefaultTrue());
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }
}
