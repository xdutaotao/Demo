package com.xunao.diaodiao.Common;

import android.content.Context;

import com.xunao.diaodiao.App;
import com.xunao.diaodiao.Utils.LogUtils;
import com.xunao.diaodiao.Utils.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    /**
     * 缓存时间 7天
     */
    private static final int CACHE_TIME = 0;

    private RetrofitService retrofitService;

    /**
     * 公共头部拦截器
     */
    private Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("User-Agent", URLEncoder.encode("CWorker", "UTF-8"))
                    .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .build();
            return chain.proceed(request);
        }
    };

    /**
     * 缓存拦截器
     */
    private Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtils.isNetworkAvailable()) {
                request = request.newBuilder()
                        .addHeader("Connection", "close")
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetWorkUtils.isNetworkAvailable()) {
                int maxAge = 0;
                response.newBuilder().header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                response.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_TIME)
                        .removeHeader("Pragma")
                        .build();
            }

            return response;
        }
    };

    /**
     * log 拦截器
     */
    private final Interceptor loggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            LogUtils.i(String.format("okhttp log: Sending request %s on %s%n%s \n body: %s", request.url(), chain.connection(),
                    request.headers(), bodyToString(request)));
            Response response = chain.proceed(request);
            String bodyString = response.body().string();
            long t2 = System.nanoTime();
            LogUtils.i(String.format(Locale.getDefault(), "okhttp log: Received response for %s in %.1fms%n%s \n body: %s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(), bodyString));
            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
        }
    };

    /**
     * Http Post Body
     */
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    private static class SingletonHolder {
        private static final RetrofitConfig INSTANCE = new RetrofitConfig();
    }

    public static final RetrofitConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitConfig() {
        File cacheFile = new File(App.getContext().getCacheDir(), "OkHttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        SSLSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = getSocketFactory(App.getContext(), "BKS");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        //disableConnectionReuseIfNecessary();

        OkHttpClient okHttpClient = okHttpBuilder
                .connectTimeout(20000, TimeUnit.MILLISECONDS)
                .readTimeout(20000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(cacheInterceptor)
                .cache(cache)
                .build();
        //.addInterceptor(loggingInterceptor)
        //.addInterceptor(headerInterceptor)
        //.cache(cache)

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        retrofitService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }


    private void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        System.setProperty("http.keepAlive", "false");
    }

    //Request to String
    private String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    //证书
    public static SSLSocketFactory getSocketFactory(Context context, String keyStoreType)
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        InputStream trust_input = context.getResources().openRawResource(R.raw.release);//CA机构授信证书
        InputStream trust_input = null;//CA机构授信证书
        Certificate ca = cf.generateCertificate(trust_input);
        trust_input.close();
        if (keyStoreType == null || keyStoreType.length() == 0) {
            keyStoreType = KeyStore.getDefaultType();
        }
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);
        return sslContext.getSocketFactory();
    }


    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        try {
                            originalTrustManager.checkClientTrusted(x509Certificates, s);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        try {
                            originalTrustManager.checkServerTrusted(x509Certificates, s);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }
                }
        };
    }


}