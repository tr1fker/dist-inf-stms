package utils;

import java.util.Scanner;

public class InputValidator {
    private final Scanner scanner;

    public InputValidator() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    public int readModeChoice() {
        while (true) {
            System.out.println("\nВыберите режим работы:");
            System.out.println("1. Синхронный вызов");
            System.out.println("2. Асинхронный вызов с callback");
            System.out.println("3. Выход");
            System.out.print("Ваш выбор: ");

            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    return choice;
                } else {
                    System.out.println("Ошибка: введите число от 1 до 3!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число!");
            }
        }
    }

    public boolean askContinue() {
        while (true) {
            System.out.print("\nХотите выполнить еще один расчет? (да/нет): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("да") || input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("нет") || input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("Пожалуйста, введите 'да' или 'нет'");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}