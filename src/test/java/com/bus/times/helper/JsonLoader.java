package com.bus.times.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.file.Files.readAllBytes;
import static org.springframework.util.ResourceUtils.getFile;

public class JsonLoader {

  public static String loadJsonFile(String path) throws IOException {
    File file = getFile("classpath:" + path);
    return new String(readAllBytes(file.toPath()));
  }

  public static <T> T mapJsonFileToObject(String path, Class<T> responseType) throws IOException {
    File file = getFile("classpath:" + path);
    InputStream input = new FileInputStream(file);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(input, responseType);
  }
}
