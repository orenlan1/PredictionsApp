package predictions.client.util.http;

import okhttp3.*;

public class HttpClientUtil {
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncPost(String finalUrl, RequestBody body, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }
}
