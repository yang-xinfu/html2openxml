package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.nodes.Node;


public class BreakResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        RunUtils.getCurrentRun(wordMLPackage).getContent().add(RunUtils.getObjectFactory().createBr());
    }

}
