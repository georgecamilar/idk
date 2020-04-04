package com;

import java.io.Serializable;

public class Response implements Serializable {
    private Object content;
    private ResponseType type;

    public Response() {
    }

    public Object content() {
        return this.content;
    }

    public ResponseType type() {
        return this.type;
    }

    private void content(Object object) {
        this.content = object;
    }

    private void type(ResponseType type) {
        this.type = type;
    }

    public static class Builder {
        private Response response = new Response();

        public Response.Builder type(ResponseType type) {
            response.type = type;
            return this;
        }

        public Response.Builder object(Object object) {
            response.content = object;
            return this;
        }

        public Response build() {
            return response;
        }
    }

}
