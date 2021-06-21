package edisonslightbulbs.traceless;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    Button callButton;
    Button listenButton;
    Button closeClientSocketButton;
    Button closeServerSocketButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind main activity resources
        callButton = findViewById(R.id.callButton);
        listenButton = findViewById(R.id.listenButton);
        closeClientSocketButton = findViewById(R.id.closeClientSocketButton);
        closeServerSocketButton = findViewById(R.id.closeServerSocketButton);

        Driver driver = new Driver(this);

        AtomicBoolean clientOnline = new AtomicBoolean(false);
        AtomicBoolean serverOnline = new AtomicBoolean(false);

        // open RFCOMM (TX) channel
        callButton.setOnClickListener(v -> {
            try {
                driver.call();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            clientOnline.set(true);
            Utils.toast(this, "setting up client and sending messages to remote server");
            driver.updateUI();
        });

        // open RFCOMM (RX) channel
        listenButton.setOnClickListener(v -> {
            driver.listen();
            serverOnline.set(true);
            Utils.toast(this, "setting up server and listening for massages from remote client");
        });

        closeClientSocketButton.setOnClickListener(v -> {
            boolean close = clientOnline.get();
            if(close){
                driver.m_client.closeSocket();
            }
        });

        closeServerSocketButton.setOnClickListener(v -> {
            boolean close = serverOnline.get();
            if(close){
                driver.m_server.closeSocket();
            }
        });
    }
}
