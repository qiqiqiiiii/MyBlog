package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "上传",description = "上传相关接口")
public class UploadController {
    //private String
    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    @ApiOperation(value="上传头像",notes="实现上传头像")
    @ApiImplicitParam(name="img",value="上传的图片")
    public ResponseResult upload(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
