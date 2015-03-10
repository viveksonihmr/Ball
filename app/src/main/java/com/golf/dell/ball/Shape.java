package com.golf.dell.ball;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;




public class Shape {
    private FloatBuffer m_VertexData;
    private FloatBuffer m_NormalData;
    private FloatBuffer m_ColorData;


    float m_Scale;
    float m_Squash;
    float m_Radius;
    int m_Stacks, m_Slices;


    public Shape(int stacks, int slices, float radius, float squash) { //Ball
        this.m_Stacks = stacks;
        this.m_Slices = slices;
        this.m_Radius = radius;
        this.m_Squash = squash;
        init(m_Stacks, m_Slices, radius, squash, "dummy");
    }

    private void init(int stacks, int slices, float radius, float squash, String textureFile) {
        float[] vertexData;
        float[] colorData;
        float[] normalData;
        float colorIncrement = 0f;
        float blue = 0f;
        float red = 1.0f;
        int numVertices = 0;
        int vIndex = 0; //vertex index
        int cIndex = 0; //color index
        int nIndex = 0;
        m_Scale = radius;
        m_Squash = squash;
        colorIncrement = 1.0f / (float) stacks;
        {
            m_Stacks = stacks;
            m_Slices = slices;

            vertexData = new float[3 * ((m_Slices * 2 + 2) * m_Stacks)];

            colorData = new float[(4 * (m_Slices * 2 + 2) * m_Stacks)];
            normalData = new float[(3 * (m_Slices * 2 + 2) * m_Stacks)];
            int phiIdx, thetaIdx;

            for (phiIdx = 0; phiIdx < m_Stacks; phiIdx++) //6
            {

                float phi0 = (float) Math.PI * ((float) (phiIdx + 0) *
                        (1.0f / (float) (m_Stacks)) - 0.5f);

                float phi1 = (float) Math.PI * ((float) (phiIdx + 1) *
                        (1.0f / (float) (m_Stacks)) - 0.5f);
                float cosPhi0 = (float) Math.cos(phi0);
                float sinPhi0 = (float) Math.sin(phi0);
                float cosPhi1 = (float) Math.cos(phi1);
                float sinPhi1 = (float) Math.sin(phi1);
                float cosTheta, sinTheta;

                for (thetaIdx = 0; thetaIdx < m_Slices; thetaIdx++) {

                    float theta = (float) (-2.0f * (float) Math.PI * ((float) thetaIdx) *
                            (1.0 / (float) (m_Slices - 1)));
                    cosTheta = (float) Math.cos(theta);
                    sinTheta = (float) Math.sin(theta);
                    vertexData[vIndex + 0] = m_Scale * cosPhi0 * cosTheta;
                    vertexData[vIndex + 1] = m_Scale * (sinPhi0 * m_Squash);
                    vertexData[vIndex + 2] = m_Scale * (cosPhi0 * sinTheta);
                    vertexData[vIndex + 3] = m_Scale * cosPhi1 * cosTheta;
                    vertexData[vIndex + 4] = m_Scale * (sinPhi1 * m_Squash);
                    vertexData[vIndex + 5] = m_Scale * (cosPhi1 * sinTheta);
                    colorData[cIndex + 0] = (float) red;
                    colorData[cIndex + 1] = (float) 0f;
                    colorData[cIndex + 2] = (float) blue;
                    colorData[cIndex + 4] = (float) red;
                    colorData[cIndex + 5] = (float) 0f;
                    colorData[cIndex + 6] = (float) blue;
                    colorData[cIndex + 3] = (float) 1.0;
                    colorData[cIndex + 7] = (float) 1.0;


                    normalData[nIndex + 0] = cosPhi0 * cosTheta;
                    normalData[nIndex + 1] = sinPhi0;
                    normalData[nIndex + 2] = cosPhi0 * sinTheta;
                    normalData[nIndex + 3] = cosPhi1 * cosTheta;
                    normalData[nIndex + 4] = sinPhi1;
                    normalData[nIndex + 5] = cosPhi1 * sinTheta;

                    cIndex += 2 * 4;
                    vIndex += 2 * 3;
                }

                red -= colorIncrement;

                vertexData[vIndex + 0] = vertexData[vIndex + 3] = vertexData[vIndex - 3];
                vertexData[vIndex + 1] = vertexData[vIndex + 4] = vertexData[vIndex - 2];
                vertexData[vIndex + 2] = vertexData[vIndex + 5] = vertexData[vIndex - 1];
            }
        }

        m_VertexData= makeFloatBuffer(vertexData);

        m_ColorData= makeFloatBuffer(colorData);

        m_NormalData=makeFloatBuffer(normalData);

    }
    private static FloatBuffer makeFloatBuffer(float[] vertexData) {
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexData.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vertexData);
        fb.position(0);
        return fb;
    }



    public void draw (GL10 gl)
    {
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glNormalPointer(GL10.GL_FLAT,0,m_NormalData);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);



        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_VertexData);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColorPointer(4,GL10.GL_FLOAT,0,m_ColorData);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,(m_Slices)*2*(m_Stacks-1)+2);

    }

}


