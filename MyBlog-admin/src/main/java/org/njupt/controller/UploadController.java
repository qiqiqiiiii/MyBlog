package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.service.impl.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "上传接口",description = "处理上传的接口")
public class UploadController {
    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    @ApiOperation(value="上传头像",notes="实现上传头像")
    @ApiImplicitParam(name="img",value="上传的图片")
    public ResponseResult upload(MultipartFile img){
        return ossUploadService.uploadImg(img);
    }
}
