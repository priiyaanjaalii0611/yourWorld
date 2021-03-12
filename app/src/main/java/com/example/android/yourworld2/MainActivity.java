package com.example.android.yourworld2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AppLaunchChecker;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private String MODEL_URL="https://modelviewer.dev/shared-assets/models/Astronaut.glb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment= (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);  // initialize the fragment using id
        setUpModel();
        setUpPlane();
    }

    private void setUpPlane() {
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor= hitResult.createAnchor();                  //anchors from ARCORE we use this to attach our models to the real world
            AnchorNode anchorNode=new AnchorNode(anchor);

            anchorNode.setParent(arFragment.getArSceneView().getScene());  //set parent as scene that is our fragment for our anchornode
            createModel(anchorNode); // set anchor node as the parent for our model 
        }));
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode node= new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode); // ye hoga iska parent node jiske around ye bithaayega apne aap ko
        node.setRenderable(modelRenderable); // this node is the model that we v downloaded
        node.select();
    }

    private void setUpModel() {
        ModelRenderable.builder().setSource(this,RenderableSource.builder().setSource(
                this,Uri.parse(MODEL_URL),RenderableSource.SourceType.GLB) // parsing the link the source for our renderable
                .setScale(0.75f)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build())
                .setRegistryId(MODEL_URL)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(MainActivity.this,"Can't load the model",Toast.LENGTH_SHORT).show();
                    return  null;
                });

    }
}