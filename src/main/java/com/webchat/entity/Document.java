package com.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    private int id;
    private String author;
    private Date submitTime;
    private String title;
    private String text;


}
