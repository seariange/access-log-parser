package logparser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int correctFileCount = 0;

        while (true) {
            System.out.print("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);

            if (!file.exists()) {
                System.out.println("Файл не существует. Попробуйте снова.");
                continue;
            }

            if (file.isDirectory()) {
                System.out.println("Указанный путь ведет к папке, а не к файлу. Попробуйте снова.");
                continue;
            }

            correctFileCount++;
            System.out.println("Путь указан верно");
            System.out.println("Это файл номер " + correctFileCount);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;
                int totalLines = 0;
                int googleCount = 0;
                int yandexCount = 0;

                Map<String, Integer> osStats = new HashMap<>();
                Map<String, Integer> browserStats = new HashMap<>();
                Statistics stats = new Statistics(); // создаём объект Statistics

                while ((line = reader.readLine()) != null) {

                    if (line.length() > 1024)
                        throw new LineTooLongException("Строка превышает 1024 символа: " + line);

                    totalLines++;

                    // --- Парсим строку через LogEntry ---
                    LogEntry entry = new LogEntry(line);

                    // --- Подсчёт Googlebot и YandexBot ---
                    if (entry.getUserAgent().isGoogleBot()) googleCount++;
                    if (entry.getUserAgent().isYandexBot()) yandexCount++;

                    // --- Статистика по ОС ---
                    String osType = entry.getUserAgent().getOsType();
                    osStats.put(osType, osStats.getOrDefault(osType, 0) + 1);

                    // --- Статистика по браузеру ---
                    String browserType = entry.getUserAgent().getBrowserType();
                    browserStats.put(browserType, browserStats.getOrDefault(browserType, 0) + 1);

                    // --- Добавляем запись в статистику трафика ---
                    stats.addEntry(entry);
                }

                // --- Вывод статистики ---
                System.out.println("\nВсего строк в логе: " + totalLines);
                System.out.printf("Доля запросов от Googlebot: %.2f%%\n", (100.0 * googleCount / totalLines));
                System.out.printf("Доля запросов от YandexBot: %.2f%%\n", (100.0 * yandexCount / totalLines));

                System.out.println("\nСтатистика по ОС:");
                for (Map.Entry<String, Integer> entry : osStats.entrySet()) {
                    String os = entry.getKey();
                    int count = entry.getValue();
                    System.out.printf("%s: %d (%.2f%%)\n", os, count, 100.0 * count / totalLines);
                }

                System.out.println("\nСтатистика по браузерам:");
                for (Map.Entry<String, Integer> entry : browserStats.entrySet()) {
                    String browser = entry.getKey();
                    int count = entry.getValue();
                    System.out.printf("%s: %d (%.2f%%)\n", browser, count, 100.0 * count / totalLines);
                }

                // --- Средний трафик за час ---
                System.out.println("\nОбщий трафик: " + stats.getTotalTraffic() + " байт");
                System.out.printf("Средний трафик за час: %.2f байт/час\n", stats.getTrafficRate());

                break; // выходим после успешной обработки

            } catch (LineTooLongException e) {
                System.out.println("Ошибка: " + e.getMessage());
                break;

            } catch (Exception ex) {
                System.out.println("Ошибка при разборе файла:");
                ex.printStackTrace();
            }
        }
    }
}
