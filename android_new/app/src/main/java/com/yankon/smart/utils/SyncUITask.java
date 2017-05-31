package com.yankon.smart.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.yankon.smart.App;
import com.yankon.smart.fragments.ProgressDialogFragment;
import com.yankon.smart.providers.YanKonProvider;

/**
 * Created by Evan on 15/3/19.
 */
public class SyncUITask extends AsyncTask<Void, Void, Void> {
    ProgressDialogFragment progressDialogFragment;
    FragmentManager mFragmentManager;

    public SyncUITask(FragmentManager fm, String msg) {
        super();
        mFragmentManager = fm;
        progressDialogFragment = ProgressDialogFragment.newInstance(null, msg);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (KiiSync.isSyncing()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        KiiSync.sync(true);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialogFragment.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogFragment.show(mFragmentManager, "dialog");
    }

    public boolean checkIfSyncSucc() {
        boolean result = true;
        Context context = App.getApp();
        Cursor cursor = context.getContentResolver().query(YanKonProvider.URI_SCHEDULE, null,
                "synced=0", null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                result = false;
            }
            cursor.close();
        }
        return result;
    }
}
