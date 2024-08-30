package com.denisfesenko.tag;

import com.denisfesenko.core.TagHandler;
import com.denisfesenko.util.ConverterUtils;
import com.denisfesenko.util.JcEnumMapper;
import com.denisfesenko.util.RunUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.UnitsOfMeasurement;
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

/**
 * This class handles the conversion of HTML paragraphs to WordprocessingMLPackage format.
 */
public class ParagraphHandler implements TagHandler {

    /**
     * Converts the paragraph node to a WordprocessingMLPackage instance.
     *
     * @param node          The paragraph node to be converted.
     * @param wordMLPackage The WordprocessingMLPackage instance to which the converted paragraph will be added.
     */
    @Override
    public void handleTag(Node node, WordprocessingMLPackage wordMLPackage) {
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

    /**
     * Returns whether the tag handler is repeatable.
     *
     * @return false, because the ParagraphHandler is not repeatable.
     */
    @Override
    public boolean isRepeatable() {
        return false;
    }
}
