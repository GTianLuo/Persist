package com.rui.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class community {
    private String uid;
    private String headUrl;
    private String userName;
    private String communityUid;
    private List<Object> picture;
    private String content;
    private String beganTime;
}
