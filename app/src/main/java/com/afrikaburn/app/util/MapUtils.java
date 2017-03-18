/*
 * MIT License
 *
 * Copyright (c) 2017 Maia Grotepass
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.afrikaburn.app.util;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.afrikaburn.app.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Map utilities to handle the mbtiles files
 * Created by maia on 2017/03/18.
 */

public class MapUtils {
    private static String theMBTilesFilename = "ab.mbtiles";

    @WorkerThread
    public static void setupMBTilesFile(Context context) {
        //Only do this the first time, so check if the file exists.
        if (!fileExists(context, theMBTilesFilename)) {
            InputStream inputStream = context.getResources().openRawResource(R.raw.ab);

            try {
                OutputStream outputStream = context.openFileOutput(theMBTilesFilename, Context.MODE_PRIVATE);
                try {
                    copy(inputStream, outputStream);
                    outputStream.close();
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @WorkerThread
    private static long copy(InputStream input, OutputStream output) throws Exception {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        long count = 0;
        while (-1 != (bytesRead = input.read(buffer))) {
            output.write(buffer, 0, bytesRead);
            count += bytesRead;
        }
        return count;
    }

    public static File getMBTilesHandle(Context context) {
        return new File(context.getFilesDir(), theMBTilesFilename);
    }

    private static boolean fileExists(Context context, String name) {
        File file = context.getFileStreamPath(name);
        return file.exists();
    }
}
