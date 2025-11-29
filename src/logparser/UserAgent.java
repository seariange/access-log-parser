package logparser;

public class UserAgent {
    private final String osType;
    private final String browserType;
    private final String originalUA; // сохраняем исходную строку

    public UserAgent(String userAgentString) {
        this.originalUA = userAgentString;

        // ОС
        if (userAgentString.contains("Windows")) this.osType = "Windows";
        else if (userAgentString.contains("Macintosh") || userAgentString.contains("Mac OS")) this.osType = "macOS";
        else if (userAgentString.contains("Linux") || userAgentString.contains("X11")) this.osType = "Linux";
        else this.osType = "Other";

        // Браузер
        if (userAgentString.contains("Edge")) this.browserType = "Edge";
        else if (userAgentString.contains("Firefox")) this.browserType = "Firefox";
        else if (userAgentString.contains("Chrome") && !userAgentString.contains("Chromium")) this.browserType = "Chrome";
        else if (userAgentString.contains("Opera") || userAgentString.contains("OPR")) this.browserType = "Opera";
        else this.browserType = "Other";
    }

    public String getOsType() { return osType; }
    public String getBrowserType() { return browserType; }
    public String getOriginalUA() { return originalUA; }

    public boolean isGoogleBot() { return originalUA.contains("Googlebot"); }
    public boolean isYandexBot() { return originalUA.contains("YandexBot"); }
}
