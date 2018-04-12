package com.testingsyndicate.jms.responder.matcher;

import com.testingsyndicate.jms.responder.model.BodySource;
import com.testingsyndicate.jms.responder.model.RequestInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashMatcherTest {

    @Test
    public void matchesWhenEqual() {
        // given
        HashMatcher sut = HashMatcher("wibble", false);

        RequestInfo requestInfo = requestInfoWithBody("wibble");

        // when
        boolean actual = sut.matches(requestInfo);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    public void doesntMatchWhenDifferent() {
        // given
        HashMatcher sut = HashMatcher("wibble", false);
        RequestInfo requestInfo = requestInfoWithBody("wobble");

        // when
        boolean actual = sut.matches(requestInfo);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    public void matchesNulls() {
        // given
        HashMatcher sut = HashMatcher(null, false);
        RequestInfo requestInfo = requestInfoWithBody(null);

        // when
        boolean actual = sut.matches(requestInfo);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    public void matchesWhenTrimmed() {
        // given
        HashMatcher sut = HashMatcher("hi", true);
        RequestInfo requestInfo = requestInfoWithBody("  hi   ");

        // when
        boolean actual = sut.matches(requestInfo);

        // then
        assertThat(actual).isTrue();
    }

    private static HashMatcher HashMatcher(String body, boolean trim) {
        try {
            return HashMatcher.newBuilder()
                    .withBody(new BodySource(body))
                    .withTrim(trim)
                    .build();
        } catch (Exception e) {

        }
        return null;
    }

    private static RequestInfo requestInfoWithBody(String body) {
        return RequestInfo.newBuilder()
                .withBody(body)
                .build();
    }

}