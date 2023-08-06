package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.service.GravatarService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

@Service
public class GravatarServiceImpl implements GravatarService {

    private final Logger logger = LoggerFactory.getLogger(GravatarServiceImpl.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofMinutes(1))
            .readTimeout(Duration.ofMinutes(1))
            .callTimeout(Duration.ofMinutes(2))
            .build();

    @Override
    public byte[] getImage(String md5Email, int size) {
        try {
            if (existsInLocalStore(md5Email, size)) {
                return getFromLocalStore(md5Email, size);
            }

            byte[] bytes = getFromGravatarSource(md5Email, size, false);

            // get default image
            if (0 == bytes.length) {
                bytes = getDefaultGravatarFromLocalStore(size);
            }

            // write to local store
            writeToLocalStore(md5Email, size, bytes);

            return bytes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

    @Override
    public void refreshLocalImage(String md5Email, int size) {
        try {
            byte[] localBytes = getFromLocalStore(md5Email, size);

            if (localBytes.length > 0) {
                byte[] bytes = getFromGravatarSource(md5Email, size, false);
                if (bytes.length > 0
                        && bytes.length != localBytes.length) {
                    writeToLocalStore(md5Email, size, bytes);

                    logger.info("local image refreshed, previousLength: {}, currentLength: {}", localBytes.length, bytes.length);
                }
            }
        } catch (Exception e) {
            logger.error("image refresh failed", e);
        }
    }

    private byte[] getFromLocalStore(String md5Email, int size) {
        Path filePath = getGravatarFilePath(md5Email, size);

        try {
            if (existsInLocalStore(md5Email, size)) {
                return Files.readAllBytes(filePath);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return new byte[]{};
    }

    private void writeToLocalStore(String md5Email, int size, byte[] bytes) {
        Path folderPath = getGravatarFolderPath(md5Email, size);
        Path filePath = getGravatarFilePath(md5Email, size);

        try {
            Files.createDirectories(folderPath);
            Files.write(filePath, bytes);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private byte[] getDefaultGravatarFromLocalStore(int size) {
        try {
            if (existsInLocalStore(CommonConstants.GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL, size)) {
                return getFromLocalStore(CommonConstants.GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL, size);
            }

            byte[] bytes = getFromGravatarSource(CommonConstants.GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL, size, true);
            if (bytes.length > 0) {
                writeToLocalStore(CommonConstants.GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL, size, bytes);
            }

            return bytes;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

    private byte[] getFromGravatarSource(String md5Email, int size, boolean includeNobodyImage) {
        String url = String.format(CommonConstants.GRAVATAR_SOURCE_ADDRESS, md5Email, size);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute();
             ResponseBody body = response.body()) {

            if (!includeNobodyImage) {
                if (!response.request().url().toString().equals(url)) {
                    return new byte[]{};
                }
            }

            if (HttpStatus.OK.value() != response.code()
                    || !response.isSuccessful()) {
                return new byte[]{};
            }

            return body.byteStream().readAllBytes();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

    private boolean existsInLocalStore(String md5Email, int size) {
        return Files.exists(getGravatarFilePath(md5Email, size));
    }

    private Path getGravatarFolderPath(String md5Email, int size) {
        String localPath = String.format(CommonConstants.GRAVATAR_STORE_FOLDER, md5Email);
        return Path.of(localPath);
    }

    private Path getGravatarFilePath(String md5Email, int size) {
        String localPath = String.format(CommonConstants.GRAVATAR_STORE_FOLDER, md5Email);
        return Path.of(localPath, String.format("%d.jpg", size));
    }

}
