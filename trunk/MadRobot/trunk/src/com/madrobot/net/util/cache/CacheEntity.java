
package com.madrobot.net.util.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;

import com.madrobot.io.IOUtils;
import com.madrobot.net.util.cache.annotation.Immutable;

@Immutable
class CacheEntity implements HttpEntity, Serializable {

    private static final long serialVersionUID = -3467082284120936233L;

    private final HttpCacheEntry cacheEntry;

    public CacheEntity(final HttpCacheEntry cacheEntry) {
        super();
        this.cacheEntry = cacheEntry;
    }

    public Header getContentType() {
        return this.cacheEntry.getFirstHeader(HTTP.CONTENT_TYPE);
    }

    public Header getContentEncoding() {
        return this.cacheEntry.getFirstHeader(HTTP.CONTENT_ENCODING);
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return true;
    }

    public long getContentLength() {
        return this.cacheEntry.getResource().length();
    }

    public InputStream getContent() throws IOException {
        return this.cacheEntry.getResource().getInputStream();
    }

    public void writeTo(final OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        InputStream instream = this.cacheEntry.getResource().getInputStream();
        try {
            IOUtils.copy(instream, outstream);
        } finally {
            instream.close();
        }
    }

    public boolean isStreaming() {
        return false;
    }

    public void consumeContent() throws IOException {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
