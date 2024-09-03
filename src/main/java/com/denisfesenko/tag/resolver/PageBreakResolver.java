package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Br;
import org.docx4j.wml.STBrType;
import org.jsoup.nodes.Node;


public class PageBreakResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        Br br = RunUtils.getObjectFactory().createBr();
        br.setType(STBrType.PAGE);
        RunUtils.getCurrentRun(wordMLPackage).getContent().add(br);
    }

}
