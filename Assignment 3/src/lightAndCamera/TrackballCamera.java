package lightAndCamera;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;


public class TrackballCamera implements MouseListener, MouseMotionListener, MouseWheelListener {

    // some hard limitations to camera values
    private static final double MIN_DISTANCE = 1;
    private static final double MIN_FOV = 1;
    private static final double MAX_FOV = 80;
    
    // the point to look at
    private double lookAt[] = {0, 0, 0};

    // the camera rotation angles
    private double angleX = 45;
    private double angleY = 25;

    // old mouse position for dragging
    private Point oldMousePos;
    private int mouseButton;

    // camera parameters
    double fieldOfView = 45;
    double distanceToOrigin = 150;
    double windowWidth = 1;
    double windowHeight = 1;

    // GLU context
    GLU glu = new GLU();
    

    /**
     * Constructor of the trackball camera
     * @param drawable the GL drawable context to register this camera with
     */
    public TrackballCamera(GLCanvas canvas) {
    	canvas.addMouseListener(this);
    	canvas.addMouseWheelListener(this);
    	canvas.addMouseMotionListener(this);
    	
    	this.setDistance(1.5);
    	this.setFieldOfView(70);
    	this.setLookAt(0, 0, 0);
    }

    /**
     * "Draws" the camera.
     * This sets up the projection matrix and
     * the camera position and orientation.
     * This method has to be called first thing
     * in the <code>display()</code> method
     * of the main program
     *
     * @param gl then OpenGL context to draw the camera in
     */
    public void draw(GL2 gl, boolean perspective) {
        // set up projection first
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        // setting up perspective projection
        // far distance is hardcoded to 3*cameraDistance. If your scene is bigger,
        // you might need to adapt this
        if(perspective) {
        	glu.gluPerspective(fieldOfView, windowWidth / windowHeight, 0.1, distanceToOrigin * 3);
        }else {
            gl.glOrtho(-1, 1, -1, 1, (-10)*distanceToOrigin, 10*distanceToOrigin);
        }

        // then set up the camera position and orientation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        double r = distanceToOrigin * Math.cos(Math.toRadians(angleY));
        double camZ = r * Math.cos(Math.toRadians(angleX));
        double camX = r * Math.sin(Math.toRadians(angleX));
        double camY = distanceToOrigin * Math.sin(Math.toRadians(angleY));
        glu.gluLookAt(
            camX, camY, camZ,                // eye
            lookAt[0], lookAt[1], lookAt[2], // center
            0, 1, 0);                        // up
    }

    //Close
    public void close() {
    	this.glu = null;
    	this.lookAt = null;
		System.out.println("TrackballCamera closed....");
    }
    
    /**
     * Gets the distance of the camera from the lookAt point
     * @return the distance of the camera from the lookAt point
     */
    public double getDistance() {
        return distanceToOrigin;
    }

    /**
     * Sets the distance of the camera to the lookAt point.
     * @param dist the new distance of the camera to the lookAt point
     */
    public void setDistance(double dist) {
        distanceToOrigin = dist;
        limitDistance();
    }

    /**
     * Limits the distance of the camera to valid values.
     */
    private void limitDistance() {
        if (distanceToOrigin < MIN_DISTANCE) {
            distanceToOrigin = MIN_DISTANCE;
        }
    }

    /**
     * Gets the field of view angle of the camera
     * @return the field of view of the camera in degrees
     */
    public double getFieldOfView() {
        return fieldOfView;
    }

    /**
     * Sets the field of view angle of the camera.
     * @param fov the new field of view angle of the camera in degrees
     */
    public void setFieldOfView(double fov) {
        fieldOfView = fov;
        limitFieldOfView();
    }

    /**
     * Limits the field of view angle to a valid range.
     */
    private void limitFieldOfView() {
        if (fieldOfView < MIN_FOV) {
            fieldOfView = MIN_FOV;
        }
        if (fieldOfView > MAX_FOV) {
            fieldOfView = MAX_FOV;
        }
    }

    /**
     * Sets up the lookAt point
     * @param x X coordinate of the lookAt point
     * @param y Y coordinate of the lookAt point
     * @param z Z coordinate of the lookAt point
     */
    public void setLookAt(double x, double y, double z) {
        lookAt = new double[]{x, y, z};
    }

    /**
     * Resets the camera rotations.
     */
    public void reset() {
        angleX = angleY = 0;
        oldMousePos = null;
    }

    /**
     * Passes a new window size to the camera.
     * This method should be called from the <code>reshape()</code> method
     * of the main program.
     *
     * @param width the new window width in pixels
     * @param height the new window height in pixels
     */
    public void newWindowSize(int width, int height) {
        windowWidth = Math.max(1.0, width);
        windowHeight = Math.max(1.0, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        oldMousePos = e.getPoint();
        mouseButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        oldMousePos = null;
        mouseButton = -0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        if (oldMousePos != null) {
            // dragging with left mouse button: rotate
            if (mouseButton == MouseEvent.BUTTON1) {
                angleX -= p.x - oldMousePos.x;
                angleY += p.y - oldMousePos.y;
                // limit Y rotation angle to avoid gimbal lock
                angleY = Math.min(89.9, Math.max(-89.9, angleY));
            } // dragging with right mouse button: change distance
            else if (mouseButton == MouseEvent.BUTTON3) {
                distanceToOrigin += 0.1 * (p.y - oldMousePos.y);
                limitDistance();
            }

        }
        oldMousePos = p;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        // zoom using the FoV
        while (clicks > 0) {
            fieldOfView *= 1.1;
            clicks--;
        }
        while (clicks < 0) {
            fieldOfView /= 1.1;
            clicks++;
        }
        limitFieldOfView();
    }

  
}
