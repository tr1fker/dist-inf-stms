package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AsyncLCMCalculator extends Remote {
    void calculateLCMAsync(int a, int b, LCMCallback callback) throws RemoteException;
}