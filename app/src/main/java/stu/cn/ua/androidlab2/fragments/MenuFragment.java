package stu.cn.ua.androidlab2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import stu.cn.ua.androidlab2.R;
import stu.cn.ua.androidlab2.contract.ResponseListener;
import stu.cn.ua.androidlab2.model.Player;

public class MenuFragment extends BaseFragment {
    private static final String KEY_PLAYER = "PLAYER";
    private Button getPredictionButton;
    private Player player;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        registerListener(Player.class, listener);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            player = savedInstanceState
                    .getParcelable(KEY_PLAYER);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_menu,
                container,
                false
        );
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.optionsButton)
                .setOnClickListener(v -> {
                    getAppContract().toOptionsScreen(this, player);
                });
        view.findViewById(R.id.quitButton)
                .setOnClickListener(v -> {
                    getAppContract().cancel();
                });

        getPredictionButton = view
                .findViewById(R.id.getPredictionButton);
        getPredictionButton.setOnClickListener(v -> {
            getAppContract().toQuestionsScreen(this, player);
        });
        updateView();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_PLAYER, player);
    }
    private void updateView() {
        getPredictionButton.setEnabled(player != null
                && player.isValid());
    }
    private ResponseListener<Player> listener = player-> {
        this.player = player;
    };
}
