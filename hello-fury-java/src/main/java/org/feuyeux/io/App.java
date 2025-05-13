package org.feuyeux.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.fury.Fury;
import org.apache.fury.config.CompatibleMode;
import org.apache.fury.config.Language;

/** Hello Fury Java Example */
public class App {
  public static void main(String[] args) throws IOException {
    System.out.println("Java Fury Implementation");

    // Create output directory if it doesn't exist
    Path outputDir = Paths.get("../hello-fury-io");
    Files.createDirectories(outputDir);

    // Create a sample UserInfo object
    UserInfo userInfo = createSampleUserInfo();
    System.out.println("Created user: " + userInfo);

    // Create Fury instance configured for cross-language compatibility
    Fury fury =
        Fury.builder()
            .withLanguage(Language.JAVA)
            .withRefTracking(true)
            .withCompatibleMode(CompatibleMode.COMPATIBLE)
            .build();
    fury.register(UserInfo.class, "org.feuyeux.fury.UserInfo");

    // Serialize the user to bytes
    byte[] serialized = fury.serialize(userInfo);

    // Write the serialized data to a file
    Path outputFile = outputDir.resolve("userinfo_java.fury");
    Files.write(outputFile, serialized);
    System.out.println("User information serialized to " + outputFile);

    // Try to read serialized data from other languages
    Path defaultFile = outputDir.resolve("userinfo.fury");
    if (Files.exists(defaultFile)) {
      System.out.println("Found serialized data from other language implementation.");
      deserializeUser(fury, defaultFile);
    } else {
      System.out.println("No serialized data from other languages found yet.");
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

  private static void deserializeUser(Fury fury, Path filePath) throws IOException {
    byte[] serialized = Files.readAllBytes(filePath);
    UserInfo userInfo = (UserInfo) fury.deserialize(serialized);
    System.out.println("Deserialized user: " + userInfo);
  }
}
