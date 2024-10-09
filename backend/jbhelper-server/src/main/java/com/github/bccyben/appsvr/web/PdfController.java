package com.github.bccyben.appsvr.web;

import io.swagger.v3.oas.annotations.Parameter;
import com.github.bccyben.common.model.pdf.SamplePdf;
import com.github.bccyben.common.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    /**
     * 実際の実装をする場合、このDIはservices layerでやるべき
     */
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * PDF作成、サンプル用
     *
     * @return
     * @throws IOException
     */
    @GetMapping("")
    public ResponseEntity<Resource> getPdf(
            @Parameter(required = true, description = "ユーザ名") @RequestParam("user") String user) throws IOException {
        SamplePdf model = new SamplePdf(resourceLoader);
        model.setUser(user);
        return pdfService.getPdf(model);
    }
}
