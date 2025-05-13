package org.feuyeux.io;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class UserInfo implements Serializable {
  private Long userId;
  private String name;
  private Integer age;
  private List<String> emails;
  private Map<String, String> properties;
  private List<Integer> scores;
  private Boolean active;
  private Long createdAt;
  private byte[] avatar;
}
