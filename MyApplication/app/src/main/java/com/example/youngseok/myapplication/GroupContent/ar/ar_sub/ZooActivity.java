package com.example.youngseok.myapplication.GroupContent.ar.ar_sub;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youngseok.myapplication.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ZooActivity extends AppCompatActivity implements View.OnClickListener {
    ArFragment arFragment;
    private ModelRenderable bearRenderable,
    catRenderable,cowRenderable,dogRenderable,elephantRenderable,
    ferretRenderable,hippopotamusRenderable,horseRenderable,koala_bearRenderable,lionRenderable;

    ImageView bear,cat,cow,dog,elephant,ferret,hippo,horse,koala,lion;
    View arrayView[];
    ViewRenderable name_animal;

    int selected =1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoo);

        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        bear = findViewById(R.id.bear);
        cat = findViewById(R.id.cat);
        cow=findViewById(R.id.cow);
        dog=findViewById(R.id.dog);
        elephant=findViewById(R.id.elephant);
        ferret=findViewById(R.id.ferret);
        hippo=findViewById(R.id.hippopotamus);
        horse=findViewById(R.id.horse);
        koala=findViewById(R.id.koala_bear);
        lion=findViewById(R.id.lion);
        
        setArrayView();
        setclickListener();
        
        setUpModel();
        
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    createModel(anchorNode,selected);

            }
        });
    }

    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this,R.raw.bear)
                .build().thenAccept(renderable -> bearRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.cat)
                .build().thenAccept(renderable -> catRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.cow)
                .build().thenAccept(renderable -> cowRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.dog)
                .build().thenAccept(renderable -> dogRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.elephant)
                .build().thenAccept(renderable -> elephantRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.ferret)
                .build().thenAccept(renderable -> ferretRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.hippopotamus)
                .build().thenAccept(renderable -> hippopotamusRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.horse)
                .build().thenAccept(renderable -> horseRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.koala_bear)
                .build().thenAccept(renderable -> koala_bearRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this,R.raw.lion)
                .build().thenAccept(renderable -> lionRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"가져오기 실패 ㅠ",Toast.LENGTH_SHORT).show();
                            return null;
                        });
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if(selected==1){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(bearRenderable);
            bear.select();
        }
        if(selected==2){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(catRenderable);
            bear.select();
        }
        if(selected==3){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(cowRenderable);
            bear.select();
        }
        if(selected==4){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(dogRenderable);
            bear.select();
        }
        if(selected==5){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(elephantRenderable);
            bear.select();
        }
        if(selected==6){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(ferretRenderable);
            bear.select();
        }
        if(selected==7){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(hippopotamusRenderable);
            bear.select();
        }
        if(selected==8){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(horseRenderable);
            bear.select();
        }
        if(selected==9){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(koala_bearRenderable);
            bear.select();
        }
        if(selected==10){
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(lionRenderable);
            bear.select();
        }
    }

    private void setclickListener() {
        for(int i=0;i<arrayView.length;i++)
            arrayView[i].setOnClickListener(this);
    }

    private void setArrayView() {

        arrayView = new View[]{
                bear,cat,cow,dog,elephant,ferret,hippo,horse,koala,lion
        };
    }

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.bear){
            selected=1;
            setBackGround(view.getId());
        }

        else if(view.getId()==R.id.cat){
            selected=2;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.cow){
            selected=3;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.dog){
            selected=4;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.elephant){
            selected=5;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.ferret){
            selected=6;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.hippopotamus){
            selected=7;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.horse){
            selected=8;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.koala_bear){
            selected=9;
            setBackGround(view.getId());
        }
        else if(view.getId()==R.id.lion){
            selected=10;
            setBackGround(view.getId());
        }

    }

    private void setBackGround(int id) {
        for(int i=0; i<arrayView.length;i++){
            if(arrayView[i].getId()==id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
        }
    }


}

























