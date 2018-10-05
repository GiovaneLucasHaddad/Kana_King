package com.example.android.kanaking.control;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.example.android.kanaking.Constantes.DEVICE_NAME;
import static com.example.android.kanaking.Constantes.MENSAGEM_ESCREVER;
import static com.example.android.kanaking.Constantes.MENSAGEM_LER;
import static com.example.android.kanaking.Constantes.MENSAGEM_MUDANCA_ESTADO;
import static com.example.android.kanaking.Constantes.MENSAGEM_NOME_DISPOSITIVO;
import static com.example.android.kanaking.Constantes.MENSAGEM_TOAST;
import static com.example.android.kanaking.Constantes.TOAST;

public class BluetoothService {

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    //
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private ThreadAceitar mAcceptThread;
    private ThreadConectar mConnectThread;
    private ThreadConectado mConnectedThread;
    private int estado;
    private int mNewState;

    // Estado da conexão
    public static final int ESTADO_NENHUM = 0;      // Nenhum estado
    public static final int ESTADO_OUVINDO = 1;     // Ouvindo conexões de entrada
    public static final int ESTADO_CONECTANDO = 2;  // Iniciando conexão de saída
    public static final int ESTADO_CONECTADO = 3;   // Conectado a um dispositivo

    public BluetoothService(Context context, Handler handler){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        estado = ESTADO_NENHUM;
        mNewState = estado;
        mHandler = handler;
    }

    /**
     * Update UI title according to the current state of the chat connection
     */
    private synchronized void atualizarTituloDaUI() {
        estado = getEstado();
        mNewState = estado;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(MENSAGEM_MUDANCA_ESTADO, mNewState, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getEstado() {
        return estado;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {

        // Cancelar qualquer thread tentando fazer uma conexão
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancelar qualquer thread executando uma conexão
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Iniciar a Thread para ouvir em um BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new ThreadAceitar();//true);//TODO - potencial
            mAcceptThread.start();
            //TODO - Potencial para a tripla conexão
        }
        // Atualizar o título da UI
        atualizarTituloDaUI();
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device){//, boolean secure) {//TODO-Potencial
        // Cancelar qualquer thread tentando fazer uma conexão
        if (estado == ESTADO_CONECTANDO) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancelar qualquer thread executando uma conexão
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Iniciar a Thread para conectar ao dispositivo fornecido
        mConnectThread = new ThreadConectar(device);//, secure);
        mConnectThread.start();
        // Atualizar o título da UI
        atualizarTituloDaUI();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {

        // Cancelar a Thread que completou a conexão
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancelar qualquer thread executando uma conexão
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancelar a Thread de aceitação para conectar a apenas um dispositivo
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Iniciar a thread para gerenciar a conexão e realizar transmissões
        mConnectedThread = new ThreadConectado(socket, socketType);
        mConnectedThread.start();

        // Enviar o nome do dispositivo de volta à UI
        Message msg = mHandler.obtainMessage(MENSAGEM_NOME_DISPOSITIVO);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Atualizar o título da UI
        atualizarTituloDaUI();
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        estado = ESTADO_NENHUM;
        // Atualizar o título da UI
        atualizarTituloDaUI();
    }

    public void write(byte[] out) {
        //  Criando objeto tepmorario
        ThreadConectado r;
        // Sincronizar uma cópia da ThreadConectado
        synchronized (this) {
            if (estado != ESTADO_CONECTADO) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    private void conexaoFalhou() {
        // Enviar uma mensagem de falha à UI
        Message msg = mHandler.obtainMessage(MENSAGEM_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        estado = ESTADO_NENHUM;
        // Atualizar o título da UI
        atualizarTituloDaUI();

        // Iniciar o serviço para reiniciar o modo de escuta
        BluetoothService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void conexaoPerdida() {
        // Enviar uma mensagem de falha de volta à Activity
        Message msg = mHandler.obtainMessage(MENSAGEM_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, "Conexão do dispositivo foi perdida");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        estado = ESTADO_NENHUM;

        atualizarTituloDaUI();

        // Iniciar o serviço para reiniciar o modo de escuta
        BluetoothService.this.start();
    }

    private class ThreadAceitar extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public ThreadAceitar(){//boolean secure) {//TODO - potencial
            BluetoothServerSocket tmp = null;
//            mSocketType = secure ? "Secure" : "Insecure";

            // Create a new listening server socket
            try {
//                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,MY_UUID_SECURE);
//                } else {
//                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
//                            NAME_INSECURE, MY_UUID_INSECURE);
//                }
            } catch (IOException e) {
                //Falha ao conectar socket
            }
            mmServerSocket = tmp;
            estado = ESTADO_OUVINDO;
        }

        public void run() {
            setName("ThreadAceitar" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (estado != ESTADO_CONECTADO) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    //Falha na aceitação do socket
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (estado) {
                            case ESTADO_OUVINDO:
                            case ESTADO_CONECTANDO:
                                // Situação normal. Iniciar a ThreadConectado.
                                connected(socket, socket.getRemoteDevice(),mSocketType);
                                break;
                            case ESTADO_NENHUM:
                            case ESTADO_CONECTADO:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    //Não foi possível fechar o soquete indesejado
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                //Falha no fechamento do socket do servidor
            }
        }
    }

    private class ThreadConectar extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ThreadConectar(BluetoothDevice device){//, boolean secure) {//TODO - potencial
            mmDevice = device;
            BluetoothSocket tmp = null;
//            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
//                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
//                } else {
//                    tmp = device.createInsecureRfcommSocketToServiceRecord(
//                            MY_UUID_INSECURE);
//                }
            } catch (IOException e) {
                //Falha na criação do socket
            }
            mmSocket = tmp;
            estado = ESTADO_CONECTANDO;
        }

        public void run() {
            setName("ThreadAceitar" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    //Falha: não foi possível fechar o socket durante a conexão
                }
                conexaoFalhou();
                return;
            }

            // Reset the ThreadConectar because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                //Falha do fechamento do socket da conexão
            }
        }
    }

    private class ThreadConectado extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ThreadConectado(BluetoothSocket socket, String socketType) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Sockets temporarios não criados
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            estado = ESTADO_CONECTADO;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Manter ouvindo o InputStream enquanto conectado
            while (estado == ESTADO_CONECTADO) {
                try {
                    // Ler do InputStream
                    bytes = mmInStream.read(buffer);

                    // Enviar os bytes obtidos à UI
                    mHandler.obtainMessage(MENSAGEM_LER, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    //Desconectado
                    conexaoPerdida();
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(MENSAGEM_ESCREVER, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                //Exceção durante a leitura
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                //Falha no fechamento do socket conectado
            }
        }
    }
}
