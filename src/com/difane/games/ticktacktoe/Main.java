package com.difane.games.ticktacktoe;

import com.livescribe.penlet.Penlet;
import com.livescribe.display.Display;
import com.livescribe.ui.ScrollLabel;
import com.livescribe.configuration.Config;
import com.livescribe.event.StrokeListener;
import com.livescribe.penlet.Region;
import com.livescribe.event.HWRListener;
import com.livescribe.icr.ICRContext;
import com.livescribe.icr.Language;
import com.livescribe.icr.Resource;
import com.livescribe.event.MenuEventListener;
import com.livescribe.event.MenuEvent;
import com.livescribe.afp.PageInstance;
import com.livescribe.event.PenTipListener;
/**
 * This Penlet displays "Hello World!" as text when activated by menu.
 */
public class Main extends Penlet implements StrokeListener, HWRListener, MenuEventListener, PenTipListener {
    
    // Configuration key for example configuration reading 
    private static final String configKey = "CONFIG_DATA";
    private String configData;
    
    
    private Display display;
    private ScrollLabel label;    
    private ICRContext icrContext;

    public Main() {   
    }

    /**
     * Invoked when the application is initialized.  This happens once for an application instance.
     */
    public void initApp() {
        this.logger.info("Penlet Main initialized.");
        this.display = this.context.getDisplay();
        this.label = new ScrollLabel();
    }
    
    /**
     * Invoked each time the penlet is activated.  Only one penlet is active at any given time.
     */
    public void activateApp(int reason, Object[] args) {
        this.logger.info("Penlet Main activated.");
        if (reason == Penlet.ACTIVATED_BY_MENU) {
            this.label.draw(context.getResourceBundle().getTextResource("helloWorld.text").getText(), true);
            this.display.setCurrent(this.label);
        }
        this.context.addStrokeListener(this);

        // Prompt the user for text entry
        this.label.draw(this.context.getResourceBundle().getTextResource("icr.instruction.text"), true);
        this.display.setCurrent(this.label);
        
        // Configure the ICR context
        try {
            this.icrContext = this.context.getICRContext(1000, this);
            Resource[] resources = {
                this.icrContext.getDefaultAlphabetKnowledgeResource(),
                this.icrContext.createAppResource("/icr/LEX_smartpen-ticktacktoe.res"),
                this.icrContext.createAppResource("/icr/SK_smartpen-ticktacktoe.res")                                                                      
            };
            this.icrContext.addResourceSet(resources);            
        } catch (Exception e) {
            String msg = "Error initializing handwriting recognition resources: " + e.getMessage();
            this.logger.error(msg);
            this.label.draw(msg, true);
            this.display.setCurrent(this.label);
        }
		context.addPenTipListener(this);
    }
    
    /**
     * Invoked when the application is deactivated.
     */
    public void deactivateApp(int reason) {
        this.logger.info("Penlet Main deactivated.");
        this.context.removeStrokeListener(this);
        icrContext.dispose();
        icrContext = null;
		context.removePenTipListener(this);            
    }
    
    /**
     * Invoked when the application is destroyed.  This happens once for an application instance.  
     * No other methods will be invoked on the instance after destroyApp is called.
     */
    public void destroyApp() {
        this.logger.info("Penlet Main destroyed.");
    }


                 
    /**
     * Called when a new stroke is created on the pen. 
     * The stroke information is added to the ICRContext
     */
    public void strokeCreated(long time, Region regionId, PageInstance page) {
        this.icrContext.addStroke(page, time);
    }
    
    /**
     * When the user pauses (pause time specified by the wizard),
     * all strokes in the ICRContext are cleared
     */
    public void hwrUserPause(long time, String result) {
        this.icrContext.clearStrokes();
    }
    
    /**
     * When the ICR engine detects an acceptable series or strokes,
     * it prints the detected characters onto the Pulse display.
     */
    public void hwrResult(long time, String result) {
        this.label.draw(this.context.getResourceBundle().getTextResource("result.label") + " " + result);
    }
    
    /**
     * Called when an error occurs during handwriting recognition 
     */
    public void hwrError(long time, String error) {}
    
    /**
     * Called when the user crosses out text
     */
    public void hwrCrossingOut(long time, String result) {}
    
    /**
     * Specifies that the penlet should respond to events
     * related to open paper
     */
    public boolean canProcessOpenPaperEvents () {
        return true;
    }

	public boolean handleMenuEvent(MenuEvent menuEvent) {
		return true;
	}

	public void penUp(long time, Region region, PageInstance page) {
	
	}

	public void penDown(long time, Region region, PageInstance page) {
	
	}

	public void singleTap(long time, int x, int y) {
	
	}

	public void doubleTap(long time, int x, int y) {
	
	}
	
	private void exampleConfigurationReading() {
		// Obtain the label from the configuration file
		Config config = this.context.getAppConfiguration();
		try {
			this.configData = config.getStringValue(configKey);
			this.logger.info("Configuration value = " + this.configData);
			this.label.draw(configKey + " = " + this.configData, true);
		} catch (Exception e) {
			this.configData = "ERROR";
			this.logger.info("Failed to obtain value for " + configKey
					+ " in config.txt");
		}
	}
}
