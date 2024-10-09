package com.github.bccyben.common.service;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.github.bccyben.common.model.pdf.Html2Pdf;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class PdfService {

    public ResponseEntity<Resource> getPdf(Html2Pdf model) throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // add font
        for (var fontPath : model.getFontPaths()) {
            renderer.getFontResolver().addFont(
                    fontPath, BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
        }
        renderer.setDocumentFromString(model.getHtml());
        renderer.layout();
        renderer.createPDF(out);
        out.close();
        renderer.finishPDF();
        Resource resource = new ByteArrayResource(out.toByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/octet-stream"))
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + UriUtils.encode(
                                "pdf.pdf",
                                StandardCharsets.UTF_8.name())
                                + "\"")
                .body(resource);
    }

}
