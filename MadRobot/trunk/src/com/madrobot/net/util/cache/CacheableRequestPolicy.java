
package com.madrobot.net.util.cache;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;

import com.madrobot.net.HeaderConstants;
import com.madrobot.net.HeaderConstants.HttpMethod;
import com.madrobot.net.util.cache.annotation.Immutable;

import android.util.Log;

/**
 * Determines if an HttpRequest is allowed to be served from the cache.
 *
 * @since 4.1
 */
@Immutable
class CacheableRequestPolicy {


    /**
     * Determines if an HttpRequest can be served from the cache.
     *
     * @param request
     *            an HttpRequest
     * @return boolean Is it possible to serve this request from cache
     */
    public boolean isServableFromCache(HttpRequest request) {
        String method = request.getRequestLine().getMethod();

        ProtocolVersion pv = request.getRequestLine().getProtocolVersion();
        if (HttpVersion.HTTP_1_1.compareToVersion(pv) != 0) {
            Log.d("MadRobot","non-HTTP/1.1 request was not serveable from cache");
            return false;
        }

        if (!method.equals(HttpMethod.GET)) {
        	  Log.d("MadRobot","non-GET request was not serveable from cache");
            return false;
        }

        if (request.getHeaders(HeaderConstants.PRAGMA).length > 0) {
        	  Log.d("MadRobot","request with Pragma header was not serveable from cache");
            return false;
        }

        Header[] cacheControlHeaders = request.getHeaders(HeaderConstants.CACHE_CONTROL);
        for (Header cacheControl : cacheControlHeaders) {
            for (HeaderElement cacheControlElement : cacheControl.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_NO_STORE.equalsIgnoreCase(cacheControlElement
                        .getName())) {
                	  Log.d("MadRobot","Request with no-store was not serveable from cache");
                    return false;
                }

                if (HeaderConstants.CACHE_CONTROL_NO_CACHE.equalsIgnoreCase(cacheControlElement
                        .getName())) {
                	  Log.d("MadRobot","Request with no-cache was not serveable from cache");
                    return false;
                }
            }
        }

        Log.d("MadRobot","Request was serveable from cache");
        return true;
    }

}
