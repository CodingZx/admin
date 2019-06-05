package lol.cicco.admin.common.util;

import com.google.gson.*;
import lol.cicco.admin.common.annotation.GsonIgnore;
import lol.cicco.admin.common.util.time.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonUtils {

    private static final GsonBuilder builder;
    private static final JsonParser parser;

    static {
        builder = new GsonBuilder().disableHtmlEscaping().setDateFormat(DateUtils.DEFAULT_DATE_TIME_FORMATTER)
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
                    // 注册序列化LocalDateTime
                    return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMATTER)));
                }).registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> {
                    // 注册序列化LocalDate
                    return new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMATTER)));
                }).registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfSrc, context) -> {
                    // 注册反序列化LocalDateTime
                    String datetime = json.getAsJsonPrimitive().getAsString();
                    return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_TIME_FORMATTER));
                }).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfSrc, context) -> {
                    // 注册反序列化LocalDate
                    String date = json.getAsJsonPrimitive().getAsString();
                    return LocalDate.parse(date, DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATE_FORMATTER));
                }).setExclusionStrategies(new ExclusionStrategy() {

                    @Override
                    public boolean shouldSkipField(FieldAttributes f) { //需要忽略的字段,返回true会忽略
                        return f.getAnnotation(GsonIgnore.class) != null;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) { //忽略的类
                        return false;
                    }

                });

        ////////////////////////
        parser = new JsonParser();
    }

    public static Gson gson() {
        return builder.create();
    }

    public static JsonParser parser() {
        return parser;
    }
    
}
