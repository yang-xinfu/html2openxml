package com.denisfesenko.tag.resolver;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.util.Constants;
import com.denisfesenko.util.ConverterUtils;
import com.denisfesenko.util.RunUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;
import org.jsoup.nodes.Node;

import java.util.Map;
import java.util.Objects;

public class SpanResolver implements TagResolver {

    @Override
    public void convert(Node node, WordprocessingMLPackage wordMLPackage) {
        Node spanNode = ConverterUtils.findParentNode(node, "span");
        Map<String, String> nodeStyle = ConverterUtils.getNodeStyle(spanNode);
        ObjectFactory objectFactory = RunUtils.getObjectFactory();
        RPr currentRPr = RunUtils.getCurrentRPr(wordMLPackage);

        String nodeColor = spanNode.attr(Constants.ATTR_KEY_COLOR);
        if (StringUtils.isNotBlank(nodeColor)) {
            String fontColor = ConverterUtils.isHexColor(nodeColor)
                    ? nodeColor : ConverterUtils.rgbToHex(nodeColor, Constants.HEX_BLACK_COLOR);
            Color color = objectFactory.createColor();
            color.setVal(fontColor);
            currentRPr.setColor(color);
        }

        String bgColor = nodeStyle.get(Constants.ATTR_KEY_BACKGROUND_COLOR);
        if (bgColor != null) {
            Highlight highlight = RunUtils.getObjectFactory().createHighlight();
            highlight.setVal(ConverterUtils.isHexColor(bgColor) ? bgColor : ConverterUtils.rgbToHex(bgColor, Constants.HEX_WHITE_COLOR));
            //skip unsupported color
            if (highlight.getVal() == null) {
                highlight.setVal(Constants.HEX_WHITE_COLOR);
            }
            currentRPr.setHighlight(highlight);
        }

        String s = nodeStyle.get(Constants.TextDecoration.KEY_TEXT_DECORATION);
        U u = new U();
        if (StringUtils.isNotBlank(s)) {
            if (Objects.equals(s,Constants.TextDecoration.VALUE_UNDERLINE)) {
                u.setVal(UnderlineEnumeration.SINGLE);
                currentRPr.setU(u);
            }else if (Objects.equals(s,Constants.TextDecoration.VALUE_LINE_THROUGH)) {
                BooleanDefaultTrue strike = new BooleanDefaultTrue();
                strike.setVal(true);
                currentRPr.setStrike(strike);
            }
        }

        String decorationLine = nodeStyle.get(Constants.TextDecoration.KEY_TEXT_DECORATION_LINE);
        if (StringUtils.isNotBlank(decorationLine)) {
            if (Objects.equals(decorationLine, Constants.TextDecoration.VALUE_UNDERLINE)) {
                String styleWavy = nodeStyle.get(Constants.TextDecoration.KEY_TEXT_DECORATION_STYLE);
                if (Objects.equals(styleWavy,Constants.TextDecoration.VALUE_STYLE_WAVY)) {
                    u.setVal(UnderlineEnumeration.WAVE);
                    currentRPr.setU(u);
                }
            }
        }

        String emphasis = nodeStyle.get(Constants.ATTR_KEY_TEXT_EMPHASIS);
        if (StringUtils.isNotBlank(emphasis)) {
            CTEm ctEm = new CTEm();
            ctEm.setVal(STEm.UNDER_DOT);
            currentRPr.setEm(ctEm);
        }

    }

    @Override
    public boolean isRepeatable() {
        return true;
    }
}
