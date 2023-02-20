package org.njupt.controller;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddLinkDto;
import org.njupt.domain.dto.UpdateLinkDto;
import org.njupt.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.getLinkList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getDetailLink(@PathVariable Long id){
        return linkService.detailLink(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody UpdateLinkDto updateLinkDto){
        return linkService.updateLink(updateLinkDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }

}
