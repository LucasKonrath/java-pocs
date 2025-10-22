package org.example.okhttppoc;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OkHttpClientService {

    public String doCall(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://www.baidu.com").build();
        try {
            return client.newCall(request).execute().body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
