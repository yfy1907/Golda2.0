package com.example.administrator.goldaapp.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;

public class StringUtil {

    public static String encodeStr(byte[] paramArrayOfByte){
        byte[] unCompressed = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(paramArrayOfByte.length);
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(paramArrayOfByte);
            final byte[] buf = new byte[1024];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
            unCompressed = bos.toByteArray();
            bos.close();
            return new String(unCompressed, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            decompressor.end();
        }
        // String test = bos.toString();
        // return unCompressed;
        return null;
    }

    //	public java.lang.String decompress(byte[] paramArrayOfByte){
//
//		byte[] unCompressed = null;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(paramArrayOfByte.length);
//        Inflater decompressor = new Inflater();
//        try {
//            decompressor.setInput(paramArrayOfByte);
//            final byte[] buf = new byte[1024];
//            while (!decompressor.finished()) {
//                int count = decompressor.inflate(buf);
//                bos.write(buf, 0, count);
//            }
//
//            unCompressed = bos.toByteArray();
//            bos.close();
//
//            return new String(unCompressed, "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            decompressor.end();
//        }
//        // String test = bos.toString();
//        // return unCompressed;
//        return null;
//
//	}
}
