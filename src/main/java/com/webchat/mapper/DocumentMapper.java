package com.webchat.mapper;

import com.webchat.entity.Document;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DocumentMapper {

    /**
     * 查询文本内容
     * @return
     */
    @Select("SELECT id, author, submit_time, title, text FROM document")
    List<Document> getDocument();

    /**
     *
     * 添加文本内容
     * @param document
     * @return
     */
    @Insert("INSERT into document(author, submit_time, title, text)" +
            "values(#{author}, #{submitTime}, #{title}, #{text})")
    int addText(Document document);
}
