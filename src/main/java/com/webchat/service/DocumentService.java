package com.webchat.service;

import com.webchat.entity.Document;
import com.webchat.mapper.DocumentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocumentService implements DocumentMapper{

    @Resource
    private DocumentMapper documentMapper;

    @Override
    public List<Document> getDocument(){
        return documentMapper.getDocument();
    }

    @Override
    public int addText(Document document) {
        return documentMapper.addText(document);
    }
}
