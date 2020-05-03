package com.business.storage.controller;

import com.business.storage.cache.GetFileCache;
import com.business.storage.cache.enums.MinioCacheEnum;
import com.business.storage.utils.MinioUtils;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.ServerSideEncryption;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mmustafin@context-it.ru
 * @created: 10.04.2020
 */
@RestController
@RequestMapping("/minio")
public class MinioController {

    private static final String BUCKET_KEY = "bucket";
    private static final String NAME_KEY = "name";
    private static final String STATUS_KEY = "status";
    private static final String META_INFO = "meta-info";
    private static final String BYTES = "bytes";

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/getFile")
    public ResponseEntity<?> getFile(@RequestParam String bucket, @RequestParam String path) {
        final GetFileCache getFileCache = (GetFileCache) redissonClient.getBucket(bucket + path + MinioCacheEnum.GET).get();
        if (getFileCache != null) {
            return ResponseEntity.ok()
                    .contentLength(getFileCache.getBytes().length)
                    .contentType(MediaType.parseMediaType(getFileCache.getContentType()))
                    .header("Content-disposition", "attachment; filename=\"" + getFileCache.getFileName() + "\"")
                    .body(getFileCache.getBytes());
        }
        try {
            this.minioClient.statObject(bucket, path);
            final InputStream inputStream = this.minioClient.getObject(bucket, path);
            final byte[] bytes = IOUtils.toByteArray(inputStream);
            inputStream.close();
            final String fileName = MinioUtils.retrieveFileNameFromString(path);
            Assert.notNull(fileName, "Not found file name. Check your request path variables");
            final String contentType = URLConnection.guessContentTypeFromName(fileName);
            redissonClient.getBucket(bucket + path + MinioCacheEnum.GET).set(new GetFileCache(bytes, contentType, fileName));
            return ResponseEntity.ok()
                    .contentLength(bytes.length)
                    .contentType(MediaType.parseMediaType(contentType))
                    .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(bytes);

        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException | InvalidResponseException | XmlPullParserException var3) {
            throw new RuntimeException("Error while fetching files in Minio", var3);
        }
    }

    @GetMapping("/getMeta")
    public ResponseEntity<?> getMeta(@RequestParam String bucket, @RequestParam String path) {
        Map cachedResponse = (LinkedHashMap) redissonClient.getBucket(bucket + path + MinioCacheEnum.META).get();
        if (!CollectionUtils.isEmpty(cachedResponse)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cachedResponse);
        }
        try {
            final ObjectStat objectStat = this.minioClient.statObject(bucket, path);
            final Map<String, String> statResponse = new LinkedHashMap<>();
            statResponse.put(BUCKET_KEY, objectStat.bucketName());
            statResponse.put(NAME_KEY, objectStat.name());
            objectStat.httpHeaders().forEach((key, value) -> statResponse.put(key, value.get(0)));
            redissonClient.getBucket(bucket + path + MinioCacheEnum.META).set(statResponse);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(statResponse);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException | InvalidResponseException | XmlPullParserException var3) {
            throw new RuntimeException("Error while fetching metadata in Minio", var3);
        }
    }

    @PutMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestBody Map<String, Object> payload) {
        final String bucket = (String) payload.get("bucket");
        final String path = (String) payload.get("path");
        String fileName = MinioUtils.retrieveFileNameFromString(path);
        final String contentType = URLConnection.guessContentTypeFromName(fileName);
        final Map<String, String> metaInfo = (LinkedHashMap) payload.get(META_INFO);
        final List<Integer> byteList = (List<Integer>) payload.get(BYTES);
        if (CollectionUtils.isEmpty(byteList) || StringUtils.isEmpty(contentType)
                || StringUtils.isEmpty(bucket) || StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Content is missing. Check your request configuration");
        }
        final byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = byteList.get(i).byteValue();
        }
        final InputStream file = new ByteArrayInputStream(bytes);
        try {
            this.minioClient.putObject(bucket, path, file, (long) file.available(), metaInfo, (ServerSideEncryption) null, contentType);
            final Map<String, String> uploadResponse = new LinkedHashMap<>();
            uploadResponse.put(STATUS_KEY, "CREATED");
            uploadResponse.put(BUCKET_KEY, bucket);
            uploadResponse.put(NAME_KEY, path);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(uploadResponse);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException | InvalidResponseException | XmlPullParserException var6) {
            throw new RuntimeException("Error while uploading files in Minio", var6);
        }
    }

    @DeleteMapping("deleteFile")
    public ResponseEntity<?> deleteFile(@RequestParam String bucket, @RequestParam String path) {
        try {
            this.minioClient.statObject(bucket, path);
            this.minioClient.removeObject(bucket, path);
            final Map<String, String> removeResponse = new LinkedHashMap<>();
            removeResponse.put(STATUS_KEY, "DELETED");
            removeResponse.put(BUCKET_KEY, bucket);
            removeResponse.put(NAME_KEY, path);
            clearCache(bucket, path);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(removeResponse);
        } catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException | NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException | InvalidResponseException | XmlPullParserException var3) {
            throw new RuntimeException("Error while deleting files in Minio", var3);
        }
    }

    private void clearCache(String bucket, String path) {
        redissonClient.getBucket(bucket + path + MinioCacheEnum.GET).delete();
        redissonClient.getBucket(bucket + path + MinioCacheEnum.META).delete();
    }
}
