package com.lar.system.file;

import cn.hutool.http.HttpUtil;
import cn.xuyanwu.spring.file.storage.Downloader;
import cn.xuyanwu.spring.file.storage.FileInfo;
import cn.xuyanwu.spring.file.storage.FileStorageService;
import com.lar.common.util.FileCryptoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

@RestController
@RequestMapping(value = "/file")
@AllArgsConstructor
@Slf4j
public class FileController {
    // @Autowired
    // private FileService fileService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FileCryptoUtil fileCryptoUtil;
    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(value = "/preview", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] preview(String fileVersionId) throws IOException {

        File file = new File("C:\\Users\\lg\\Desktop\\vue\\xlj\\static\\1.jpeg");
        @Cleanup FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }


    /**
     * 上传文件
     *
     * @return
     */
    @PostMapping("/upload")
    public FileInfo upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        byte[] bytesCrypt = FileCryptoUtil.encryptFile(bytes);
        FileInfo upload = fileStorageService.of(bytes).setPath("i18n/").setSaveFilename(multipartFile.getOriginalFilename()).upload();
        return upload;
    }

    @GetMapping("/download")
    public byte[] download(@RequestParam(value = "url") String url) {
        FileInfo fileInfo = fileStorageService.getFileInfoByUrl(url);
        Downloader download = fileStorageService.download(fileInfo);
        return download.bytes();
    }

    @GetMapping("/downloadFile")
    public void kkfileFiledownload(@RequestParam("url") String downloadUrl, HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            // 下载并解密文件
            byte[] encryptedBytes = HttpUtil.downloadBytes(downloadUrl);
            byte[] decryptedBytes = fileCryptoUtil.decryptFile(encryptedBytes);

            // 设置响应头
            URLConnection conn = new URL(downloadUrl).openConnection();
            String contentType = conn.getContentType();
            String fileName = "downloadFile";
            int lastSlashIndex = downloadUrl.lastIndexOf('/');
            if (lastSlashIndex != -1) {
                fileName = downloadUrl.substring(lastSlashIndex + 1);
            }

            response.setContentType(contentType != null ? contentType : "application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("utf-8"), "utf-8"));
            response.setContentLength(decryptedBytes.length);

            // 直接写入解密后的字节流
            os.write(decryptedBytes);
            os.flush();
        } catch (Exception e) {
            log.error("kkfile文件下载失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 上传文件，成功返回文件 url
     */
    @PostMapping("/upload2")
    public String upload2(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("upload/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                .putAttr("role", "admin") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }

    /**
     * 上传图片，成功返回文件信息
     * 图片处理使用的是 https://github.com/coobird/thumbnailator
     */
    @PostMapping("/upload-image")
    public FileInfo uploadImage(MultipartFile file) {
        return fileStorageService.of(file)
                .image(img -> img.size(1000, 1000))  //将图片大小调整到 1000*1000
                .thumbnail(th -> th.size(200, 200))  //再生成一张 200*200 的缩略图
                .upload();
    }

    /**
     * 上传文件到指定存储平台，成功返回文件信息
     */
    @PostMapping("/upload-platform")
    public FileInfo uploadPlatform(MultipartFile file) {
        return fileStorageService.of(file)
                .setPlatform("aliyun-oss-1")    //使用指定的存储平台
                .upload();
    }

}
