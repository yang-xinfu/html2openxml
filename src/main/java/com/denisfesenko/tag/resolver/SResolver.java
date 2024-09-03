package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.RPr;
import org.jsoup.nodes.Node;

/**
 * 文字删除线
 *
 * @author YangXinFu
 * @date 2024/8/29 14:25
 */
public class SResolver implements TagResolver {
    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        RPr currentRPr = RunUtils.getCurrentRPr(wordMLPackage);
        BooleanDefaultTrue strike = new BooleanDefaultTrue();
        strike.setVal(true);
        currentRPr.setStrike(strike);
    }
}
