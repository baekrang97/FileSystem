package com.cydinfo.fudms.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttachmentVo {

    private int id;
    private String name;
    private String description;
    private String hashTag;
    private String createDate;
    private int systemId;
    private String status;
    private String userName;

}
