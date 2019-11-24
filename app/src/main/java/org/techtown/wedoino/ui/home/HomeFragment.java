package org.techtown.wedoino.ui.home;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.techtown.wedoino.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    TextView editText1;
    TextView editText2;
    ImageView leftimage;
    ImageView rightimage;
    Integer left;
    Integer right;
    Button button;
    Integer sum;
    Integer newleft;
    Integer newright;
    String[] token = new String[4];
    boolean connect = false;
    TextView leftangle;
    TextView rightangle;

    TextView mTvBluetoothStatus;

    TextView internalPressure; //압력, 기울기 표시할 textView들
    TextView internalAngle;
    TextView externalPressure;
    TextView externalAngle;

    TextView mTvSendData;
    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnConnect;
    Button mBtnSendData;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {



            }
        });
        return root;
    }
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        editText1 = (TextView)getView().findViewById(R.id.textView3);
        editText2 = (TextView)getView().findViewById(R.id.textView4);
        leftimage = (ImageView)getView().findViewById(R.id.imageView3);
        rightimage = (ImageView)getView().findViewById(R.id.imageView4);
        button = (Button)getView().findViewById(R.id.button);
        leftangle = (TextView) getView().findViewById(R.id.textView6);
        rightangle = (TextView) getView().findViewById(R.id.textView7);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //left = Integer.parseInt(editText1.getText().toString());
                //right = Integer.parseInt(editText2.getText().toString());

                //leftimage.getLayoutParams().height = 1022 - (left*1022/100);
                //rightimage.getLayoutParams().height = 1022- (right*1022/100);
                left = Integer.parseInt(token[0]);
                right = Integer.parseInt(token[2]);
                sum = left+right;
                newleft = left * 100/sum;
                newright = right * 100/sum;
                Toast.makeText(getContext(),String.valueOf(newleft), Toast.LENGTH_LONG).show();


                leftimage.getLayoutParams().height = 1022 - (newleft*1022/100);
                rightimage.getLayoutParams().height = 1022- (newright*1022/100);
                leftimage.requestLayout();
                rightimage.requestLayout();
                editText1.setText(newleft+"%");
                editText1.setText(newright+"%");
                leftangle.setText(token[1]);
                rightangle.setText(token[3]);

            }
        });


        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {

                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {
                                if(connect){
                                    try {
                                        left = Integer.parseInt(token[0]);
                                    } catch(NumberFormatException e){
                                        left= 0;
                                    }catch (Exception e){
                                        left= 0;
                                    }
                                    try {
                                        right = Integer.parseInt(token[2]);
                                    } catch(NumberFormatException e){
                                        right= 0;
                                    }catch (Exception e){
                                        right= 0;
                                    }
                                    if(left<0)
                                        left=0;
                                    if(right<0)
                                        right=0;

                                sum = left+right;
                                if(sum==0){
                                    newleft=50;
                                    newright=50;
                                }
                                else {
                                    newleft = left * 100 / sum;
                                    newright = right * 100 / sum;
                                }
                                Toast.makeText(getContext(),String.valueOf(newleft), Toast.LENGTH_LONG).show();


                                leftimage.getLayoutParams().height = 1022 - (newleft*1022/100);
                                rightimage.getLayoutParams().height = 1022- (newright*1022/100);
                                leftimage.requestLayout();
                                rightimage.requestLayout();
                                editText1.setText(newleft+"%");
                                editText1.setText(newright+"%");
                                leftangle.setText(token[1]);
                                rightangle.setText(token[3]);}
                            }
                        });
                    }

                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start();

    // 현재 시간을 반환



        mTvBluetoothStatus = (TextView)getView().findViewById(R.id.tvBluetoothStatus);

        internalPressure = (TextView)getView().findViewById(R.id.internalPressure); //기울기, 압력 받을 객체
        internalAngle = (TextView)getView().findViewById(R.id.internalAngle);
        externalPressure = (TextView)getView().findViewById(R.id.externalPressure);
        externalAngle = (TextView)getView().findViewById(R.id.externalAngle);

        mTvSendData =  (EditText) getView().findViewById(R.id.tvSendData);
        mBtnBluetoothOn = (Button)getView().findViewById(R.id.btnBluetoothOn);
        mBtnBluetoothOff = (Button)getView().findViewById(R.id.btnBluetoothOff);
        mBtnConnect = (Button)getView().findViewById(R.id.btnConnect);
        mBtnSendData = (Button)getView().findViewById(R.id.btnSendData);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        mBtnBluetoothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOn();
            }
        });
        mBtnBluetoothOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOff();
            }
        });
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });
        mBtnSendData.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write(mTvSendData.getText().toString());
                    mTvSendData.setText("");
                }
            }
        });
        mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;

                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8"); //입력 메세지 넣는 부분
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                    }
                    token = readMessage.split("A");
                    connect = true;



                }
            }
        };
    }

    public void refresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.detach(this).attach(this).commit();
    }

    void bluetoothOn() {
        if(mBluetoothAdapter == null) {
            Toast.makeText(getContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                mTvBluetoothStatus.setText("활성화");
            }
            else {
                Toast.makeText(getContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }
    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
            mTvBluetoothStatus.setText("비활성화");
        }
        else {
            Toast.makeText(getContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("활성화");
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getContext(), "취소", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("비활성화");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                getContext ();          }
        }
        else {
            getContext();      }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            Toast.makeText(getContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getCurrentTime() {
        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        String str = dayTime.format(new Date(time));

        return str;
    }
}