package com.denisfesenko.converter;

import com.denisfesenko.tag.resolver.core.TagResolver;
import com.denisfesenko.tag.resolver.core.TagResolverFactory;
import com.denisfesenko.tag.resolver.PlainTextResolver;
import com.denisfesenko.tag.resolver.TableResolver;
import com.denisfesenko.util.RunUtils;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Html to office open xml 的转换器
 */
public class HtmlToOpenXMLConverter {

    private final Map<String, TagResolver> tagResolverMap;
    private final Set<TagResolver> tagResolvers;


    public HtmlToOpenXMLConverter() {
        tagResolverMap = new TagResolverFactory().createTagResolverMap();
        tagResolvers = new HashSet<>();
    }


    public WordprocessingMLPackage convert(String html) throws InvalidFormatException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        return convert(html, wordMLPackage);
    }

    public WordprocessingMLPackage convert(String html, WordprocessingMLPackage wordMLPackage) {
        Document document = Jsoup.parseBodyFragment(html);
        traverseDocument(document, wordMLPackage);
        return wordMLPackage;
    }

    private void traverseDocument(Document document, WordprocessingMLPackage wordMLPackage) {
        document.traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                TagResolver tagResolver = tagResolverMap.get(node.nodeName());
                if (tagResolver != null) {
                    if (tagResolver instanceof PlainTextResolver) {
                        RunUtils.getCurrentParagraph(wordMLPackage).getContent().add(RunUtils.getObjectFactory().createR());
                        tagResolvers.forEach(handler -> handler.convert(node, wordMLPackage));
                        tagResolver.convert(node, wordMLPackage);
                    } else if (tagResolver instanceof TableResolver) {
                        ((TableResolver) tagResolver).addConverter(HtmlToOpenXMLConverter.this).convert(node, wordMLPackage);
                        //prevent of second convert
                        node.remove();
                    } else if (tagResolver.isRepeatable()) {
                        tagResolvers.add(tagResolver);
                    } else {
                        tagResolver.convert(node, wordMLPackage);
                    }
                }
            }

            @Override
            public void tail(Node node, int depth) {
                TagResolver tagResolver = tagResolverMap.get(node.nodeName());
                if (!tagResolvers.isEmpty()) {
                    tagResolvers.remove(tagResolver);
                }
            }
        });
    }
}
