package com.handson.chatbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service

public class JokeService {

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    @Autowired
    ObjectMapper om;
    static class JockLocktionRes{
        List<JockLocktionObj> result;

        public List<JockLocktionObj> getResult() {
            return result;
        }
    }
    public String getJock() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.chucknorris.io/jokes/search?query=big")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        JockLocktionRes res=om.readValue(response.body().string(),JockLocktionRes.class);
int rnd=(int)(Math.random()*100);
        return res.getResult().get(rnd).getValue();
    }

    private static class JockLocktionObj {
        public String getValue() {
            return value;
        }

        public String getUrl() {
            return url;
        }

        public String getId() {
            return id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getIcon_url() {
            return icon_url;
        }

        String value, url,id,updated_at,icon_url;
    }
}
