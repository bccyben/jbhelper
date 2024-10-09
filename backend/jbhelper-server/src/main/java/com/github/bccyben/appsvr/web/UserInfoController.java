package com.github.bccyben.appsvr.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.groups.Default;
import com.github.bccyben.common.domain.SimplePage;
import com.github.bccyben.common.domain.UserMasterPageRequest;
import com.github.bccyben.common.model.common.UserInfoModel;
import com.github.bccyben.common.model.spec.UserMasterSpec;
import com.github.bccyben.common.service.UserInfoService;
import com.github.bccyben.common.validator.group.Create;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("")
    @Operation(summary = "ユーザ一覧取得")
    @ApiResponse(responseCode = "200", description = "成功")
    @Parameter(name = "filter", in = ParameterIn.QUERY, description = "ユーザ一名（暗号化）で検索する", example = "username")
    @Parameter(name = "userId", in = ParameterIn.QUERY, description = "ユーザーID", example = "joinuserid")
    @Parameter(name = "roleList", in = ParameterIn.QUERY, description = "役割リスト", schema = @Schema(type = "array"))
    @Parameter(name = "sort", in = ParameterIn.QUERY, description = "ソート", schema = @Schema(type = "string", allowableValues = {
            "userName", "updateTime" }))
    @Parameter(name = "order", in = ParameterIn.QUERY, description = "オーダー", schema = @Schema(type = "string", allowableValues = {
            "DESC", "ASC" }))
    @Parameter(name = "pageNum", in = ParameterIn.QUERY, description = "Zero-based page index", example = "0", schema = @Schema(type = "integer"))
    @Parameter(name = "pageSize", in = ParameterIn.QUERY, description = "ページサイズ", example = "20", schema = @Schema(type = "integer"))
    public SimplePage<UserInfoModel> findAll(
            @Parameter(hidden = true) UserMasterSpec spec,
            @Parameter(hidden = true) UserMasterPageRequest page) {
        return userInfoService.findAll(spec, page);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "ユーザ一詳細取得")
    @ApiResponse(responseCode = "200", description = "取得成功")
    public UserInfoModel findById(
            @Parameter(required = true, description = "userId", example = "9a8c17694a9844abbf434f44d0c97872") @PathVariable String userId) {
        return userInfoService.findById(userId);
    }


    @PostMapping("")
    @Operation(summary = "ユーザ新規作成")
    @ApiResponse(responseCode = "201", description = "成功")
    public ResponseEntity<Void> create(@RequestBody @Validated({ Create.class, Default.class }) UserInfoModel model) {
        userInfoService.create(model);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("")
    @Operation(summary = "ユーザ編集")
    @ApiResponse(responseCode = "201", description = "成功")
    public void update(@RequestBody @Validated UserInfoModel model){
        userInfoService.update(model);
    }

}
