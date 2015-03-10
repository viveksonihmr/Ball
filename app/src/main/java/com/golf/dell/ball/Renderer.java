package com.golf.dell.ball;

import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class Renderer implements GLSurfaceView.Renderer {

    private Shape mShape = new Shape(50,50,1.0f,1.0f);
    private float mTransY;
    private float mAngle;

    public final static int SS_SUNLIGHT = GL10.GL_LIGHT0;

    public void initLighting(GL10 gl)
    {
        float[] diffuse = {0.0f, 1.0f, 0.0f, 1.0f};
        float[] pos = {50.0f, 0.0f, 3.0f, 1.0f};

        float[] white = {1.0f, 1.0f, 1.0f, 1.0f};

        gl.glLightfv(SS_SUNLIGHT, GL10.GL_POSITION, FloatBuffer.wrap(pos));

        gl.glLightfv(SS_SUNLIGHT, GL10.GL_DIFFUSE, FloatBuffer.wrap(white));

        gl.glLightfv(SS_SUNLIGHT,GL10.GL_SPECULAR, FloatBuffer.wrap(white));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_SPECULAR,FloatBuffer.wrap(white));

        gl.glLightfv(SS_SUNLIGHT,GL10.GL_AMBIENT, FloatBuffer.wrap(white));
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_AMBIENT,FloatBuffer.wrap(white));


        float[] yellow={0.5f, 0.5f, 0.0f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,GL10.GL_EMISSION, FloatBuffer.wrap(white));

        gl.glEnable(GL10.GL_COLOR_MATERIAL);

        gl.glLightf(SS_SUNLIGHT, GL10.GL_LINEAR_ATTENUATION, 0.25f);


        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, FloatBuffer.wrap(yellow));
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(SS_SUNLIGHT);
        gl.glLoadIdentity();
    }



    private void initGeometry(GL10 gl) {
    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDepthMask(false);
        initGeometry(gl);
        initLighting(gl);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        float aspectRatio;
        float zNear =0.1f;
        float zFar =1000;
        float fov =50.0f/57.3f;
        float size;

        gl.glEnable(GL10.GL_NORMALIZE);
        aspectRatio = (float)width/(float)height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        size = zNear * (float)(Math.tan((double)(fov/2.0)));
        gl.glFrustumf(-size,size,-size/aspectRatio,size/aspectRatio,zNear,zFar);
        gl.glMatrixMode(GL10.GL_MODELVIEW);

    }


    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f,(float)Math.sin(mTransY),-4.0f);

        gl.glRotatef(mAngle, 1, 0, 0);
        gl.glRotatef(mAngle, 0, 1, 0);

        mShape.draw(gl);
        mTransY +=0;
        mAngle +=7.0;
    }
}
