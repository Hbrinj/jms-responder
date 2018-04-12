package com.testingsyndicate.jms.responder.matcher;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.testingsyndicate.jms.responder.model.BodySource;
import com.testingsyndicate.jms.responder.model.Request;

import java.util.Arrays;
import java.util.Objects;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@JsonDeserialize(builder = HashMatcher.Builder.class)
public final class HashMatcher implements Matcher {

    private final String body;
    private final boolean trim;
    private final byte[] bodyDigest;

    private HashMatcher(Builder builder) throws NoSuchAlgorithmException {
        MessageDigest md =  MessageDigest.getInstance("SHA-256");
        trim = builder.trim;
        if (null != builder.body && trim) {
            body = builder.body.trim();
        } else {
            body = builder.body;
        }

        if (null != this.body) {
            md.update(this.body.getBytes());
            this.bodyDigest = md.digest();
        } else {
            this.bodyDigest = null;
        }

    }

    public boolean matches(Request request) {
        MessageDigest md = null;
        byte[] requestBodyDigest = null;
        try{
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
        }
        String requestBody = request.getBody();

        if (null != requestBody && trim) {
            requestBody = requestBody.trim();
        }

        if( null != requestBody) {
            md.update(requestBody.getBytes());
            requestBodyDigest = md.digest();
        }

        return Arrays.equals(bodyDigest, requestBodyDigest);
    }

    public String getBody() {
        return body;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private String derp(byte[] by) {
        StringBuilder sb = new StringBuilder();
        for (byte b : by) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @JsonPOJOBuilder
    public static final class Builder {

        private String body;
        private boolean trim;

        private Builder() {
            trim = false;
        }

        public Builder withBody(BodySource body) {
            this.body = body.getBody();
            return this;
        }

        public Builder withTrim(boolean trim) {
            this.trim = trim;
            return this;
        }

        public HashMatcher build() throws NoSuchAlgorithmException {
            return new HashMatcher(this);
        }

    }
}
