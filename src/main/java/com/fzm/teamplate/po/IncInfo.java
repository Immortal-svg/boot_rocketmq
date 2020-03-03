package com.fzm.teamplate.po;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "inc_info")
public class IncInfo {

    @Id
    private String id;// 主键

    private String collName;// 业务名称

    private Long seqId;

}



