package com.github.bccyben.common.model.csv;

import com.github.bccyben.common.annotation.EnableNotDisplayHeaderCsv;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@EnableNotDisplayHeaderCsv
public class ProductTemplate extends ProductCsv {
    static public ProductTemplate createTemplate() {
        var template = new ProductTemplate();
        template.setProductName("サンプル商品");
        template.setInfo("テンプレートinfo");
        return template;
    }
}
