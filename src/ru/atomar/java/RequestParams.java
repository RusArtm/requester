package ru.atomar.java;

public class RequestParams {

    public boolean secure;
    public String host;
    public String auth;
    public String file;
    public String contentType;
    public String payload;
    public String methodName;
    public String request;

    public RequestParams(){
        request = null;
    }

    public RequestParams(String methodName, boolean secure, String host, String auth, String file, String contentType, String payload) {
        this.methodName = methodName;
        this.secure = secure;
        this.host = host;
        this.auth = auth;
        this.file = file;
        this.contentType = contentType;
        this.payload = payload;
        request = null;
    }

}
