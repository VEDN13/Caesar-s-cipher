import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int choice = getMenuChoice(scanner);

            switch (choice) {
                case 1:
                    processFile(scanner, true);
                    break;
                case 2:
                    processFile(scanner, false);
                    break;
                case 3:
                    System.out.println("Вы вышли из программы");
                    return;
                default:
                    System.out.println("Некорректный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static int getMenuChoice(Scanner scanner) {
        System.out.println("Основное меню");
        System.out.println("1: Шифровать файл");
        System.out.println("2: Дешифровать файл");
        System.out.println("3: Выйти");
        System.out.print("Введите пункт меню: ");
        int choice = -1;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Некорректный выбор. Пожалуйста, попробуйте снова.");
            scanner.nextLine();
        }
        return choice;
    }

    private static void processFile(Scanner scanner, boolean encrypt) {
        String action = encrypt ? "зашифрования" : "дешифрования";
        String result = encrypt ? "_encrypted.txt" : "_decrypted.txt";

        System.out.print("Введите имя файла для " + action + ": ");
        String filename = scanner.nextLine();

        System.out.print("Введите сдвиг: ");
        int shift = getShift(scanner);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename + result))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = caesarCipher(line, encrypt ? shift : -shift);
                writer.write(processedLine);
                writer.newLine();
            }
            System.out.println("Файл " + action + " и сохранен как: " + filename + result);
        } catch (IOException e) {
            System.out.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    private static int getShift(Scanner scanner) {
        while (true) {
            int shift = 0;
            System.out.print("Введите сдвиг: ");
            try {
                shift = scanner.nextInt();
                scanner.nextLine();
                return shift;
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите целое число: ");
                scanner.nextLine();
            }
        }
    }

    private static String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                if (Character.UnicodeScript.of(ch) == Character.UnicodeScript.CYRILLIC) {
                    char base = Character.isLowerCase(ch) ? 'а' : 'А';
                    ch = (char) ((ch - base + shift + 32) % 32 + base);
                } else {
                    char base = Character.isLowerCase(ch) ? 'a' : 'A';
                    ch = (char) ((ch - base + shift + 26) % 26 + base);
                }
            }
            result.append(ch);
        }
        return result.toString();
    }
}