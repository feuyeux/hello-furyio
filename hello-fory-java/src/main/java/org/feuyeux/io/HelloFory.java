package org.feuyeux.io;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.fory.Fory;
import org.apache.fory.config.Language;
import org.apache.fory.logging.LoggerFactory;

@Slf4j
public class HelloFory {
  public static void main(String[] args) throws IOException {
    // Enable SLF4J logging for Fory (same as Fory uses internally)
    LoggerFactory.useSlf4jLogging(true);

    // Create output directory if it doesn't exist
    Path outputDir = Paths.get("../workspace");
    Files.createDirectories(outputDir);

    // Create a sample UserInfo object
    UserInfo userInfo = createSampleUserInfo();
    log.info("Created user: {}", userInfo);
    // Create fory instance configured for cross-language compatibility
    Fory fory = Fory.builder().withLanguage(Language.XLANG).withRefTracking(true).build();
    fory.register(UserInfo.class);

    // Serialize the user to bytes
    byte[] serialized = fory.serialize(userInfo);

    // Write the serialized data to a file
    Path outputFile = outputDir.resolve("userinfo_java.fory");
    Files.write(outputFile, serialized);
    log.info("Serialized to {}", outputFile);

    // Try to read serialized data from other languages
    String[] langs = {"java", "go", "js", "python", "rust"};
    for (String lang : langs) {
      Path langFile = outputDir.resolve("userinfo_" + lang + ".fory");
      if (Files.exists(langFile)) {
        try {
          UserInfo userInfoFromFile = deserializeUser(fory, langFile);
          log.info("Deserialized [{}] user info from {}:{}", lang, langFile, userInfoFromFile);
        } catch (Exception e) {
          log.error("Failed to deserialize [{}]", lang, e);
        }
      }
    }
  }

  private static UserInfo createSampleUserInfo() {
    UserInfo info = new UserInfo();
    info.setUserId(12345L);
    info.setName("John Doe");
    info.setAge(30);
    info.setEmails(Arrays.asList("john.doe@example.com", "johndoe@gmail.com"));

    HashMap<String, String> props = new HashMap<>();
    props.put("city", "San Francisco");
    props.put("country", "USA");
    props.put("role", "Developer");
    info.setProperties(props);

    info.setScores(new ArrayList<>(Arrays.asList(85, 90, 78, 92)));
    info.setActive(true);
    info.setCreatedAt(System.currentTimeMillis() / 1000);
    info.setAvatar(new byte[] {0, 1, 2, 3, 4, 5});

    return info;
  }

  private static UserInfo deserializeUser(Fory fory, Path filePath) throws IOException {
    byte[] serialized = Files.readAllBytes(filePath);
    return (UserInfo) fory.deserialize(serialized);
  }
}

@Data
class UserInfo implements Serializable {
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
