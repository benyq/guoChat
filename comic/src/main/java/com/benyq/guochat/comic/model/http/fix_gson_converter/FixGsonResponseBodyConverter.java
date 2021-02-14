package com.benyq.guochat.comic.model.http.fix_gson_converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Hpack;
import retrofit2.Converter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author benyq
 * @time 2021/2/14
 * @e-mail 1520063035@qq.com
 * @note 修复[]中的null问题
 */
final class FixGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    FixGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        String json = removeEmptyArray(value.string());
        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }

    private String removeEmptyArray(String json) {
        //直接删除空数组
        int index = json.indexOf("},[],{");
        if (index != -1) {
            return json.replaceFirst("\\},\\[\\],\\{", "},{");
        }
        return json;
    }
}
