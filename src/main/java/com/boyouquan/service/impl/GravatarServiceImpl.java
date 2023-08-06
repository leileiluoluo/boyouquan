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
        return getFromLocalStore(md5Email, size);
    }

    private byte[] getFromLocalStore(String md5Email, int size) {
        String localPath = String.format(CommonConstants.GRAVATAR_STORE_FOLDER, md5Email);
        Path folderPath = Path.of(localPath);
        Path filePath = Path.of(localPath, String.format("%d.jpg", size));

        try {
            // not exists
            if (Files.notExists(filePath)) {
                byte[] bytes = getFromGravatarSource(md5Email, size, false);

                // get default image
                if (0 == bytes.length) {
                    bytes = getDefaultGravatarFromLocalStore(size);
                }

                Files.createDirectories(folderPath);
                Files.write(filePath, bytes);

                return bytes;
            }

            return Files.readAllBytes(filePath);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new byte[]{};
        }
    }

    private byte[] getDefaultGravatarFromLocalStore(int size) {
        Path defaultImageFolderPath = Path.of(CommonConstants.GRAVATAR_DEFAULT_IMAGE_STORE_FOLDER);
        Path defaultImageFilePath = Path.of(CommonConstants.GRAVATAR_DEFAULT_IMAGE_STORE_FOLDER, String.format("%d.jpg", size));

        try {
            if (Files.notExists(defaultImageFilePath)) {
                byte[] bytes = getFromGravatarSource(CommonConstants.GRAVATAR_DEFAULT_IMAGE_MD5_EMAIL, size, true);

                if (bytes.length > 0) {
                    Files.createDirectories(defaultImageFolderPath);
                    Files.write(defaultImageFilePath, bytes);
                }

                return bytes;
            }
            return Files.readAllBytes(defaultImageFilePath);
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

}
