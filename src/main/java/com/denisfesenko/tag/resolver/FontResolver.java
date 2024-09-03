package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.Constants;
import com.denisfesenko.util.ConverterUtils;
import com.denisfesenko.util.RunUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Color;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.RPr;
import org.jsoup.nodes.Node;

import java.util.Objects;

public class FontResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        Node fontNode = ConverterUtils.findParentNode(node, "font");
        ObjectFactory objectFactory = RunUtils.getObjectFactory();
        RPr rPr = RunUtils.getCurrentRPr(wordMLPackage);

        String nodeColor = fontNode.attr(Constants.ATTR_KEY_COLOR);
        if (StringUtils.isNotBlank(nodeColor)) {
            String fontColor = ConverterUtils.isHexColor(nodeColor)
                    ? nodeColor : ConverterUtils.rgbToHex(nodeColor, Constants.HEX_BLACK_COLOR);
            Color color = objectFactory.createColor();
            color.setVal(fontColor);
            rPr.setColor(color);
        }

        String size = fontNode.attr("size");
        Double pxNum = ConverterUtils.getPxNum(size);
        if (Objects.nonNull(pxNum)) {
            HpsMeasure hpsMeasure = objectFactory.createHpsMeasure();
            hpsMeasure.setVal(ConverterUtils.pxToHalfPoints(pxNum));
            rPr.setSz(hpsMeasure);
            rPr.setSzCs(hpsMeasure);
        }
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }
}
