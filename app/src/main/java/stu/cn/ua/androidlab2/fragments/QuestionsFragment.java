package stu.cn.ua.androidlab2.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import stu.cn.ua.androidlab2.R;
import stu.cn.ua.androidlab2.model.Player;
import stu.cn.ua.androidlab2.service.QuestionsService;

public class QuestionsFragment extends BaseFragment {
    private static final String TAG =
            QuestionsFragment.class.getSimpleName();
    private static final String ARG_PLAYER = "PLAYER";
    private static final String KEY_VISIBLE = "VISIBLE";
    private static final String KEY_RUNNABLE = "RUNNABLE";
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private EditText question;
    private Player player;
    private Button questionsButton;
    private ProgressBar progressBar;
    private QuestionsService service;
    private Handler handler;
    private Runnable runnable;
    private Boolean isVisible = true;
    private View view;

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
        this.view = view;
        questionsButton = view.findViewById(R.id.questionButton);
        progressBar = view.findViewById(R.id.progress);

        player = getPlayer();
        question = view.findViewById(R.id.questionEditText);

        if(savedInstanceState != null){
            isVisible = savedInstanceState.getBoolean(KEY_VISIBLE);
            progressBar = view.findViewById(R.id.progress);
            onVisible(isVisible);
        }

        setupButtons(view);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.cancelButton)
                .setOnClickListener(v -> {

                    getAppContract().cancel();
                });

        questionsButton.setOnClickListener(v -> {
            onVisible(false);
            handler = new Handler();
            runnable = () -> {
                String answer = service.getAnswer(question.getText() + " " + player.toString(), this);
                Toast.makeText(view.getContext(), answer, Toast.LENGTH_SHORT).show();
                onVisible(true);
            };
            handler.postDelayed(runnable, 3000);
        });
    }

    public void onVisible(Boolean state) {
        this.isVisible = state;
        view.findViewById(R.id.questionButton).setEnabled(state);
        if(state)
            view.findViewById(R.id.progress).setVisibility(View.INVISIBLE);
        else
            view.findViewById(R.id.progress).setVisibility(View.VISIBLE);
        view.findViewById(R.id.questionEditText).setEnabled(state);
    }

    private Player getPlayer() {
        return getArguments().getParcelable(ARG_PLAYER);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VISIBLE, isVisible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getContext(), QuestionsService.class);
        getActivity().bindService(intent, connection, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (runnable != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(connection);
        Intent intent = new Intent(getActivity(), QuestionsService.class);
        getActivity().stopService(intent);
    }


}