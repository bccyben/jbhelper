package com.github.bccyben.appsvr.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.exceptions.CsvException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.github.bccyben.common.model.csv.ProductCsv;
import com.github.bccyben.common.model.csv.ProductTemplate;
import com.github.bccyben.common.service.CsvService;

@RestController
@RequestMapping("/api/v1/csv")
public class CsvController {
    @Autowired
    private CsvService csvService;

    @GetMapping("/template")
    @Operation(summary = "テンプレートCSVを取得")
    @ApiResponse(responseCode = "200", description = "成功")
    public ResponseEntity<Resource> getSample() throws CsvException, IOException {
        return csvService.writeToCsv(new ArrayList<>(List.of(ProductTemplate.createTemplate())), "テンプレート",
                ProductTemplate.class);
    }

    @GetMapping("/dummy")
    @Operation(summary = "ダミーCSVを取得")
    @ApiResponse(responseCode = "200", description = "成功")
    public ResponseEntity<Resource> getDummy() throws CsvException, IOException {
        ProductCsv csv = new ProductCsv("p01", "ダミー", "これはダミーです", LocalDate.of(2023, 1, 1));
        return csvService.writeToCsv(new ArrayList<>(List.of(csv)), "ダミー",
                ProductCsv.class);
    }

    @PostMapping("/load")
    @RequestMapping(path = "/load", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "csvファイルを読み込む")
    @ApiResponse(responseCode = "200", description = "成功")
    public List<ProductCsv> readCsv(
            @Parameter(required = true, description = "csvファイル") @RequestParam("file") MultipartFile file)
            throws IOException {
        return csvService.readToEntities(file, ProductCsv.class);
    }
}
