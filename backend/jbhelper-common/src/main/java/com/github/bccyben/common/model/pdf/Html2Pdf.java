package com.github.bccyben.common.model.pdf;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <li>pdfモデルのベース</li>
 * <li>pdfモデルを新規作成する場合このclassを継承</li>
 */
public abstract class Html2Pdf {

    @Setter(AccessLevel.PROTECTED)
    private ResourceLoader resourceLoader;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private List<String> fontPaths = new ArrayList<>();

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String htmlPath;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String html;

    public Html2Pdf(String htmlPath, ResourceLoader resourceLoader) throws IOException {
        this.setHtmlPath(htmlPath);
        this.setResourceLoader(resourceLoader);
        Resource resource = resourceLoader.getResource(htmlPath);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(),
                StandardCharsets.UTF_8);
        String html = FileCopyUtils.copyToString(reader);
        this.setHtml(html);
    }

    protected void set(String id, String value) {
        Document doc = Jsoup.parse(this.getHtml());
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        doc.getElementById(id).empty().appendText(value);
        this.setHtml(doc.outerHtml());
    }

}
