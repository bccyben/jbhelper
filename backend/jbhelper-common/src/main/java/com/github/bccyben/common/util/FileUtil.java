package com.github.bccyben.common.util;

import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ファイル共通
 */
public final class FileUtil {

    /**
     * 各単位の倍数
     */
    private static final long BYTES_UNIT = 1024;

    /**
     * 表示用ファイルサイズに変換。KBとMBは小数点1位まで四捨五入
     *
     * <pre>
     *   1 ~ 1024 -> B
     *   1025 ~ 1024 * 1024 -> KB
     *   1024 * 1024 + 1 ~  -> MB
     * </pre>
     *
     * @param bytes the bytes
     * @return string
     */
    public static String convertFileSize(long bytes) {
        if (bytes < BYTES_UNIT) {
            return bytes + "B";
        } else if (bytes < BYTES_UNIT * BYTES_UNIT) {
            return (double) Math.round((double) bytes / BYTES_UNIT * 10) / 10 + "KB";
        } else {
            return (double) Math.round((double) bytes / BYTES_UNIT / BYTES_UNIT * 10) / 10 + "MB";
        }
    }

    /**
     * Convert image path string.
     *
     * @param host   the host
     * @param imgSrc the img src
     * @return the string
     */
    public static String convertImagePath(String host, String imgSrc) {
        if (imgSrc == null || host == null)
            return null;
        return imgSrc.replaceAll(host + "/", "");
    }

    /**
     * イメージパスを返す。pathがブランクの場合、hostも返さない
     *
     * @param host the host
     * @param path the path
     * @return the image path
     */
    public static String getImagePath(String host, String path) {
        if (StringUtils.hasText(path)) {
            return String.format("%s/%s", host, path);
        }
        return null;
    }

    /**
     * Get images path string string.
     *
     * @param data the data
     * @param host the host
     * @return the string
     */
    public static String getImagesPathString(String data, String host) {
        // productIDを使って、DBからデータを処理
        String imagesString = new String();
        // String[] images = new String[list.size()];
        try {
            String[] images = data.split(",");
            for (int i = 0; i < images.length; i++) {
                // StringBuilder MyImage = new StringBuilder(images[i]);
                images[i] = images[i].replace("[", "");
                images[i] = images[i].replace("]", "");
                images[i] = images[i].replace("\"", "");
                images[i] = images[i].replace("\"", "");
                images[i] = images[i].replace(" ", "");
                images[i] = images[i].replace(images[i], host + "/" + images[i]);
            }
            imagesString = Arrays.toString(images);

        } catch (Exception e) {
        }
        return imagesString;
    }

    /**
     * ByteArrayOutputStream->ByteArrayInputStream
     *
     * @param out
     * @return
     */
    public static ByteArrayInputStream conventOutToIn(ByteArrayOutputStream out) {
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * InputStream->ByteArrayOutputStream
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream conventInToOut(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        in.transferTo(out);
        return out;
    }

    /**
     * ファイルの拡張子を消す
     *
     * @param fullFileName
     * @return
     */
    public static String getFileNameNoExtension(String fullFileName) {
        int index = fullFileName.lastIndexOf(".");
        if (index == -1) {
            return fullFileName;
        }
        return fullFileName.substring(0, index);
    }

    /**
     * ファイルの拡張子を取得
     *
     * @param fullFileName
     * @return
     */
    public static String gitFileExtension(String fullFileName) {
        int index = fullFileName.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        return fullFileName.substring(index, fullFileName.length());
    }

    /**
     * ファイルは画像であるかどうか
     *
     * @param contentType
     * @return Boolean
     */
    public static Boolean isImage(String contentType) {
        if (ContentType.IMAGE_BMP.toString().equals(contentType)) {
            return true;
        }
        if (ContentType.IMAGE_GIF.toString().equals(contentType)) {
            return true;
        }
        if (ContentType.IMAGE_JPEG.toString().equals(contentType)) {
            return true;
        }
        if (ContentType.IMAGE_PNG.toString().equals(contentType)) {
            return true;
        }
        if (ContentType.IMAGE_SVG.toString().equals(contentType)) {
            return true;
        }
        if (ContentType.IMAGE_TIFF.toString().equals(contentType)) {
            return true;
        }
        if (ContentType.IMAGE_WEBP.toString().equals(contentType)) {
            return true;
        }
        return false;
    }

    /**
     * 文書ファイルであるかどうか
     *
     * @param contentType
     * @return
     */
    public static Boolean isDocument(String contentType) {
        // TODO
        return false;
    }

    public static String getFileAsString(String uri) {
        try (InputStream is = FileUtil.class.getClassLoader().getResourceAsStream(uri)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
