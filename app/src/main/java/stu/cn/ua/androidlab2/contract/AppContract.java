package stu.cn.ua.androidlab2.contract;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import stu.cn.ua.androidlab2.model.Player;

public interface AppContract {
    /**
     * Launch options screen
     * @param target fragment that launches options screen
     * @param player data about the student to be displayed
     */
    void toOptionsScreen(Fragment target,
                         @Nullable Player player);
    /**
     * Launch results screen
     * @param target fragment that launches results screen
     * @param player data used for calculating variant
    91
     */
    void toQuestionsScreen(Fragment target, Player player);
    /**
     * Exit from the current screen
     */
    void cancel();
    /**
     * Publish results to the target screen
     */
    <T> void publish(T data);
    /**
     * Listen for results from other screens
     */
    <T> void registerListener(Fragment fragment, Class<T> clazz,
                              ResponseListener<T> listener);
    /**
     * Stop listening for results from other screens
     */
    void unregisterListeners(Fragment fragment);
}

