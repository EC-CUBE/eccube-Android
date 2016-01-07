/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ec_cube.eccube_Android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.appiaries.baas.sdk.AB;
import com.appiaries.baas.sdk.ABDevice;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * PUSH通知のために端末の登録を行うためのFragmentです。<br/>
 * <p/>
 * このプログラムはキャンセル処理、エラー処理の省略やログ表示を行っています<br/>
 * 業務等で使用する際には適宜カスタマイズしてご使用ください。<br/>
 * 実装は以下の内容に従っていますので、合わせてご参照ください。<br/>
 * {@see <a href="https://code.google.com/p/gcm/source/browse/gcm-client/GcmClient/src/main/java/com/google/android/gcm/demo/app/DemoActivity.java">URL</a>}
 */
public class PushRegistrationFragment extends Fragment {

    /**
     * Google Play servicesの復帰用のリクエストID
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * 登録成功を表す定数値
     */
    private static final int REGISTRATION_SUCCESS = 0;

    /**
     * 登録失敗を表す定数値
     */
    private static final int REGISTRATION_FAILURE = -1;

    /**
     * ログ用のタグ
     */
    private static final String TAG = "AppiariesReg";

    /**
     * {@link GoogleCloudMessaging}
     */
    private GoogleCloudMessaging mGcm;

    /**
     * Registration ID
     */
    private String mRegId;

    /**
     * ApplicationContext
     */
    private Context mApplicationContext;

    /**
     * PushRegistrationListener
     */
    private PushRegistrationListener mListener;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PushRegistrationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implemens " + PushRegistrationListener.class.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getActivity();
        mApplicationContext = context.getApplicationContext();

        // Google Play servicesが有効かチェック
        if (checkPlayServices()) {
            mGcm = GoogleCloudMessaging.getInstance(context);
            mRegId = getRegistrationId(mApplicationContext);

            if (TextUtils.isEmpty(mRegId)) {
                registerInBackground();
            } else {
                Log.i(TAG, "regId is " + mRegId);
                mListener.onSuccess();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
            mListener.onFailure();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Google Play servicesの状態をチェックします。
     *
     * @return 有効な場合はtrue.
     */
    private boolean checkPlayServices() {
        final Activity activity = getActivity();
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registration IDを保存します。
     *
     * @param context {@link Context}
     * @param regId   Registration ID
     */
    private static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Config.PROPERTY_REG_ID, regId);
        editor.putInt(Config.PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * Registration IDを取得します。
     *
     * @param context {@link Context}
     */
    private static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        final String registrationId = prefs.getString(Config.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(Config.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * バックグラウンドで登録します。
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int result;
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(mApplicationContext);
                    }
                    mRegId = mGcm.register(Config.SENDER_ID);
                    // AppiariesサーバにRegistration IDを登録
                    sendRegistrationIdToBackend();
                    // Registration IDを登録
                    storeRegistrationId(mApplicationContext, mRegId);
                    Log.i(TAG, "Device registered, registration ID=" + mRegId);
                    result = REGISTRATION_SUCCESS;
                } catch (IOException ex) {
                    Log.i(TAG, "Error :" + ex.getMessage());
                    result = REGISTRATION_FAILURE;
                } catch (Exception ex) {
                    Log.i(TAG, "Error :" + ex.getMessage());
                    result = REGISTRATION_FAILURE;
                }
                return result;
            }

            /**
             * 処理終了後にメッセージを表示します。
             */
            @Override
            protected void onPostExecute(Integer result) {
                Log.i(TAG, "result->" + result);
                switch (result) {
                    case REGISTRATION_SUCCESS:
                        mListener.onSuccess();
                        break;
                    case REGISTRATION_FAILURE:
                        mListener.onFailure();
                        break;
                }
            }
        }.execute();
    }

    /**
     * アプリバージョンを取得します。
     *
     * @param context {@link Context}
     * @return アプリのバージョン
     */
    private static int getAppVersion(Context context) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * GCMのプリファレンスを取得します。
     *
     * @param context {@link Context}
     * @return {@link Preference}
     */
    private static SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(Config.GCM_PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * ID登録をバックグラウンドで行います。
     *
     * @throws Exception
     */
    private void sendRegistrationIdToBackend() throws Exception {
        // 今回は特別にアピアリーズには情報を送らない（Web側でやる）
        // データストアID、アプリID、アプリケーショントークン、Contextを指定
        /*AB.Config.setDatastoreID(Config.DATASTORE_ID);
        AB.Config.setApplicationID(Config.APPLICATION_ID);
        AB.Config.setApplicationToken(Config.APPLICATION_TOKEN);
        AB.activate(mApplicationContext);

        // Registration IDの登録
        ABDevice device = new ABDevice();
        device.setRegistrationID(mRegId);
        device.register();*/
    }

}
