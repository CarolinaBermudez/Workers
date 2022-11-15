package com.example.login.ui.home;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.R;
import com.example.login.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private Button button_notificacion;
    private final static String CHANNEL_ID = "NOTIFICACIÓN";


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        button_notificacion = (Button) root.findViewById(R.id.button3);
        button_notificacion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getContext().getPackageName() + "/" + R.raw.notificacion1);
                System.out.println();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"NEW", NotificationManager.IMPORTANCE_DEFAULT);

                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();
                    channel.setSound(soundUri, audioAttributes);

                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(channel);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.email)
                            .setContentTitle("Titulo hola workers!!")
                            .setContentText("Contenido excelente dia workers :)")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            //.setSound(soundUri);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
                    managerCompat.notify(1,builder.build());

                }else{
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.email)
                            .setContentTitle("Titulo")
                            .setContentText("Contenido")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setSound(soundUri);
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
                    managerCompat.notify(1,builder.build());
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}