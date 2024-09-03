package com.denisfesenko.tag.resolver.core;

import com.denisfesenko.tag.resolver.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 标签解析器工厂
 */
public class TagResolverFactory {

    public Map<String, TagResolver> createTagResolverMap() {
        Map<String, TagResolver> tagResolverMap = new HashMap<>();
        tagResolverMap.put("font", new FontResolver());
        tagResolverMap.put("table", new TableResolver());
        tagResolverMap.put("sub", new SubResolver());
        tagResolverMap.put("sup", new SupResolver());
        tagResolverMap.put("u", new UnderlineResolver());
        tagResolverMap.put("span", new SpanResolver());
        tagResolverMap.put("br", new BreakResolver());
        tagResolverMap.put("pb", new PageBreakResolver());
        tagResolverMap.put("p", new ParagraphResolver());
        tagResolverMap.put("b", new BoldResolver());
        tagResolverMap.put("strong", new BoldResolver());
        tagResolverMap.put("i", new ItalicResolver());
        tagResolverMap.put("em", new ItalicResolver());
        tagResolverMap.put("#text", new PlainTextResolver());
        tagResolverMap.put("s", new SResolver());
        tagResolverMap.put("img", new ImgResolver());
        tagResolverMap.put("svg", new ImgResolver());
        return tagResolverMap;
    }
}
