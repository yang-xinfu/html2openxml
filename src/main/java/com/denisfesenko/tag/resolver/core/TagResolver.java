package com.denisfesenko.tag.resolver.core;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.nodes.Node;

/**
 * 标签处理器
 * 负责 node(jsoup) 与 office open xml (docx4j) 的转换。
 */
public interface TagResolver {


    /**
     * 转换的核心逻辑
     * @param node html 的节点
     * @param wordMLPackage word 的包文件，通过此类可以获取 word 的结构
     */
    void convert(Node node, WordprocessingMLPackage wordMLPackage);

    /**
     * 是否可重复执行
     */
    default boolean isRepeatable() {return false;};
}
