import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            task1(scanner);
            task2(scanner);
        }
    }

    /**
     * 1. Написать программу для проверки на валидность введенного ip адреса.
     * Пусть ip адрес задается с консоли.
     * Программа должна проверять валидность введенного ip адреса
     * с помощью регулярного выражения и выводить результат проверки на экран.
     */
    private static void task1(Scanner scanner) {
        System.out.println("__________ Task 1 __________");
        System.out.println("Enter ip-address");
        String ip = scanner.nextLine();
        String regex = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        boolean matches = Pattern.matches(regex, ip);
        if (matches) {
            System.out.println("The entered ip-address is valid");
        } else {
            System.err.println("The entered ip-address is invalid");
        }
    }

    /**
     * 2. Программа на вход получает путь к папке (задается через консоль).
     * В заданной папке находятся текстовые файлы (формат тхт).
     * Каждый файл содержит произвольный текст.
     * В этом тексте может быть номер документа(один или несколько), е-мейл и номер телефона.
     * Номер документа в формате: xxxx-yyy-xxxx-yyy-xyxy, где x - это любая цифра,
     * а y - это любая буква русского или латинского алфавита.
     * Номер телефона в формате: +(ХХ)ХХХХХХХ.
     * Документ может содержать не всю информацию,
     * т.е. например, может не содержать номер телефона или другое поле.
     * Необходимо извлечь информацию из N текстовых документов.
     * Число документов для обработки N задается с консоли.
     * Если в папке содержится меньше документов, чем заданное число, то следует обрабатывать все документы.
     * Извлеченную информацию необходимо сохранить в следующую структуру данных: Map<String, Document>,
     * где ключ типа String - это имя документа без расширения,
     * значение типа Document - объект кастомного класса,
     * поля которого содержат извлеченную из текстового документа информацию.
     * Учесть вывод сообщений на случаи если:
     * - на вход передан путь к папке, в которой нет файлов;
     * - все файлы имеют неподходящий формат (следует обрабатывать только тхт файлы);
     * - сообщения на случай других исключительных ситуаций.
     * В конце работы программы следует вывести сообщение о том,
     * сколько документов обработано и сколько было документов невалидного формата.
     */
    private static void task2(Scanner scanner) throws IOException {
        System.out.println("__________ Task 2 __________");
        System.out.println("Enter path");          //resources
        String folderPath = scanner.nextLine();
        System.out.println("Enter number of files");
        int numOfFiles = scanner.nextInt();

        Set<Path> filesPaths = getPathSet(folderPath);
        Map<String, Document> dataMap = getDataMap(numOfFiles, filesPaths);
        getInformation(filesPaths, dataMap);
    }

    private static Set<Path> getPathSet(String folderPath) throws IOException {
        Set<Path> filesPaths;
        try (Stream<Path> list = Files.list(Paths.get(folderPath))) {
            filesPaths = list.collect(Collectors.toSet());
        }
        return filesPaths;
    }

    private static Map<String, Document> getDataMap(int numOfFiles, Set<Path> filesPaths) throws IOException {
        String regexEmail = "[a-zA-Z].*@\\w+\\.[a-z]{2,6}";
        String regexPhone = "\\+\\(\\d{2}\\)\\d{7}";
        String regexDocNum = "(\\d{4}-[a-zA-Zа-яА-Я]{3}-){2}\\d[a-zA-Zа-яА-Я]\\d[a-zA-Zа-яА-Я]";
        String regexPath = ".+\\.(txt)";

        Map<String, Document> dataMap = new HashMap<>();

        int counter = 0;
        if (filesPaths.size() >= 1) {
            for (Path filePath : filesPaths) {
                if (Pattern.matches(regexPath, filePath.toString())) {
                    String text = Files.readString(Paths.get(String.valueOf(filePath)));
                    String[] str = text.split("\\s+");
                    Document document = new Document();
                    for (String s : str) {
                        if (s.matches(regexDocNum)) {
                            document.setDocNum(s);
                        } else if (s.matches(regexEmail)) {
                            document.setEmail(s);
                        } else if (s.matches(regexPhone)) {
                            document.setPhone(s);
                        }
                    }
                    dataMap.put((filePath.getFileName().toString().replaceAll("\\.(txt)", "")), document);
                }
                counter++;
                if (counter == numOfFiles) break;
            }
            System.out.println(dataMap);
        } else {
            System.out.println("There are no files in the folder");
        }
        return dataMap;
    }

    private static void getInformation(Set<Path> filesPaths, Map<String, Document> dataMap) {
        int num = filesPaths.size() - dataMap.size();
        if (num == 0) {
            System.out.println("All files have the wrong format");
        } else {
            System.out.println(dataMap.size() + " files were processed. There were " + num + " documents of an invalid format");
        }
    }
}