package com.example.youngseok.myapplication.Initial.kakao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.youngseok.myapplication.Initial.signupActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

public class SessionCallback implements ISessionCallback {
    private Context context;


    public SessionCallback(Context context) {

        this.context = context;
    }


    @Override
    public void onSessionOpened() {
        requestMe();
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
    }

    private void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Intent intent = new Intent(context, signupActivity.class);
                try {
                    intent.putExtra("name", result.getNickname());
                } catch (Exception e) {
                    intent.putExtra("name", "nothing");
                }
                try {
                    intent.putExtra("email", result.getKakaoAccount().getEmail());
                } catch (Exception e) {
                    intent.putExtra("name", "nothing");
                }
                context.startActivity(intent);
                ((Activity)context).finish();
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
            }
        });
    }
}