/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.windowsazure.messaging.notificationhubs.http;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

/**
 * HTTP exception.
 */
public class HttpException extends IOException {

    private static final long serialVersionUID = 0L;

    /**
     * HTTP response.
     */
    private final HttpResponse mHttpResponse;

    /**
     * Init.
     *
     * @param httpResponse The HTTP response.
     */
    public HttpException(@NonNull final HttpResponse httpResponse) {
        super(getDetailMessage(httpResponse.getStatusCode(), httpResponse.getPayload()));
        mHttpResponse = httpResponse;
    }

    @NonNull
    private static String getDetailMessage(final int status, @NonNull final String payload) {
        if (TextUtils.isEmpty(payload)) {
            return String.valueOf(status);
        }
        return status + " - " + payload;
    }

    /**
     * Get the HTTP response.
     *
     * @return HTTP response.
     */
    public HttpResponse getHttpResponse() {
        return mHttpResponse;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final HttpException that = (HttpException) o;

        return mHttpResponse.equals(that.mHttpResponse);
    }

    @Override
    public int hashCode() {
        return mHttpResponse.hashCode();
    }
}