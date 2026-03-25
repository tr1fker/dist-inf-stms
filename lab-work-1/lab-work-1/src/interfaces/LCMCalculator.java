package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LCMCalculator extends Remote {
    long calculateLCM(int a, int b) throws RemoteException, IllegalArgumentException;
}