package io.github.sonarnext.wave.runner.sonar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * language 映射表
 * name 需要对应 gitlab language name
 * 大小写不敏感
 */
public enum LanguageEnum {

    /**
     *
     */
    PHP("PHP", new String[]{"php", "php3", "php4", "php5", "phtml", "inc"}),
    TYPE_SCRIPT("TypeScript", new String[]{"ts"}),
    RUBY("Ruby", new String[]{"rb"}),
    SCALA("Scala", new String[]{"scala"}),
    SQL("SQL", new String[]{"sql"}),
    KOTLIN("Kotlin", new String[]{"kt"}),
    GO("Go", new String[]{"go"}),
    C("C", new String[]{"c"}),
    PYTHON("Python", new String[]{"py"}),
    LESS("CSS", new String[]{"less"}),
    SASS("CSS", new String[]{"sass"}),
    SCSS("CSS", new String[]{"scss"}),
    CSS("CSS", new String[]{"css"}),
    HTML("HTML", new String[]{"html"}),
    XML("XML", new String[]{"xml"}),
    OBJECTIVE_CPP("Objective-C", new String[]{"mm"}),
    OBJECTIVE_C("Objective-C", new String[]{"m"}),
    CPP("C++", new String[]{"cpp", "cc", "cxx", "h", "hh", "c", "hxx", "hpp"}),
    C_SHARP("C#", new String[]{"cs"}),
    JAVA_SCRIPT("JavaScript", new String[]{"js"}),
    VUE("Vue", new String[]{"vue"}),
    REACT("React", new String[]{"jsx"}),
    SWIFT("Swift", new String[]{"swift"}),
    SHELL("Shell", new String[]{"sh"}),
    JAVA("Java", new String[]{"java", "jav"}),
    LUA("Lua", new String[]{"lua"}),
    DART("Dart", new String[]{"dart"}),
    MARKDOWN("Markdown", new String[]{"md"});

    private final String name;
    private final String[] fileType;

    /**
     * key 为 所有语言的文件类型
     */
    protected static final Map<String, LanguageEnum> LANGUAGE_FILE_TYPE_MAP = new HashMap<>(32);

    public static final List<LanguageEnum> SUPPORT_LANGUAGE = new ArrayList<>();

    private static final Map<String, LanguageEnum> ALL_LANGUAGE = new HashMap<>(32);

    static {

        SUPPORT_LANGUAGE.add(JAVA);
        SUPPORT_LANGUAGE.add(PHP);
        SUPPORT_LANGUAGE.add(JAVA_SCRIPT);
        SUPPORT_LANGUAGE.add(VUE);
        SUPPORT_LANGUAGE.add(REACT);
        SUPPORT_LANGUAGE.add(TYPE_SCRIPT);
        SUPPORT_LANGUAGE.add(CPP);
        SUPPORT_LANGUAGE.add(C);
        SUPPORT_LANGUAGE.add(GO);
        SUPPORT_LANGUAGE.add(KOTLIN);
        SUPPORT_LANGUAGE.add(SCALA);
        SUPPORT_LANGUAGE.add(PYTHON);
        SUPPORT_LANGUAGE.add(OBJECTIVE_C);
        SUPPORT_LANGUAGE.add(RUBY);
        SUPPORT_LANGUAGE.add(DART);

        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum == MARKDOWN) {
                continue;
            }
            for (String fileType : languageEnum.getFileType()) {
                LANGUAGE_FILE_TYPE_MAP.put(fileType, languageEnum);
            }
            ALL_LANGUAGE.put(languageEnum.getName(), languageEnum);
        }

    }

    LanguageEnum(String name, String[] fileType) {
        this.name = name;
        this.fileType = fileType;
    }

    public static boolean containExtension(String extension) {
        return LanguageEnum.LANGUAGE_FILE_TYPE_MAP.containsKey(extension);
    }
    public static boolean isJava(String language) {
        return language.equalsIgnoreCase(JAVA.toString());
    }

    public static String[] getFileType(LanguageEnum languageEnum) {
        return languageEnum.fileType;
    }


    /**
     * 根据fileType查找语言
     * @param fileType 文件类型，不带.
     * @return
     */
    public static LanguageEnum getLanguageFromFileType(String fileType) {
        return LANGUAGE_FILE_TYPE_MAP.get(fileType);
    }

    public static LanguageEnum getLanguageFromName(String name) {
        return LANGUAGE_ENUM_MAP.get(name.toLowerCase());
    }

    public static boolean isIos(String languageName) {
        return languageName.equalsIgnoreCase(LanguageEnum.OBJECTIVE_C.name)
                || languageName.equalsIgnoreCase(LanguageEnum.SWIFT.name);
    }

    private static final Map<String, LanguageEnum> LANGUAGE_ENUM_MAP = new HashMap<>(LanguageEnum.values().length);
    static {
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            LANGUAGE_ENUM_MAP.put(languageEnum.getName().toLowerCase(), languageEnum);
        }
    }

    //    public static final String VUE = "vue";
    public static final String REACT_NAME = "REACT";
//    public static final String SASS = "sass";
//    public static final String LESS = "less";

    @JsonValue
    public String getValue() {
        return this.getName();
    }

    @JsonCreator
    public static LanguageEnum forValue(String languageName) {
        return ALL_LANGUAGE.get(languageName);
    }
    /**
     * 转换语言，例如 vue转为js
     * @param language
     * @return
     */
    public static String convertLanguage(String language) {
        if (language.equalsIgnoreCase(VUE.toString()) || language.equalsIgnoreCase(REACT_NAME)) {
            language = LanguageEnum.JAVA_SCRIPT.getName();
        } else if (language.equalsIgnoreCase(SASS.toString()) || language.equalsIgnoreCase(LESS.toString())) {
            language = LanguageEnum.CSS.getName();
        }
        return language;

    }


    public static Boolean isIncludeLanguage(String language) {
        String newLanguage = convertLanguage(language);
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum.name.equalsIgnoreCase(newLanguage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 输入任意大小写的 语言，均可以获得 LanguageEnum 中存储的标准语言
     * @param language
     * @return
     */
    public static String getNormalLanguage(String language){
        String newLanguage = convertLanguage(language);
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum.name.equalsIgnoreCase(newLanguage)) {
                return languageEnum.name;
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public String[] getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        return "LanguageEnum{" +
                "name='" + name + '\'' +
                ", fileType=" + fileType +
                '}';
    }
}
