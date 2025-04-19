package com.lar.system.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/onedrive")
public class OneDriveController {
    // 获取授权码
    @GetMapping("/redirectUri")
    public String redirectUri(String code) {
        System.out.println(code);
        return code;
    }

    // 上传文件，在文档中心里面新建一个文档使用
    @PostMapping("/upload")
    public String upload(@RequestBody FileData params) throws IOException {
        String token = "";
        token = params.getToken();
        // 上传到root下的uploadfile文件夹
        String uploadUrl = "https://graph.microsoft.com/v1.0/me/drive/root:/uploadfile/";
        File file = new File("D:\\Users\\JNPF\\Desktop\\project\\toolserve\\src\\main\\resources\\config\\1.docx");
        uploadUrl = uploadUrl + "2.docx:/content";

        byte[] bytes = FileUtils.readFileToByteArray(file);
        String body = HttpRequest.put(uploadUrl)
                .header(Header.AUTHORIZATION, token)
                .header(Header.CONTENT_TYPE, "application/octet-stream")
                .body(bytes)
                .execute().body();
        return body;
    }
    String token = "EwBIBMl6BAAUBKgm8k1UswUNwklmy2v7U/S+1fEAAWjSCcaxivkhduygEykxdOEAhtr60K8eUDSsOnkaJnaBCZutp8hLXb6u7gNSOJXSYxKRt+7S9xGUKAW4Ex/okzlyp22h/kyCw0DKc2pPBibEeZWnJt5aYcuc9qj/HhuV2JhviRkpUDHsGGxoNu7pyUwcd46I24Cd0Ji6gONwpX067dA5d03GRfz7Yllvka9BJufb9WS3+zHuywMuaHvH9IVbryMepw6IJeRkkYIGDxITllqqUV63YdwW7E8Ahga+4FUTE0P/l9N7nGM27vNg4NIyyCZ5SEtMazCIDB3pnm+hWG6P7fSaWE/02vohD4bgI5S4aS05FjHhB94SJX0aHIoQZgAAELgdDaIrQ8OsoebHRJzQk5gQA8s2xGm1YWxs8Qy6eayD0zfyPb0F2FN+sV6tIzdmOC3ulpBDinkUYQ1QnH0rCz03MUYHnCM/NksWQRTNT3VWR3bCebOFnXx9Hi+MKu5Y2lS02MXpYGJZbT1/QVuK1SedIZVkrQ0Glp2hIBpT8OV9q7RWH6uFLNJWpaxC7KCdIXxHE62AjKtyq3UtnLdgbQQXUUKkwTWTzuhWfo5OAn4kPEpQ9p4tW78DuQXFvK7MVeMYlGs7yyvLCv/v5QJLpSzpXLfKNsTHj0+mFxCoCNW3ts7o9JSce52W2o2hEJczArvs3ICaW5VRnMTfHYC22AZ3FBbZU0IjSOpeqwdTHlMVIiuCItdIhNcII9aoVaVXXNsR3OC7y57XYoT+WGbpbJzZ1vnuQrWh0IWu7fzghC7jkHH2HOnmIejouPW9lX6UoI6bvilpobQFExhmu5xNByFjz0hyuft3j8z9q4iULZdmDwtkTltNhugkd9qAc/RiACZX+gJdNr6IIy6kJFKXgoefJRq/1YwAS4GK5RftmIxPdaSOFuNebx/g0L4sd6r7ax05Ehxeor27AbbzEfRDOzn4i6YIeYdgg3ttiIc44IdsFQUxf1bVLjwIun5D2sYpHM0MTYFqgA68yabrKqo8/N1cvu+XGREd3skNRUfhI1WwBqeTQDwNRYiYzwPrOjmEv6sx/LCdhvA+jPCPvG0EH5zD832lrgX7mQUSbUGGlMnu6sEWE0Gx1+1MdpX52t5l6g5JZLqmOMXn0OY6ZdGgU1kbStiw8iBzAiMuUY+I61ZYln99g1H5sgtTCyCKbWWDN02IWz7ugysnZQAndcLsNubRQLkKd75AxG7sGUL27SBDw7Y97Fvgv/9QZAT6jNTd/y4aQJYPkA0TGA2GmTswjCH+XeQeMozkBHlxWkpIpBQVzeE7P6DfEBT0IXEZqX1U1CrWK+fUaVduD/XxlsV1PnSgE7FwiED5FpBX0nk5hwkJ6LRcgkFYvqnwZBopbi3ITqge/za7VpptJw7mdeAAE9NLGIN1vXqLqVoZlfkwGQKH0WMuAw==";
    @PostMapping("/download2/{itemId}")
    public void download2(@PathVariable("itemId")  String itemId, HttpServletResponse response) throws IOException {
        String downloadUrl = "https://graph.microsoft.com/v1.0/me/drive/items/" + itemId + "/content";
        @Cleanup
        HttpResponse execute = HttpRequest.get(downloadUrl)
                .header(Header.AUTHORIZATION, token)
                .execute();
//        try (OutputStream outputStream = response.getOutputStream()) {
//            response.setContentType("application/octet-stream");
//            IoUtil.copy(execute.bodyStream(), outputStream, IoUtil.DEFAULT_BUFFER_SIZE);
//        }
        //  2. 将响应体写入临时文件（路径可自定义）
                File tempFile = FileUtil.writeFromStream(
                    execute.bodyStream(),
                    FileUtil.file("D:\\Users\\JNPF\\Desktop\\project\\toolserve\\src\\main\\resources\\config\\", "2.docx") // 保存路径示例
                );
    }
    // 下载文件，通过id从onedrive下载文件内容回来
    @PostMapping("/download/{itemId}")
    public void download(@PathVariable("itemId")  String itemId, HttpServletResponse response) throws IOException {
        String downloadUrl = "https://graph.microsoft.com/v1.0/me/drive/items/" + itemId + "/content";
        String token = "xxx";

        File file = new File("");
        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + file.getName());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    // 获取onedrive的文件列表内容
    @PostMapping("/list")
    public String list(@RequestBody FileData params) throws IOException {

        return "";
    }


}
