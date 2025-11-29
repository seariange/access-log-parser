package logparser;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {

    private final String ip;
    private final LocalDateTime dateTime;
    private final HttpMethod method;
    private final String path;
    private final int statusCode;
    private final long responseSize;
    private final String referer;
    private final UserAgent userAgent; // теперь объект UserAgent

    // --- Конструктор с параметрами ---
    public LogEntry(String ip,
                    LocalDateTime dateTime,
                    HttpMethod method,
                    String path,
                    int statusCode,
                    long responseSize,
                    String referer,
                    UserAgent userAgent) {

        this.ip = ip;
        this.dateTime = dateTime;
        this.method = method;
        this.path = path;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    // --- Конструктор, принимающий строку лога ---
    public LogEntry(String line) {
        try {
            String[] parts = line.split(" ");

            this.ip = parts[0];

            // Дата
            int start = line.indexOf('[');
            int end = line.indexOf(']');
            String dateStr = line.substring(start + 1, end);
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            this.dateTime = OffsetDateTime.parse(dateStr, formatter).toLocalDateTime();

            // Request
            int reqStart = line.indexOf('"');
            int reqEnd = line.indexOf('"', reqStart + 1);
            String request = line.substring(reqStart + 1, reqEnd);
            String[] reqParts = request.split(" ");
            this.method = HttpMethod.valueOf(reqParts[0]);
            this.path = reqParts[1];

            // Код ответа и размер
            int afterReq = reqEnd + 2;
            String[] after = line.substring(afterReq).split(" ");
            this.statusCode = Integer.parseInt(after[0]);
            this.responseSize = Long.parseLong(after[1]);

            // Referer
            int refStart = line.indexOf('"', reqEnd + 1);
            int refEnd = line.indexOf('"', refStart + 1);
            this.referer = line.substring(refStart + 1, refEnd);

            // User-Agent
            int agentStart = line.indexOf('"', refEnd + 1);
            int agentEnd = line.indexOf('"', agentStart + 1);
            String uaString = line.substring(agentStart + 1, agentEnd);
            this.userAgent = new UserAgent(uaString); // создаём объект UserAgent

        } catch (Exception e) {
            throw new RuntimeException("Ошибка разбора строки: " + line, e);
        }
    }

    // --- Геттеры ---
    public String getIp() { return ip; }
    public LocalDateTime getDateTime() { return dateTime; }
    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public int getStatusCode() { return statusCode; }
    public long getResponseSize() { return responseSize; }
    public String getReferer() { return referer; }
    public UserAgent getUserAgent() { return userAgent; }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ip='" + ip + '\'' +
                ", dateTime=" + dateTime +
                ", method=" + method +
                ", path='" + path + '\'' +
                ", statusCode=" + statusCode +
                ", responseSize=" + responseSize +
                ", referer='" + referer + '\'' +
                ", userAgent=" + userAgent +
                '}';
    }
}
