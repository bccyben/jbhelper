package com.github.bccyben.appsvr.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.github.bccyben.common.service.SesService;

@RestController
@RequestMapping("/api/v1/ses")
public class SesController {

    @Autowired(required = false)
    private SesService sesService;

    @GetMapping("/send")
    @Operation(summary = "メール送信")
    @ApiResponse(responseCode = "202", description = "送信中")
    public ResponseEntity<Void> sendMail(String to) {
        sesService.sendAsync(to, "test", "this is a test");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
