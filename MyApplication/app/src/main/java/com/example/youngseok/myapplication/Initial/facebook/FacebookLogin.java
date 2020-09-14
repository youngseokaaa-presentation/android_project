package com.example.youngseok.myapplication.Initial.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.youngseok.myapplication.Initial.signupActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookLogin {

    private Context context;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    public FacebookLogin(Context context) {
        this.context = context;
        callbackManager = CallbackManager.Factory.create();
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setLoginButton(LoginButton loginButton) {
        this.loginButton = loginButton;
        loginButton.setReadPermissions("public_profile","email");
        loginButton.registerCallback(callbackManager, new CallBack());
    }
    private class CallBack implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Intent intent = new Intent(context, signupActivity.class);
                            try {
                                intent.putExtra("name", object.getString("name"));
                            } catch (JSONException e) {
                                intent.putExtra("name", "nothing");
                            }
                            try {
                                intent.putExtra("email", object.getString("email"));
                            } catch (JSONException e) {
                                intent.putExtra("email", "nothing");
                            }
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, name, email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {
            error.printStackTrace();
        }
    }
}
