package com.denisfesenko.util;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;

import java.util.List;
import java.util.function.Function;

public final class RunUtils {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private RunUtils() {
        // Private constructor to prevent instantiation
    }

    public static void createParagraph(WordprocessingMLPackage wordMLPackage, PPr pPr) {
        P paragraph = OBJECT_FACTORY.createP();
        if (pPr != null) {
            paragraph.setPPr(pPr);
        }
        wordMLPackage.getMainDocumentPart().getContent().add(paragraph);
    }

    public static P getCurrentParagraph(WordprocessingMLPackage wordMLPackage) {
        List<Object> content = wordMLPackage.getMainDocumentPart().getContent();
        if (content.isEmpty()) {
            return createAndAddParagraph(wordMLPackage);
        }
        Object lastElement = content.get(content.size() - 1);
        if (lastElement instanceof P) {
            return (P) lastElement;
        }
        return createAndAddParagraph(wordMLPackage);
    }

    public static R getCurrentRun(WordprocessingMLPackage wordMLPackage) {
        P lastElement = getCurrentParagraph(wordMLPackage);
        List<Object> paragraphContent = lastElement.getContent();
        if (paragraphContent.isEmpty()) {
            return createAndAddRun(lastElement);
        }
        Object lastElementOfParagraph = paragraphContent.get(paragraphContent.size() - 1);
        return lastElementOfParagraph instanceof R ? (R) lastElementOfParagraph : createAndAddRun(lastElement);
    }

    public static RPr getCurrentRPr(WordprocessingMLPackage wordMLPackage) {
        R run = getCurrentRun(wordMLPackage);
        if (run.getRPr() == null) {
            RPr rPr = OBJECT_FACTORY.createRPr();
            run.setRPr(rPr);
            return rPr;
        }
        return run.getRPr();
    }

    public static BooleanDefaultTrue createBooleanDefaultTrue() {
        BooleanDefaultTrue booleanDefaultTrue = OBJECT_FACTORY.createBooleanDefaultTrue();
        booleanDefaultTrue.setVal(true);
        return booleanDefaultTrue;
    }

    public static ObjectFactory getObjectFactory() {
        return OBJECT_FACTORY;
    }

    private static P createAndAddParagraph(WordprocessingMLPackage wordMLPackage) {
        return createAndAddElement(wordMLPackage.getMainDocumentPart().getContent(), ObjectFactory::createP);
    }

    private static R createAndAddRun(P paragraph) {
        return createAndAddElement(paragraph.getContent(), ObjectFactory::createR);
    }

    private static <T> T createAndAddElement(List<Object> contentList, Function<ObjectFactory, T> factoryMethod) {
        T element = factoryMethod.apply(OBJECT_FACTORY);
        contentList.add(element);
        return element;
    }
}
