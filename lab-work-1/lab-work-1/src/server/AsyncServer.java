package server;

import interfaces.AsyncLCMCalculator;
import interfaces.LCMCallback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncServer extends UnicastRemoteObject implements AsyncLCMCalculator {

    private final ExecutorService threadPool;
    private final String serverName;

    protected AsyncServer(String name, int poolSize) throws RemoteException {
        super();
        this.serverName = name;
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        System.out.println("Асинхронный сервер '" + name + "' создан с пулом на " + poolSize + " потоков");
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
    public void calculateLCMAsync(int a, int b, LCMCallback callback) throws RemoteException {
        System.out.println("[" + serverName + "] Получен запрос: LCM(" + a + ", " + b + ")");

        threadPool.submit(() -> {
            try {
                // Статистика потоков
                System.out.println("   Поток выполнения: " + Thread.currentThread().getName());

                // Имитация задержки
                Thread.sleep(1500);

                if (a == 0 || b == 0) {
                    callback.onError("Числа не могут быть нулевыми");
                    return;
                }

                long result = (long)Math.abs(a) * ((long)Math.abs(b) / gcd(a, b));
                callback.onSuccess(result);

                System.out.println("[" + serverName + "] Результат отправлен");

            } catch (Exception e) {
                try {
                    callback.onError("Ошибка: " + e.getMessage());
                } catch (RemoteException ex) {
                    System.err.println("Не удалось отправить ошибку клиенту");
                }
            }
        });
    }

    public void shutdown() {
        threadPool.shutdown();
        System.out.println("Сервер '" + serverName + "' остановлен");
    }
}