package com.example.youngseok.myapplication.invite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import java.util.ArrayList;
import java.util.HashSet;

public class InviteLoader {



    public static ArrayList<InviteDTO> getData(Context context){


        ArrayList<InviteDTO> datas_sub = new ArrayList<InviteDTO>();
        ContentResolver resolver = context.getContentResolver();



        Uri PhoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String proj[] = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = resolver.query(PhoneUri,proj,null,null,null);



        if(cursor !=null){
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex(proj[0]);
                String name =cursor.getString(index);

                index = cursor.getColumnIndex(proj[1]);
                String tel =cursor.getString(index);
                tel.replaceAll("-","");

                final InviteDTO data = new InviteDTO();

                data.setPhonebook_name(name);
                data.setPhonebook_phone(tel.replaceAll("-","").replaceAll("\\+82","0")
                .replaceAll("//","").replaceAll(" ",""));





                datas_sub.add(data);


            }
        }
        cursor.close();

        HashSet <InviteDTO> hset = new HashSet<>(datas_sub);
        ArrayList<InviteDTO> datas = new ArrayList<InviteDTO>(hset);


        return datas;

    }

}
