package com.rui.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class target {
    private String targetUid;
    private String uid;
    private int type;
    private boolean isWholeDay;
    private String beganTime,endTime;
    private String headLine;
    private String remark;
    private boolean isDone;

}
