package com.github.bccyben.common.model.pdf;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * pdf実装モデルサンプル用
 */
@EqualsAndHashCode(callSuper = false)
public class SamplePdf extends Html2Pdf {

    @Getter
    private String user;

    public SamplePdf(ResourceLoader resourceLoader) throws IOException {
        super("classpath:pdfhtml/sample.html", resourceLoader);
        this.getFontPaths().add("pdfhtml/fonts/ipag.ttf");
    }

    public void setUser(String user) {
        this.user = user;
        this.set("user", user);
    }

}
