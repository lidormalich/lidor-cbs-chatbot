package com.handson.chatbot.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonService {

    public String searchProducts(String keyword) throws IOException {
        return parseProductHtml(getProductHtml(keyword));
    }


    private String parseProductHtml(String html) {
        String res = "";
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        while (matcher.find()) {
            res += matcher.group(1) + " - " + matcher.group(2) + ", price:" + matcher.group(3) + "<br>\n";
        }
        return res;
    }
    public static final Pattern PRODUCT_PATTERN = Pattern.compile("\"<span class=\\\"a-size-medium a-color-base a-text-normal\\\">([^<]+)<\\/span>.*<span class=\\\"a-icon-alt\\\">([^<]+)<\\/span>.*<span class=\\\"a-offscreen\\\">([^<]+)<\\/span>\"gm");
    private String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
//                .url("https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords="+keyword+"&crid=1GT9X4BGGW4FB&sprefix="+keyword+"%2Caps%2C345")

                .url("https://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords="+keyword+"&crid=1GT9X4BGGW4FB&sprefix=ipod%2Caps%2C345")
                .method("GET", null)

                .addHeader("authority", "www.amazon.com")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .addHeader("accept-language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("cookie", "ubid-main=130-2616730-9193362; sp-cdn=\"L5Z9:IL\"; x-main=\"RcaCQTGYZF0FJBjLbRqhns5utonl9?nqMtEf3m8od?OVwkl@mI5xjd8KcYmhQ7YU\"; at-main=Atza|IwEBIETR64n-bjGm6d_3i8NJ3rBWa7C-99E_pN0qWa4zXF4uZUR21jmbl8HIXX55YGUa5KncXJfVI-mhoToY6jj21sWDr6M0utft7SbKouUDJSgJ5B05ZZQ7nHa-Lwr5yOM6y6AotVwN4EUDRi9PTVEaJs3VlqqLmYrKMjnIh8GraSgosQ38_ozBK4VmvJBJe6lNtPgJ38f7cEGFYI9vTni9SgU4; sess-at-main=\"y0cYfPjY85o5oTPgTEF5F0n3QF7YEBy7QV/6eyApLj0=\"; sst-main=Sst1|PQG3WYP9bDrnijebidd3G5vxCZ2Hk_UCm03GzOYsQK-Dtsvm55ntMiKejd60Nv_cHLtHdvPHYT0_MuPELwYFtQYN-ZWHofacCwayZgIQCqoxewajqNcEQTt9iB4mq_ywoKo0n3QZIUnMLRMVDOxvexa0vFjJ3spu9ku0KauWjwyOZ_nmSB88j4wOxPs35JIBijmSbU-HObtqXaq0jzku5c8k2ZjtcKjL_QJi1f-o2kV5su9C0NF1OU146V0H1SeiWxobxwFEQUaUDU-gfMolWGSSctRvCRXOEL1yV1bdEr4W0iU; aws-target-data=%7B%22support%22%3A%221%22%7D; aws_lang=en; AMCVS_7742037254C95E840A4C98A6%40AdobeOrg=1; aws-target-visitor-id=1679300582141-849250.37_0; s_campaign=ps%7C2d3e6bee-b4a1-42e0-8600-6f2bb4fcb10c; s_cc=true; aws-mkto-trk=id%3A112-TZM-766%26token%3A_mch-aws.amazon.com-1679300582532-54841; s_eVar60=2d3e6bee-b4a1-42e0-8600-6f2bb4fcb10c; aws-ubid-main=531-0062842-1600150; remember-account=true; aws-account-alias=995553441267; regStatus=registered; AMCV_7742037254C95E840A4C98A6%40AdobeOrg=1585540135%7CMCIDTS%7C19437%7CMCMID%7C03992363174051669422460669102646323493%7CMCAAMLH-1680026502%7C6%7CMCAAMB-1680026502%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1679428902s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C4.4.0; aws-userInfo=%7B%22arn%22%3A%22arn%3Aaws%3Aiam%3A%3A995553441267%3Auser%2Flidor%22%2C%22alias%22%3A%22995553441267%22%2C%22username%22%3A%22lidor%22%2C%22keybase%22%3A%22IVUbXepzLb%2Fj13K%2FpyUBWu8zEWRPZkshwu7b5ymCMTw%5Cu003d%22%2C%22issuer%22%3A%22http%3A%2F%2Fsignin.aws.amazon.com%2Fsignin%22%2C%22signinType%22%3A%22PUBLIC%22%7D; aws-userInfo-signed=eyJ0eXAiOiJKV1MiLCJrZXlSZWdpb24iOiJldS1jZW50cmFsLTEiLCJhbGciOiJFUzM4NCIsImtpZCI6ImQ5MGMzYWI2LTVkMzktNDVjMi1hZDcwLWMwMjMzZjYyNmI0ZCJ9.eyJzdWIiOiI5OTU1NTM0NDEyNjciLCJzaWduaW5UeXBlIjoiUFVCTElDIiwiaXNzIjoiaHR0cDpcL1wvc2lnbmluLmF3cy5hbWF6b24uY29tXC9zaWduaW4iLCJrZXliYXNlIjoiSVZVYlhlcHpMYlwvajEzS1wvcHlVQld1OHpFV1JQWmtzaHd1N2I1eW1DTVR3PSIsImFybiI6ImFybjphd3M6aWFtOjo5OTU1NTM0NDEyNjc6dXNlclwvbGlkb3IiLCJ1c2VybmFtZSI6ImxpZG9yIn0.c6qs36pyQdsnYJ0VhOi56rIKKgayGAcYGWcib7yZHNQMXex7ew0J0lnWZaV526Ixa7mschuGSEHWeUhR7mIdY160lEDQDZ3pPMcNS-LCbp14e7-ajQJQxX4ayTNZvDl4; awsc-color-theme=light; awsc-uh-opt-in=\"\"; noflush_awsccs_sid=cc8d7caaa1c771909f4ef91157163cef4753db643b93348f45301b0bd61c89e5; aws-signer-token_eu-central-1=eyJrZXlWZXJzaW9uIjoiLl9Fak1yQUhMVjNZdmVIVzh3aDdJTXNrckVrR09ibVMiLCJ2YWx1ZSI6IjBvc2N2Sjh6UzliOXl3ZUkxVHo4emlDK3MrQlBOMGtMOE84SGJlZ2JWR2c9IiwidmVyc2lvbiI6MX0=; session-id=139-6251787-3361564; session-id-time=2082787201l; i18n-prefs=ILS; lc-main=en_US; skin=noskin; session-token=0KmwmHCKiBBG2/G6q2jZaWG2xs6X0K5TYun/mDiZkL1mG2uFtbBDpLqfUJamTFVtyw6m86JmDW9Lbkx9nEk+jl+FaK/LL+R/Iw8GZXK3D+D5NRQZtD0GLkikk5suArAE3nu9omSagKQTmx7nGeF8qYPIxbjqTjA0gOrErW0TByGI2eWCiEuoG6uXs3vVR28V/6Q7Ypnw+vVr29UoI/PeH9CiszkAzbokbnekJ5NKNrnFV4y0etIE5Y/f20/jPFXR; csm-hit=tb:9BKHQT8044XRSAA4DTN0+s-9BKHQT8044XRSAA4DTN0|1679474579299&t:1679474579299&adb:adblk_no")
                .addHeader("device-memory", "8")
                .addHeader("dnt", "1")
                .addHeader("downlink", "10")
                .addHeader("dpr", "1.25")
                .addHeader("ect", "4g")
                .addHeader("referer", "https://www.amazon.com/")
                .addHeader("rtt", "0")
                .addHeader("sec-ch-device-memory", "8")
                .addHeader("sec-ch-dpr", "1.25")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-ch-ua-platform-version", "\"15.0.0\"")
                .addHeader("sec-ch-viewport-width", "1124")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                .addHeader("viewport-width", "1124")
                .build();
        Response response = client.newCall(request).execute();

    return response.body().string();
    }
}
