package com.thread.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 函数作用：
 * onPreExecute、onProgressUpdate、onPostExecute都是更新UI,主线程执行
 * doInBackground 子线程执行，耗时操作
 * <p/>
 * 必须重写的方法： doInBackgroup() 和 onPostExecute(String result)
 * 选择性重写的方法： onProgressUpdate(Integer... values) 、onPreExecute() 、 onCancelled()
 *
 * @author yline 2016/10/30 --> 13:22
 * @version 1.0.0
 */
public class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, String> {
    private TextView textView;

    private ProgressBar progressBar;

    /**
     * 实例化
     */
    public ProgressBarAsyncTask(TextView textView, ProgressBar progressBar) {
        super();
        this.textView = textView;
        this.progressBar = progressBar;
    }

    /**
     * 执行任务之前,对 UI 操作
     */
    @Override
    protected void onPreExecute() {
        textView.setText("开始执行异步线程");
    }

    /**
     * 子线程中执行
     * params -> AsyncTask的第一个参数
     * 返回值  -> AsyncTask的第三个参数
     * 该方法不运行在UI线程当中，主要用于异步操作，不能对UI操作
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    @Override
    protected String doInBackground(Integer... params) {
        float i = 0;
        for (; i < params[1]; i++) {
            Log.i("config", "" + i * 100 / params[1]);
            try {  // 模拟 耗时的操作
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int process = (int) ((i + 1) * 100 / params[1]);
            publishProgress(process);  // 传值到 onProgressUpdate
        }
        return i + "";
    }

    /**
     * result -> AsyncTask的第三个参数（也就是接收doInBackground的返回值）
     * 对 UI 操作,操作结束后执行
     */
    @Override
    protected void onPostExecute(String result) {
        textView.setText("异步操作执行结束" + result);
    }

    /**
     * 调用publishProgress时,触发onProgressUpdate执行,很快更新UI
     * values -> AsyncTask的第二个参数
     * 可以 对UI进行操作
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int value = values[0];
        textView.setText("当前进度: " + value + "%");
        progressBar.setProgress(value);  // 总数100
    }

    /**
     * 调用取消时的操作
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
