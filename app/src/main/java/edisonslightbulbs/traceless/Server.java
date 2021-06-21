package edisonslightbulbs.traceless;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class Server extends Thread {

    Handler m_handler;
    Service.Listener m_listener;
    private final BluetoothServerSocket m_socket;
    private static final String TAG = "SERVER_DEBUG_TAG";
    private static final UUID MY_UUID =
        UUID.fromString("d8308c4e-9469-4051-8adc-7a2663e415e2");

    public Server(Handler handler, BluetoothAdapter bluetoothAdapter) {
        m_handler = handler;
        BluetoothServerSocket socket = null;

        try {
            socket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("RECORD", MY_UUID);
            Log.e(TAG, "-- listening on rfcomm socket!");
        } catch (IOException e) {
            Log.e(TAG, "--failed to listen on rfcomm socket!", e);
        }
        m_socket = socket;
    }

    public void run() {
        BluetoothSocket socket;

        while (true) {
            try {
                socket = m_socket.accept();
                Log.e(TAG, "-- socket accepted successfully");
                service(socket);
            } catch (IOException e) {
                Log.e(TAG, "-- failed to accept socket!", e);
                break;
            }

        }
    }

    public void service(BluetoothSocket socket){
        m_listener  = new Service.Listener(socket, m_handler);
        m_listener.start();
    }

    public void closeSocket() {
        m_listener.closeSocket();
        try {
            m_socket.close();
        } catch (IOException e) {
            Log.e(TAG, "-- failed to close socket!", e);
        }
    }
}
