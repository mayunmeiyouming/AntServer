package Http;

import java.util.HashMap;

public class MimeTypes {
    public static HashMap mimeMap = new HashMap();

    static {
        mimeMap.put("", "content/unknown");
        mimeMap.put(".aac", "audio/aac");
        mimeMap.put(".abw", "application/x-abiword");
		mimeMap.put(".arc", "application/x-freearc");
		mimeMap.put(".avi", "video/x-msvideo");
		mimeMap.put(".azw", "application/vnd.amazon.ebook");
		mimeMap.put(".bin", "application/octet-stream");
		mimeMap.put(".bmp", "image/bmp");
		mimeMap.put(".bz", "application/x-bzip");
		mimeMap.put(".bz2", "application/x-bzip2");
		mimeMap.put(".csh", "application/x-csh");
		mimeMap.put(".css", "text/css");
		mimeMap.put(".csv", "text/csv");
		mimeMap.put(".doc", "application/msword");
		mimeMap.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeMap.put(".eot", "application/vnd.ms-fontobject");
		mimeMap.put(".epub", "application/epub+zip");
		mimeMap.put(".gif", "image/gif");
		mimeMap.put(".htm", "text/html");
		mimeMap.put(".html", "text/html");
		mimeMap.put(".ico", "image/vnd.microsoft.icon");
		mimeMap.put(".ics", "text/calendar");
		mimeMap.put(".jar", "application/java-archive");
		mimeMap.put(".jpg", "image/jpeg");
		mimeMap.put(".jpeg", "image/jpeg");
		mimeMap.put(".js", "text/javascript");
		mimeMap.put(".json", "application/json");
		mimeMap.put(".jsonld", "application/ld+json");
		mimeMap.put(".mid", "audio/midi");
		mimeMap.put(".midi", "audio/midi");
		mimeMap.put(".mjs", "text/javascript");
		mimeMap.put(".mp3", "audio/mpeg");
		mimeMap.put(".mpeg", "video/mpeg");
		mimeMap.put(".mpkg", "application/vnd.apple.installer+xml");
		mimeMap.put(".odp", "application/vnd.oasis.opendocument.presentation");
		mimeMap.put(".ods", "application/vnd.oasis.opendocument.spreadsheet");
		mimeMap.put(".odt", "application/vnd.oasis.opendocument.text");
		mimeMap.put(".oga", "audio/ogg");
		mimeMap.put(".ogv", "video/ogg");
		mimeMap.put(".ogx", "application/ogg");
		mimeMap.put(".otf", "font/otf");
		mimeMap.put(".png", "image/png");
		mimeMap.put(".pdf", "application/pdf");
		mimeMap.put(".ppt", "application/vnd.ms-powerpoint");
		mimeMap.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		mimeMap.put(".rar", "application/x-rar-compressed");
		mimeMap.put(".rtf", "application/rtf");
		mimeMap.put(".sh", "application/x-sh");
		mimeMap.put(".svg", "image/svg+xml");
		mimeMap.put(".swf", "application/x-shockwave-flash");
		mimeMap.put(".tar", "application/x-tar");
		mimeMap.put(".tif", "image/tiff");
		mimeMap.put(".tiff", "image/tiff");
		mimeMap.put(".ttf", "font/ttf");
		mimeMap.put(".txt", "text/plain");
		mimeMap.put(".vsd", "application/vnd.visio");
		mimeMap.put(".wav", "audio/wav");
		mimeMap.put(".weba", "audio/webm");
		mimeMap.put(".webm", "video/webm");
		mimeMap.put(".webp", "image/webp");
		mimeMap.put(".woff", "font/woff");
		mimeMap.put(".woff2", "font/woff2");
		mimeMap.put(".xhtml", "application/xhtml+xml");
		mimeMap.put(".xls", "application/vnd.ms-excel");
		mimeMap.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		// application/xml 代码对普通用户来说不可读 (RFC 3023, section 3)
		// text/xml 代码对普通用户来说可读 (RFC 3023, section 3)
		mimeMap.put(".xml", "text/xml");
		mimeMap.put(".xul", "application/vnd.mozilla.xul+xml");
		mimeMap.put(".zip", "application/zip");
		// video/3gpp
		// audio/3gpp（若不含视频）
		mimeMap.put(".3gp", "video/3gpp");
		// video/3gpp2
		// audio/3gpp2（若不含视频）
		mimeMap.put(".3g2", "video/3gpp2");
		mimeMap.put(".7z", "application/x-7z-compressed");
    }

	public static String getContentType(String extension) {
        String contentType = (String) mimeMap.get(extension);
        if (contentType == null)
            contentType = "text/plain";
        return contentType;
    }

}