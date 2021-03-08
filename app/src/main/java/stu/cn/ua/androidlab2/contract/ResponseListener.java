package stu.cn.ua.androidlab2.contract;

public interface ResponseListener<T> {
    void onResults(T results);
}
