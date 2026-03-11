package server;

import interfaces.LCMCalculator;
import interfaces.AsyncLCMCalculator;
import interfaces.LCMCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements LCMCalculator, AsyncLCMCalculator {

    private final ExecutorService threadPool;
    private static final int THREAD_POOL_SIZE = 10;

    public Server() {
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("Пул потоков создан. Размер: " + THREAD_POOL_SIZE);
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    @Override
    public long calculateLCM(int a, int b) throws RemoteException, IllegalArgumentException {
        if (a == 0 || b == 0) {
            throw new IllegalArgumentException("Числа не могут быть равны 0");
        }

        long lcm = (long)Math.abs(a) * ((long)Math.abs(b) / gcd(a, b));

        System.out.println("[СИНХРОННЫЙ] Вычисление НОК(" + a + ", " + b + ") = " + lcm + " в потоке: " + Thread.currentThread().getName());

        return lcm;
    }

    @Override
    public void calculateLCMAsync(int a, int b, LCMCallback callback) throws RemoteException {
        System.out.println("[АСИНХРОННЫЙ] Запрос на вычисление НОК(" + a + ", " + b + ") в потоке: " + Thread.currentThread().getName());

        threadPool.submit(() -> {
            try {
                Thread.sleep(2000);

                if (a == 0 || b == 0) {
                    callback.onError("Числа не могут быть равны 0");
                    return;
                }

                long result = (long)Math.abs(a) * ((long)Math.abs(b) / gcd(a, b));

                System.out.println("[АСИНХРОННЫЙ] Результат готов: " + result + " в потоке: " + Thread.currentThread().getName());

                callback.onSuccess(result);

            } catch (InterruptedException e) {
                try {
                    callback.onError("Вычисление было прервано");
                } catch (RemoteException ex) {
                    System.err.println("Ошибка при отправке ошибки клиенту: " + ex.getMessage());
                }
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                try {
                    callback.onError("Ошибка при вычислении: " + e.getMessage());
                } catch (RemoteException ex) {
                    System.err.println("Ошибка при отправке ошибки клиенту: " + ex.getMessage());
                }
            }
        });

        System.out.println("[АСИНХРОННЫЙ] Задача отправлена в пул потоков");
    }

    public void shutdown() {
        System.out.println("\nЗавершение работы сервера...");

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }

        System.out.println("Сервер остановлен");
    }

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server();
            final Server finalServer = server;

            Remote stub = UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry создан на порту 1099");

            registry.rebind("LCMCalculator", (LCMCalculator) stub);
            registry.rebind("AsyncLCMCalculator", (AsyncLCMCalculator) stub);

            System.out.println("\nRMI Сервер LCM запущен");
            System.out.println("Доступные сервисы:");
            System.out.println("   - Синхронный:   LCMCalculator");
            System.out.println("   - Асинхронный:  AsyncLCMCalculator");
            System.out.println("\nОжидание подключений клиентов...");
            System.out.println("   (Для остановки нажмите Ctrl+C)\n");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (finalServer != null) {
                    System.out.println("\nПолучен сигнал завершения...");
                    finalServer.shutdown();
                }
            }));

        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
            if (server != null) {
                server.shutdown();
            }
        }
    }
}