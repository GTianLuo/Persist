package com.rui.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class runRecord {
   private String uid;
   private String runWhen;
   private String runTime;
   private String distance;
   private String day;
}
