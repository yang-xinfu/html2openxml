package com.denisfesenko.tag;

import com.denisfesenko.core.TagHandler;
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
public class SHandler implements TagHandler {
    @Override
    public void handleTag(Node node, WordprocessingMLPackage wordMLPackage) {
        RPr currentRPr = RunUtils.getCurrentRPr(wordMLPackage);
        BooleanDefaultTrue strike = new BooleanDefaultTrue();
        strike.setVal(true);
        currentRPr.setStrike(strike);
    }
}
