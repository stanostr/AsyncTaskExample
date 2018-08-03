package com.stanislavveliky.asynctaskexample;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.Build.VERSION.SDK;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private int mResult;
    private Button mOkButton;
    private EditText mEntryText;
    private TextView mResultView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEntryText = findViewById(R.id.number_edit_text);
        mOkButton = findViewById(R.id.ok_button);
        mProgressBar = findViewById(R.id.progress_bar);
        mResultView = findViewById(R.id.result_view);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               calculate();
            }
        });
    }

    private void calculate()
    {
        String entry = mEntryText.getText().toString();
        if(entry.trim().length()==0)
        {
            Toast.makeText(this, R.string.no_input_message, Toast.LENGTH_SHORT).show();
            return;
        }
        new FibAsyncTask().execute(entry);
    }

    private class FibAsyncTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            mResultView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(false);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                mProgressBar.setProgress(0, true);
            else mProgressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            int n = Integer.valueOf(params[0]);

            //Fake thinking
            for (int i = 0; i <= 100; i++) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            n = fibTopDown(n, new int[n+1]);
            return String.valueOf(n);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                mProgressBar.setProgress(mProgressBar.getProgress()+1, true);
            else mProgressBar.setProgress(mProgressBar.getProgress()+1);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            mResultView.setVisibility(View.VISIBLE);
            mResultView.setText(getString(R.string.result, result));
        }
    }

    /**
     * Dynamic algorithm for finding Fibonacci numbers
     * @param n which number in the sequence you want to find
     * @param fib an array to hold all the previous numbers, pass in empty array of size n+1
     * @return the Fibonacci number
     */
    public static int fibTopDown(int n, int [] fib) {
        if(n==0) return 0;
        if(n==1) return 1;
        if(fib[n]!=0){
            return fib[n];
        } else{
            fib[n] = fibTopDown(n-1, fib) + fibTopDown(n-2, fib);
            return fib[n];
        }
    }
}
