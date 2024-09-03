package com.denisfesenko.tag.style;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.util.Map;

/**
 * 自定义样式处理器
 *
 * @author YangXinFu
 * @date 2024/9/3 16:49
 */
public interface StyleHandler {

    void handle(Map<String,String> nodeAttr, WordprocessingMLPackage wordprocessingMLPackage);

}
