package io.github.sonarnext.wave.runner.exclude;

import java.util.Arrays;

/**
 * cppcheck 格式转换
 *
 * @author magaofei
 */
public class CppcheckExcludePatternConvert implements WildcardPatternConvert {

    /**
     * 去掉 / 绝对路径的前缀
     * 去掉 **
     * @param files
     * @return
     */
    @Override
    public String[] convert(String[] files) {

        return Arrays.stream(files).map(filePath -> {
            if (filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }
            return filePath.replace("*", "");
        }).toArray(String[]::new);
    }
}
