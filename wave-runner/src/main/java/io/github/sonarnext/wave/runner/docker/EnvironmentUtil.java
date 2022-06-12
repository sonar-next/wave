package io.github.sonarnext.wave.runner.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author magaofei
 */
public class EnvironmentUtil {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentUtil.class);

    /**
     * clean dir 清理空间
     * @param projectKey project key
     */
    public static void cleanDir(String projectKey) {
        try {
            FileSystemUtils.deleteRecursively(Paths.get(getDownloadFileAbsolutePath(), projectKey));
        } catch (IOException e1) {
            logger.warn("delete dir fail, projectKey = {}", projectKey, e1);
        }
    }

    public static void cleanDir(File path) {
        FileSystemUtils.deleteRecursively(path);
    }

    /**
     * 代码目录的绝对路径
     * @return
     */
    public static String getDownloadFileAbsolutePath() {
        String download = System.getProperty("download", "download/");
        logger.info("download 路径为 {}", download);
        return Paths.get(download).toAbsolutePath().toString();
    }

    public static String getFilePathWithDownload(String path) {
        return Paths.get(getDownloadFileAbsolutePath(), path).toAbsolutePath().toString();
    }
}
