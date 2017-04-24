package com.iflytek.mscv5plusdemo.httpdemo;

/**
 * Created by jianglei on 2017/3/18.
 */

public class JsonDemo {

    /**
     * apiInfo : {"apiName":"WuXiaolong","apiKey":"666"}
     */

    private ApiInfoBean apiInfo;

    public ApiInfoBean getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfoBean apiInfo) {
        this.apiInfo = apiInfo;
    }

    public static class ApiInfoBean {
        /**
         * apiName : WuXiaolong
         * apiKey : 666
         */

        private String apiName;
        private String apiKey;

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
}
