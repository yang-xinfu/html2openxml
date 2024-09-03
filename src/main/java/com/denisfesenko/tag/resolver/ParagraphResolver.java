package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.ConverterUtils;
import com.denisfesenko.tag.wrapper.JcEnumMapper;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Jc;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.jsoup.nodes.Node;

import java.util.Map;
import java.util.Objects;

import static com.denisfesenko.util.Constants.ATTR_KEY_ALIGN;
import static com.denisfesenko.util.Constants.ATTR_KEY_TEXT_INDENT;


public class ParagraphResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        Map<String, String> nodeStyle = ConverterUtils.getNodeStyle(node);


        ObjectFactory objectFactory = RunUtils.getObjectFactory();
        PPr pPr = objectFactory.createPPr();


        String align = nodeStyle.get(ATTR_KEY_ALIGN);
        if (align != null) {
            Jc jc = objectFactory.createJc();
            jc.setVal(JcEnumMapper.map(align));
            pPr.setJc(jc);
        }

        String textIndentValue = nodeStyle.getOrDefault(ATTR_KEY_TEXT_INDENT, "");
        Double indent = ConverterUtils.getPxNum(textIndentValue);
        if (Objects.nonNull(indent)) {
            PPrBase.Ind ind = objectFactory.createPPrBaseInd();
            ind.setFirstLine(ConverterUtils.pxToDxa(indent));
            pPr.setInd(ind);
        }

        RunUtils.createParagraph(wordMLPackage, pPr);
    }

}
