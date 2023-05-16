package com.lar.file.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class MinioServe {

  public void upload(String filePath) throws FileNotFoundException {
    // 判断是不是网络路径,还是本地路径
    File file = new File(filePath);
    String name = file.getName();
    InputStream inputStream = new FileInputStream(file);
    long size = file.length();
    this.upload(inputStream, size, name);
  }

  public void upload(File file) throws FileNotFoundException {
    InputStream inputStream = new FileInputStream(file);
    long size = file.length();
    String name = file.getName();

    this.upload(inputStream, size, name);
  }

  public void upload(InputStream inputStream, long size, String name) {
    try {
      // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象

      MinioClient minioClient =
          MinioClient.builder()
              .endpoint("http://1.116.187.129:9000")
              .credentials("UZU7OP99NJQS7K2WTG6P", "0+NgqgJkpk54eA9vinA+l1SlzdhHeUsIBEihNKV3")
              .build();

      // 检查存储桶是否已经存在
      boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("larry").build());
      if (found) {
        System.out.println("larry exists");
      } else {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket("larry").build());
      }
      // 使用putObject上传一个文件到存储桶中。
      minioClient.putObject(
          PutObjectArgs.builder().bucket("larry").object(name).stream(inputStream, size, -1)
              .build());

    } catch (MinioException e) {
      System.out.println("Error occurred: " + e);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    }
  }

  public void upload(MultipartFile file) throws IOException {
    long size = file.getSize();
    String name = file.getOriginalFilename();
    InputStream inputStream = file.getInputStream();
    this.upload(inputStream, size, name);
  }
}
