package com.handson.chatbot.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FreWebService {


    public static String searchProducts(String keyword) throws IOException {
//        if(keyword!="yesOK")return "i d'ont send news. thank";
        return parseProductHtml(getProductHtml(keyword));
    }


    private static String parseProductHtml(String html) {
        String res = "";
        Matcher matcherName = TITLE_PATTERN.matcher(html);
        String a="<li class=\"site-nav__item site-nav__item--level-2 site-nav__item--type-article\">";
        String b=a.length()+"";
//        Matcher matcherPrice = PRODUCT_PATTERNPrice.matcher(html);
        while (matcherName.find()) {
            res += matcherName.group(1)  + "<br> \n";
//            b += matcherName.group()  + "<br> \n";
        }

        return res;



    }

    public static final Pattern TITLE_PATTERN = Pattern.compile("<li class=\\\"site-nav__item site-nav__item--level-2 site-nav__item--type-article\\\">\n" +
            "    .*\n" +
            "      <span class=\\\"site-nav__item-text\\\">([^<]+)<\\/span>\n" +
            "    <\\/a>\n" +
            "  <\\/li>");
    private static String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://www.cbsnews.com/")
                .method("GET", null)
                .addHeader("DNT", "1")
                .addHeader("Referer", "https://www.google.com/")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Cookie", "CBS_INTERNAL=0; fly_device=desktop; fly_geo={\"countryCode\": \"US\", \"region\": \"OH\", \"dma\": \"535\", \"connection\": { \"type\": \"broadband\"}}")
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}

