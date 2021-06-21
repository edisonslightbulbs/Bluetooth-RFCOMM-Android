package edisonslightbulbs.traceless;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Service {

    private static final String TAG = "SERVICE_DEBUG_TAG";

    public static class Listener extends Thread {

        private final BluetoothSocket m_socket;
        private final Handler m_handler;
        private final InputStream m_inputStream;

        public Listener(BluetoothSocket socket, Handler handler) {
            m_socket = socket;
            m_handler = handler;
            InputStream inputStream = null;

            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "-- failed to create input stream!", e);
            }
            m_inputStream = inputStream;
        }

        public void run() {
            byte[] buffer = new byte[1024];

            while (true) {
                try {
                    // read buffer and check its size
                    int bufferSize = m_inputStream.read(buffer);

                    // validate buffer size
                    if (bufferSize != 0) {

                        // pass buffer (and necessary args) to UI handler
                        Message message = m_handler.obtainMessage(
                                Flags.MESSAGE_READ, bufferSize, -1,
                                buffer);
                        message.sendToTarget();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "-- failed to read input stream!", e);
                    break;
                }
            }
        }

        public void closeSocket() {
            try {
                m_socket.close();
            } catch (IOException e) {
                Log.e(TAG, "-- failed to close socket", e);
            }
        }
    }

    public static class Caller extends Thread {

        private final BluetoothSocket m_socket;
        private final OutputStream m_outputStream;

        public Caller(BluetoothSocket socket) {
            m_socket = socket;

            OutputStream outputStream = null;

            try {
                outputStream = socket.getOutputStream();
                Log.e(TAG, "-- output stream created");
            } catch (IOException e) {
                Log.e(TAG, "-- failed to create output stream!", e);
            }
            m_outputStream = outputStream;
        }

        public void run() {
            String message = " Ohio-Senpai ";
            byte[] buffer =  message.getBytes();

            try {
                m_outputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void closeSocket() {
            try {
                m_socket.close();
            } catch (IOException e) {
                Log.e(TAG, "-- failed to close socket", e);
            }
        }
    }
}
