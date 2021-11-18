import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.concurrent.TimeUnit;
import java.awt.Insets;


public class JavaAWTGraphicsBoilerplate extends Frame implements WindowListener, ComponentListener{
    //  The target frame rate to try to get, this should get set again 
    //      in setup, and can be changed at practically any time.
    int targetFrameRate = -1;
    //  This is for the draw loop, once this set to false, the loop ends and cannot be restarted
    protected boolean runDraw = true;
    //  The unix time in ms that the program started
    private long startTime;
    //  Holds the inset of the window decorations
    Insets ins;
    
    // This will run once at the beginning of the program, and is in charge of handling 
    private void setup(){
        //  Set up the event listeners
        addWindowListener(this);
        addComponentListener(this);

        //  Sets the window title
        setTitle("AWT boilerplate");
        //  Sets the starting size in pixels. This includes some space that is not part of 
        //      the display area, and is covered by window decorations like the title bar.
        setSize(800, 800);
        //  Sets the window to be resizable
        setResizable(true);
        //  Makes the window visible
        setVisible(true);
        //  Gets the space consumed by the window decorations
        ins = getInsets();

        //  Use 2 buffer pages. If you experience flickering or 
        //      framerate drops, try changing the number of pages
        createBufferStrategy(2);

        //  Set the desired framerate, -1 for no limit
        targetFrameRate = -1;
    }
    
    //  This will be run once per frame, and try to run at targetFrameRate. 
    //      This is where your drawing code should live.
    private void draw(Graphics2D g){
        //  These translations move and scale the drawing area to only the visible space on the Graphics object
        //  If you want to remove these for pixel accuracy or overhead reasons, feel free, but bear in mind
        //      that all sides of the window are cut off. The ins variable contains the pixels amounts for 
        //      that cutoff, if you want to handle things yourself.
        g.translate(ins.left, ins.top);
        g.scale((double)(getWidth()-ins.left-ins.right)/getWidth(), (double)(getHeight()-ins.top-ins.bottom)/getHeight());
    }
    
    //Gets the time since that program started
    private long getRunningTime(){
        return System.currentTimeMillis() - startTime;
    }

    //  Handles running setup and looping draw at the specified frameRate
    JavaAWTGraphicsBoilerplate(){
        // Create the Frame
        super();
        //Store the start time
        startTime = System.currentTimeMillis();
        // In here you should do your startup stuff, some is already in there.
        setup();

        //  BufferStrategy is for double buffering and will automatically determine the
        //      most efficient method for double buffering that your system supports.
        BufferStrategy strategy = getBufferStrategy();
        long lastFrame, targetFrameTime;
        //  Only do the draw loop nonsense if the BufferStrategy exists
        //  TODO: Maybe add an alternative that doesn't do doubleBuffering?
        if(strategy != null){
            //  There doesn't appear to be a good option for automatically determining 
            //      if the Frame is still valid, I'm still looking into it though
            while(runDraw){
                //  Save the time at the start of the frame
                lastFrame = System.currentTimeMillis();
                //  Calculate how many ms a frame should last. This is computed 
                //      every frame so that if the desired frameRate gets changed 
                //      during runtime, it actually does things
                targetFrameTime = 1000/targetFrameRate;
                //  Get a drawing surface from the BufferStrategy
                Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
                //  Draw to it
                draw(g);
                //  We're done with the drawing surface now, toss it
                g.dispose();
                //  Swap the buffer we just drew to to the front
                strategy.show();
                // Wait for the next frame if there's space time
                try{
                    TimeUnit.MILLISECONDS.sleep(targetFrameTime - (System.currentTimeMillis() - lastFrame));
                } catch (InterruptedException e){}
            }
            // No more drawing? Toss the double buffer, we don't need it anymore
            strategy.dispose();
        }
        // This is the end of the program's lifecycle, if it seems to still be displayable, toss it.
        if(this.isDisplayable()){
            this.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        //  This type of thing took me ages to get accustomed to. I'm instantiating an instance of this 
        //      class from within a static function of the class.
        //  Because of the draw loop is not being multi-threaded, this will block until the program closes.
        //  Many of the things that would normally be done during the main function, should be done in 
        //      JavaGraphicsBoilerplate or setup.
        new JavaAWTGraphicsBoilerplate();
    }



    // These are event handlers
    @Override
    public void windowOpened(WindowEvent e) {
    }

    //  This one handles things like the window's x button being pressed, or alt-f4 on windows
    @Override
    public void windowClosing(WindowEvent e) {
        runDraw = false;
    }

    //  This one is triggered after the window finishes closing
    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
