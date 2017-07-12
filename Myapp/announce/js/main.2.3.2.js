/**
 * Created by xupeipei on 2017/1/17.
 */
require("UIApplication");

defineClass("SHWebView", {
    webView_didStartProvisionalNavigation: function(webView, navigation) {
        if (webView.URL().absoluteString().containsString("https://jinshuju.net/f/v1dSxl")) {
            UIApplication.sharedApplication().openURL(webView.URL());
        } else {
            self.delegate().webViewStartLoad();
        }
    }
}, {});

