package org.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private static final String API_URL = "https://api.mymemory.translated.net/get";

    private static final Map<String, String> LANG_CODES = new HashMap<>();
    static {
        LANG_CODES.put("English",    "en");
        LANG_CODES.put("Khmer",      "km");
        LANG_CODES.put("Chinese",    "zh");
        LANG_CODES.put("Japanese",   "ja");
        LANG_CODES.put("Korean",     "ko");
        LANG_CODES.put("French",     "fr");
        LANG_CODES.put("Spanish",    "es");
        LANG_CODES.put("German",     "de");
       
        LANG_CODES.put("Vietnamese", "vi");
        LANG_CODES.put("Indonesian", "id");
        LANG_CODES.put("Arabic",     "ar");
        LANG_CODES.put("Portuguese", "pt");
        LANG_CODES.put("Russian",    "ru");
        LANG_CODES.put("Hindi",      "hi");
    }

    public String translate(String text, String src, String tgt) throws Exception {
        String srcCode = src.equals("Auto Detect") ? "autodetect" : LANG_CODES.getOrDefault(src, "en");
        String tgtCode = LANG_CODES.getOrDefault(tgt, "en");

        String langPair    = srcCode + "|" + tgtCode;
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String encodedPair = URLEncoder.encode(langPair, StandardCharsets.UTF_8);
        String url         = API_URL + "?q=" + encodedText + "&langpair=" + encodedPair;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return parseResponse(response.body(), src);
    }

    private String parseResponse(String json, String src) throws Exception {
        // Check status
        int statusIdx = json.indexOf("\"responseStatus\":");
        if (statusIdx != -1) {
            int s = statusIdx + 17;
            int e = json.indexOf(",", s);
            if (e == -1) e = json.indexOf("}", s);
            String status = json.substring(s, e).trim();
            if (!status.equals("200")) throw new Exception("Translation failed (status " + status + ")");
        }

        // Extract translatedText
        int idx = json.indexOf("\"translatedText\":");
        if (idx == -1) throw new Exception("Unexpected API response");
        int start = json.indexOf("\"", idx + 17) + 1;
        int end   = json.indexOf("\"", start);
        String translated = unescapeUnicode(
                json.substring(start, end)
                        .replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">")
                        .replace("&quot;", "\"").replace("&#39;", "'").replace("\\n", "\n")
        );

        // Detected language for auto mode
        if (src.equals("Auto Detect")) {
            int dIdx = json.indexOf("\"detectedLanguage\":");
            if (dIdx != -1) {
                int dStart = json.indexOf("\"", dIdx + 19) + 1;
                int dEnd   = json.indexOf("\"", dStart);
                String detLang = json.substring(dStart, dEnd);
                if (!detLang.isEmpty()) translated += "\n[Detected: " + detLang + "]";
            }
        }

        return translated;
    }

    private String unescapeUnicode(String s) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            if (i + 5 < s.length() && s.charAt(i) == '\\' && s.charAt(i + 1) == 'u') {
                try {
                    int code = Integer.parseInt(s.substring(i + 2, i + 6), 16);
                    sb.append((char) code);
                    i += 6;
                } catch (NumberFormatException e) {
                    sb.append(s.charAt(i++));
                }
            } else {
                sb.append(s.charAt(i++));
            }
        }
        return sb.toString();
    }
}