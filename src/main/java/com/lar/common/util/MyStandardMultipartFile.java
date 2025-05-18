package com.lar.common.util;

import lombok.Getter;
import org.apache.catalina.core.ApplicationPart;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyStandardMultipartFile implements MultipartFile, Serializable {

    @Getter
    private final ApplicationPart part;

    private final String filename;

    public MyStandardMultipartFile(ApplicationPart part, String filename) {
        this.part = part;
        this.filename = filename;
    }

    @Override
    public String getName() {
        return this.part.getName();
    }

    @Override
    public String getOriginalFilename() {
        return this.filename;
    }

    @Override
    public String getContentType() {
        return this.part.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return (this.part.getSize() == 0);
    }

    @Override
    public long getSize() {
        return this.part.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileCopyUtils.copyToByteArray(this.part.getInputStream());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.part.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        this.part.write(dest.getPath());
        if (dest.isAbsolute() && !dest.exists()) {
            // Servlet Part.write is not guaranteed to support absolute file paths:
            // may translate the given path to a relative location within a temp dir
            // (e.g. on Jetty whereas Tomcat and Undertow detect absolute paths).
            // At least we offloaded the file from memory storage; it'll get deleted
            // from the temp dir eventually in any case. And for our user's purposes,
            // we can manually copy it to the requested location as a fallback.
            FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest.toPath()));
        }
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(this.part.getInputStream(), Files.newOutputStream(dest));
    }

}
