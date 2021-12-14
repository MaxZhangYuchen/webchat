package com.webchat.controller;

import com.webchat.entity.Document;
import com.webchat.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class DocumentController {
    @Resource
    private DocumentService documentService;

    @RequestMapping("/edit")
    public String getDocument(){
        return "edit";
    }



    @RequestMapping("/addText")
    public String addText(Document document){
        documentService.addText(document);
        return "redirect:/edit";
    }


}
