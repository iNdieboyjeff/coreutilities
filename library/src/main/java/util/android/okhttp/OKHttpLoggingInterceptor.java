package util.android.okhttp;

import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okio.Buffer;

/**
 * <p>Interceptor for OKHttp that logs the request and response to the logcat.</p>
 */
public class OKHttpLoggingInterceptor implements Interceptor {

    private static final String LOG_TAG = OKHttpLoggingInterceptor.class.getSimpleName();

    /**
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);

        Log.d(LOG_TAG, "Request to:    [" + request.method() + "] " + request.urlString());
        Log.d(LOG_TAG, "Request body:  " + buffer.readUtf8());

        Headers headers = request.headers();
        for (Map.Entry<String, List<String>> entry : headers.toMultimap().entrySet()) {
            Log.d(LOG_TAG, "Request head:  " + entry.getKey()
                    + ": " + entry.getValue());
        }

        long t1 = System.nanoTime();
        com.squareup.okhttp.Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String msg = response.body().string();
        Log.d(LOG_TAG, String.format("Response from: %s in %.1fms%n\n%s",
                response.request().urlString(), (t2 - t1) / 1e6d, msg));
        Log.d(LOG_TAG, "Response code: " + response.code() + " " + response.message());

        Headers headers2 = response.headers();
        for (Map.Entry<String, List<String>> entry : headers2.toMultimap().entrySet()) {
            Log.d(LOG_TAG, "Response head: " + entry.getKey()
                    + ": " + entry.getValue());
        }

        Log.d(LOG_TAG, "Response body: " + msg);
        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), msg))
                .build();
    }
}
