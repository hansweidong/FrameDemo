package com.example.library.http.retrofit;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by bjhl on 16/7/1.
 * 带进度条 上传文件
 */
public class FProgressRequestBody {

    /** Returns a new request body that transmits the content of {@code file}. */
    public static RequestBody create(final MediaType contentType, final File file , final FRequestProgressListener progressListener) {
        if (file == null) throw new NullPointerException("content == null");

        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
//                    sink.writeAll(source);
                    Buffer buf = new Buffer();
                    long bytesWritten = 0L;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        bytesWritten+=readCount;
                        if (null != progressListener) {
                            Observable.just(bytesWritten).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    progressListener.onProgress(aLong, contentLength());
                                }
                            });
                        }

                    }
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}

