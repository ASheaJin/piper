package com.syswin.pipeline.service.content;

import it.sauronsoftware.jave.*;

import java.io.File;

/**
 * 处理音频和视频
 * Created by 115477 on 2019/4/11.
 */
public final class MediaUtil {

    private static final String LIBMP_3_LAME = "libmp3lame";

    public static String amrToMp3(String filePath, String format) {
        File source = new File(filePath);
      if (!source.exists()) {
            throw new IllegalArgumentException("source file does not exists: " + source.getAbsoluteFile());
        }
        String targetPath = targetPath(filePath, format);
        File target = new File(targetPath);

        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new IgnoreErrorEncoder();
        audio.setCodec(LIBMP_3_LAME);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat(format);
        attrs.setAudioAttributes(audio);
        try {
            encoder.encode(source, target, attrs);
        } catch (Exception e) {
            throw new IllegalStateException("convert amr to " + format + " error: ", e);
        }
        return targetPath;
    }

    public static String targetPath(String sourceFilePath, String postFix) {
        int lastPointIndex = sourceFilePath.lastIndexOf(".");

        if (lastPointIndex > 0) {
            return sourceFilePath.substring(0, lastPointIndex + 1) + postFix;
        } else {
            return sourceFilePath + "." + postFix;
        }
    }

    public static void thumbnail(String filePath, String format) {
        File source = new File(filePath);
        if (!source.exists()) {
            throw new IllegalArgumentException("source file does not exists: " + source.getAbsoluteFile());
        }

        String targetPath = targetPath(filePath, format);
        File imageTarget = new File(targetPath);

        Encoder encoder = new IgnoreErrorEncoder();
        VideoAttributes video = new VideoAttributes();
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("image2");
        attrs.setOffset(0F);
        attrs.setDuration(1f);
        attrs.setVideoAttributes(video);
        try {
            encoder.encode(source, imageTarget, attrs);
        } catch (Exception e) {
            throw new IllegalStateException("error: ", e);
        }

    }

    public static void main(String[] args) {
        System.out.println(targetPath("/opt/a.amr", "mp3"));
        System.out.println(targetPath("/opt/a", "mp3"));
    }
}
