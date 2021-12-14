package com.webchat;

import com.webchat.entity.Document;
import com.webchat.mapper.DocumentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class ChatApplicationTests {

    @Autowired
    private DocumentMapper documentMapper;


    @Test
    void getDocument(){
        List<Document> document = documentMapper.getDocument();
        System.out.println(document);
    }

}

