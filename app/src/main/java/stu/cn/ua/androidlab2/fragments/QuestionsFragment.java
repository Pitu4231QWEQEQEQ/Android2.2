package stu.cn.ua.androidlab2.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import stu.cn.ua.androidlab2.R;
import stu.cn.ua.androidlab2.model.Player;
import stu.cn.ua.androidlab2.service.QuestionsService;

public class QuestionsFragment extends BaseFragment {
    private static final String TAG =
            QuestionsFragment.class.getSimpleName();
    private static final String ARG_PLAYER = "PLAYER";

    private EditText question;
    private Player player;
    private Button questionsButton;
    private ProgressBar progressBar;
    private QuestionsService service;
    private Handler handler;
    private Runnable runnable;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((QuestionsService.TestBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public static QuestionsFragment newInstance(Player player) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLAYER, player);
        QuestionsFragment fragment = new QuestionsFragment();
        fragment.setArguments(args);
        return fragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), QuestionsService.class);
        getActivity().startService(intent);
        return inflater.inflate(R.layout.fragment_questions,
                container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionsButton = view.findViewById(R.id.questionButton);
        progressBar = view.findViewById(R.id.progress);

        player = getPlayer();
        question = view.findViewById(R.id.questionEditText);

        setupButtons(view);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.cancelButton)
                .setOnClickListener(v -> {
                    getAppContract().cancel();
                });

        questionsButton.setOnClickListener(v -> {
            questionsButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            question.setEnabled(false);
            handler = new Handler();
            runnable = new Runnable() {
                public void run() {

                    String answer = service.getAnswer(question.getText() + " " + player.toString());

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(view.getContext(), answer, Toast.LENGTH_SHORT).show();
                    questionsButton.setEnabled(true);
                    question.setEnabled(true);
                }
            };
            handler.postDelayed(runnable, 3000);
        });
    }

    private Player getPlayer() {
        return getArguments().getParcelable(ARG_PLAYER);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Intent intent = new Intent(getContext(), QuestionsService.class);
        getActivity().bindService(intent, connection, 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(runnable);
        if(isDetached()){
            getActivity().unbindService(connection);
            Intent intent = new Intent(getActivity(), QuestionsService.class);
            getActivity().stopService(intent);
        }
    }
}