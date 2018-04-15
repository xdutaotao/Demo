package com.gzfgeh.server;

import android.util.Log;
import android.widget.Toast;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.RequestMethod;
import com.yanzhenjie.andserver.annotation.RequestMapping;

import org.apache.httpcore.HttpException;
import org.apache.httpcore.HttpRequest;
import org.apache.httpcore.HttpResponse;
import org.apache.httpcore.protocol.HttpContext;

import java.io.IOException;

/**
 * Description:
 * Created by guzhenfu on 2018/4/15.
 */

public class DataHandler implements RequestHandler {

    @RequestMapping(method = {RequestMethod.GET})
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String method = request.getRequestLine().getMethod();
        String url = request.getRequestLine().getUri();

        Log.d("handler", method + url);
    }
}
