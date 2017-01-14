package main;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.math.VectorUtil;

public class Render implements GLEventListener {
	
	private int programId = -1;
	public final float[] Up = {0.0f,1.0f,0.0f};
	public float[] eyeLocation = {0.0f,0.0f,1.0f};
	public float[] gazeVector = {0.0f,0.0f,0.0f};
	
	private String[] vertexSrc = {
			"#version 330\n",
			"layout (location = 0) in vec3 position;\n",
			"uniform mat4 worldSpace;\n",
			"uniform mat4 cameraSpace;\n",
			"uniform mat4 clipSpace;\n",
			"void main(){\n",
			"gl_Position = clipSpace*cameraSpace*worldSpace * vec4(position, 1.0);\n",
			"}\n"
	};
	private String[] fragmentSrc = {
			"#version 330\n",
			"out vec4 color;\n",
			"void main(){\n",
			"color = vec4(0.0f,1.0f,0.0f,1.0f);\n",
			"}\n"
	};
	

	public Render() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glClearColor(0.0f, 1.0f, 0.77f, 1.0f);
		
		int vertexShader = gl.glCreateShader(gl.GL_VERTEX_SHADER);
		gl.glShaderSource(vertexShader, vertexSrc.length, vertexSrc, null);
		gl.glCompileShader(vertexShader);
		
		int fragmentShader = gl.glCreateShader(gl.GL_FRAGMENT_SHADER);
		gl.glShaderSource(fragmentShader, fragmentSrc.length, fragmentSrc, null);
		gl.glCompileShader(fragmentShader);
		
		IntBuffer success = IntBuffer.wrap(new int[1]);
		ByteBuffer log = ByteBuffer.wrap(new byte[512]);
		
		gl.glGetShaderiv(vertexShader, gl.GL_COMPILE_STATUS, success);
		
		if(success.get() == 0 ){
			gl.glGetShaderInfoLog(vertexShader, 512, (IntBuffer)null, log);
			String output = new String(log.array(),Charset.forName("UTF-8"));
			System.out.println(output);
		}
		else{
			System.out.println("vertexShader wiff");
		}
		
		success.rewind(); log.rewind();
		
		gl.glGetShaderiv(fragmentShader, gl.GL_COMPILE_STATUS, success);
		
		if(success.get() == 0 ){
			gl.glGetShaderInfoLog(fragmentShader, 512, (IntBuffer)null, log);
			String output = new String(log.array(),Charset.forName("UTF-8"));
			System.out.println(output);
		}
		else{
			System.out.println("fragmentShader wiff");
		}
		
		success.rewind(); log.rewind();
		
		 programId = gl.glCreateProgram();
		 
		 gl.glAttachShader(programId, vertexShader);
		 gl.glAttachShader(programId, fragmentShader);
		 
		 gl.glLinkProgram(programId);
		 
		 gl.glGetProgramiv(programId, gl.GL_LINK_STATUS, success);
		 
		 if(success.get(0) == 0 ){
			 gl.glGetProgramInfoLog(programId, 512, (IntBuffer)null, log);
			 String output = new String(log.array(),Charset.forName("UTF-8"));
			 System.out.println(output);
		 }
		 else{
			 System.out.println("Program Wiff");
		 }
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT );
		
		
		
		
		float[] vertices = {
				0.5f,0.0f,0.0f,
				0.5f,0.5f,0.0f,
				-0.5f,0.0f,0.0f,
				-0.5f,0.5f,0.0f,
				
				0.5f,0.0f,1.0f,
				0.5f,0.5f,1.0f,
				-0.5f,0.0f,1.0f,
				-0.5f,0.5f,1.0f,
				
		};
		
		int[] vertexOrder = {0,1,2, 3,2,1};
		int[] VAO = new int[1];
		int[] VBO = new int[1];
		int[] EBO = new int[1];
		
		gl.glGenVertexArrays(1,VAO, 0);
		gl.glGenBuffers(1, VBO,0);
		gl.glGenBuffers(1, EBO,0);
		
		gl.glBindVertexArray(VAO[0]);
		
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
		gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, vertexOrder.length*4, IntBuffer.wrap(vertexOrder),gl.GL_STATIC_DRAW);
		
		
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VBO[0]);
		gl.glBufferData(gl.GL_ARRAY_BUFFER, vertices.length*4, FloatBuffer.wrap(vertices), gl.GL_STATIC_DRAW);
		
		gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
		
		gl.glUseProgram(programId);
		
		float[] m = new float[16];
		int location = gl.glGetUniformLocation(programId, "worldSpace");
		FloatUtil.makeIdentity(m);
		gl.glUniformMatrix4fv(location, 1, false, FloatBuffer.wrap(m));
		
		float[] camera = new float[16]; 
		int camLoc = gl.glGetUniformLocation(programId, "cameraSpace");
		float[] lookPoint = new float[3];
		
		VectorUtil.addVec3(lookPoint, eyeLocation, gazeVector);
		
		FloatUtil.makeLookAt(camera, 0, eyeLocation, 0, lookPoint,0, Up, 0, new float[16]);
		gl.glUniformMatrix4fv(camLoc, 1, false, FloatBuffer.wrap(camera));
		
		float[] clipper = new float[16]; 
		int clipLoc = gl.glGetUniformLocation(programId, "clipSpace");
		FloatUtil.makePerspective(clipper, 0, true, 45.0f, 1200/900, 0.01f, 100.0f);
		gl.glUniformMatrix4fv(clipLoc, 1, false, FloatBuffer.wrap(clipper));
		
		gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_INT, 0);
		
		
		
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

}
