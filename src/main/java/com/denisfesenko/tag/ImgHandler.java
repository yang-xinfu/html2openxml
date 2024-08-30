package com.denisfesenko.tag;

import com.denisfesenko.core.TagHandler;
import com.denisfesenko.util.Constants;
import com.denisfesenko.util.ConverterUtils;
import com.denisfesenko.util.HttpUtil;
import com.denisfesenko.util.RunUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.UnitsOfMeasurement;
import org.docx4j.dml.CTPositiveSize2D;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.DefaultXmlPart;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBElement;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 图片处理器
 *
 * @author YangXinFu
 * @date 2024/8/29 17:10
 */
public class ImgHandler implements TagHandler {
    @Override
    public void handleTag(Node node, WordprocessingMLPackage wordMLPackage) {

        Map<String, String> nodeStyle = ConverterUtils.getNodeStyle(node);

        if (Objects.equals(node.nodeName(),"svg")) {

            String s = node.outerHtml();
            byte[] svgBase64 = Base64.getEncoder().encode(s.getBytes());

            Element imgEle = new Element("img");
            imgEle.attr("src","data:image/svg+xml;base64,"+new String(svgBase64));
            String width = nodeStyle.get(Constants.WIDTH);
            if (StringUtils.isNotBlank(width)) {
                Double widthPx = ConverterUtils.getPxNum(width);
                nodeStyle.put(Constants.WIDTH, widthPx + "px");
            }

            String height = nodeStyle.get(Constants.HEIGHT);
            if (StringUtils.isNotBlank(height)) {
                Double heightPx = ConverterUtils.getPxNum(height);
                nodeStyle.put(Constants.HEIGHT, heightPx + "px");
            }

            node.replaceWith(imgEle);
            node = imgEle;
        }

        R currentRun = RunUtils.getCurrentRun(wordMLPackage);

        String srcValue = node.attr(Constants.ATTR_KEY_SRC);
        // 设置图片的属性（如宽度、高度等），这里以固定宽度和高度为例
        // todo
        String extension;
        File imageFile = null;

        UUID randomFileName = UUID.randomUUID();

        if (StringUtils.isBlank(srcValue)) {
            return;
        }

        BinaryPartAbstractImage imagePart;

        try {
            if (srcValue.contains("base64")) {

                String[] split = srcValue.split(",");
                if (split.length != 2) {
                    return;
                }

                String imageType = split[0];
                String[] imageTypeStrs = imageType.split(":");
                extension = imageTypeStrs[1].trim();
                if (Objects.equals(extension,"image/svg+xml;base64")){
                    extension = "svg";
                }

                String img64 = split[1];
                byte[] imageBytes = Base64.getDecoder().decode(img64);

                imageFile = new File(randomFileName + "." + extension);
                FileUtils.writeByteArrayToFile(imageFile, imageBytes);

            } else {
                extension = FilenameUtils.getExtension(srcValue);
                imageFile = new File(randomFileName + "." + extension);
                HttpUtil.downloadFile(srcValue, imageFile);
            }


            Drawing drawing = null;
            if (Objects.equals(extension,"svg")) {

                InputStream is = Files.newInputStream(imageFile.toPath());

                DefaultXmlPart imagePartSvg = new DefaultXmlPart(new PartName("/word/media/"+randomFileName+".svg"));
                imagePartSvg.setRelationshipType("http://schemas.openxmlformats.org/officeDocument/2006/relationships/image");
                imagePartSvg.setContentType(new ContentType("image/svg+xml"));
                imagePartSvg.setDocument(is);
                Relationship rel = wordMLPackage.getMainDocumentPart().addTargetPart(imagePartSvg);
                drawing = createImageInline(rel);
                Inline inline = (Inline)drawing.getAnchorOrInline().get(0);
                CTPositiveSize2D extent = inline.getExtent();

                Double width = ConverterUtils.getPxNum(nodeStyle.get(Constants.WIDTH));
                Double height = ConverterUtils.getPxNum(nodeStyle.get(Constants.HEIGHT));

                if (Objects.nonNull(width)) {
                    int widthTwip = UnitsOfMeasurement.pxToTwip(Float.parseFloat(String.valueOf(width)));
                    long widthTwipEMU = UnitsOfMeasurement.twipToEMU(widthTwip);
                    extent.setCx(widthTwipEMU);
                }

                if (Objects.nonNull(height)) {
                    int heightTwip = UnitsOfMeasurement.pxToTwip(Float.parseFloat(String.valueOf(height)));
                    long heightTwipEMU = UnitsOfMeasurement.twipToEMU(heightTwip);
                    extent.setCy(heightTwipEMU);
                }

            }else {
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                int height1 = bufferedImage.getHeight();
                int width1 = bufferedImage.getWidth();
                Double width = ConverterUtils.getPxNum(nodeStyle.get(Constants.WIDTH));
                Double height = ConverterUtils.getPxNum(nodeStyle.get(Constants.HEIGHT));
                ConverterUtils.ScaleResult scaleResult = ConverterUtils.resizeImages((double) width1, (double) height1, width, height);


                long l = RandomUtils.nextLong();
                int l2 = RandomUtils.nextInt();

                double heightTwip = UnitsOfMeasurement.pxToTwipDouble(scaleResult.getHeightPx());
                long heightEmu = UnitsOfMeasurement.twipToEMU(heightTwip);
                double widthTwip = UnitsOfMeasurement.pxToTwipDouble(scaleResult.getWidthPx());
                long widthEmu = UnitsOfMeasurement.twipToEMU(widthTwip);
                imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, imageFile);
                Inline inline = imagePart.createImageInline(UUID.randomUUID().toString(), "", l, l2, widthEmu,heightEmu,false);
                drawing = RunUtils.getObjectFactory().createDrawing();
                drawing.getAnchorOrInline().add(inline);
            }

            currentRun.getContent().add(drawing);

            ObjectFactory factory = RunUtils.getObjectFactory();

            P currentParagraph = RunUtils.getCurrentParagraph(wordMLPackage);
            PPr pPr = currentParagraph.getPPr();
            if (Objects.isNull(pPr)) {
                pPr = factory.createPPr();
                currentParagraph.setPPr(pPr);
            }
            PPrBase.TextAlignment pPrBaseTextAlignment = factory.createPPrBaseTextAlignment();
            pPrBaseTextAlignment.setVal(JcEnumeration.CENTER.value());
            pPr.setTextAlignment(pPrBaseTextAlignment);
            currentParagraph.setPPr(pPr);

            FileUtils.delete(imageFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Drawing createImageInline(Relationship rel) throws Exception {


        String ml =
                "<w:drawing " + namespaces + ">"
                        + "<wp:inline distB=\"0\" distL=\"0\" distR=\"0\" distT=\"0\">"
                        + "<wp:extent cx=\"1019175\" cy=\"1581150\"/>"
                        + "<wp:effectExtent b=\"0\" l=\"0\" r=\"9525\" t=\"0\"/>"
                        + "<wp:docPr id=\"2\" name=\"Picture 2\"/>"
                        + "<wp:cNvGraphicFramePr>"
                        + "<a:graphicFrameLocks noChangeAspect=\"true\"/>"
                        + "</wp:cNvGraphicFramePr>"
                        + "<a:graphic>"
                        + "<a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                        + "<pic:pic>"
                        + "<pic:nvPicPr>"
                        + "<pic:cNvPr id=\"1\" name=\"\"/>"
                        + "<pic:cNvPicPr/>"
                        + "</pic:nvPicPr>"
                        + "<pic:blipFill>"
                        + "<a:blip>"
                        + "<a:extLst>"
                        + "<a:ext uri=\"{96DAC541-7B7A-43D3-8B79-37D633B846F1}\">"
                        + "<asvg:svgBlip xmlns:asvg=\"http://schemas.microsoft.com/office/drawing/2016/SVG/main\" r:embed=\"" + rel.getId() + "\"/>"
                        + "</a:ext>"
                        + "</a:extLst>"
                        + "</a:blip>"
                        + "<a:stretch>"
                        + "<a:fillRect/>"
                        + "</a:stretch>"
                        + "</pic:blipFill>"
                        + "<pic:spPr>"
                        + "<a:xfrm>"
                        + "<a:off x=\"0\" y=\"0\"/>"
                        + "<a:ext cx=\"1019175\" cy=\"1581150\"/>"
                        + "</a:xfrm>"
                        + "<a:prstGeom prst=\"rect\">"
                        + "<a:avLst/>"
                        + "</a:prstGeom>"
                        + "</pic:spPr>"
                        + "</pic:pic>"
                        + "</a:graphicData>"
                        + "</a:graphic>"
                        + "</wp:inline>"
                        + "</w:drawing>";

        Drawing drawing =  (Drawing)org.docx4j.XmlUtils.unmarshalString(ml);

        return drawing;
    }


    final static String namespaces = " xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" "
            + "xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
            + "xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" "
            + "xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" "
            + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" "
            ;
}
