package com;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType type;
    private Object content;

    public Request() {

    }

    public Object content() {
        return content;
    }

    public RequestType type() {
        return type;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", content=" + content +
                '}';
    }

    public static class Builder {
        private Request request = new Request();

        public Builder type(RequestType type) {
            request.type = type;
            return this;
        }

        public Builder object(Object object) {
            request.content = object;
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
