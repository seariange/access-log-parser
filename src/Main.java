import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;


class LineTooLongException extends RuntimeException {
    public LineTooLongException(String message) {
        super(message);
    }
}

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

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                String line;
                int totalLines = 0;
                int googleCount = 0;
                int yandexCount = 0;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) {
                        throw new LineTooLongException("Строка превышает 1024 символа: " + line);
                    }

                    totalLines++;

                    //обработка User-Agent
                    int startBracket = line.indexOf('(');
                    int endBracket = line.indexOf(')');
                    if (startBracket != -1 && endBracket != -1 && endBracket > startBracket) {
                        String firstBrackets = line.substring(startBracket + 1, endBracket);
                        String[] parts = firstBrackets.split(";");
                        if (parts.length >= 2) {
                            String fragment = parts[1].trim();
                            String program = fragment.split("/")[0];

                            if (program.equals("Googlebot")) {
                                googleCount++;
                            } else if (program.equals("YandexBot")) {
                                yandexCount++;
                            }
                        }
                    }
                }

                reader.close();


                System.out.println("Доля запросов от Googlebot: " + ((double) googleCount / totalLines));
                System.out.println("Доля запросов от YandexBot: " + ((double) yandexCount / totalLines));


                break;

            } catch (LineTooLongException e) {
                System.out.println("Ошибка: " + e.getMessage());
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}