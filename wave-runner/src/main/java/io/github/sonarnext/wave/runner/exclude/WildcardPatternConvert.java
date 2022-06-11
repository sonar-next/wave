package io.github.sonarnext.wave.runner.exclude;

/**
 * 从 WildcardPattern  转换为 Cppcheck
 *
 * @author magaofei
 */
public interface WildcardPatternConvert {

    String[] convert(String[] files);
}
