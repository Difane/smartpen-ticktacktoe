package com.difane.games.ticktacktoe;

import java.util.Vector;

import com.livescribe.afp.PageInstance;
import com.livescribe.configuration.Config;
import com.livescribe.display.BrowseList;
import com.livescribe.display.Display;
import com.livescribe.display.Graphics;
import com.livescribe.display.Image;
import com.livescribe.event.HWRListener;
import com.livescribe.event.MenuEvent;
import com.livescribe.event.MenuEventListener;
import com.livescribe.event.PenTipListener;
import com.livescribe.icr.ICRContext;
import com.livescribe.icr.Resource;
import com.livescribe.penlet.Penlet;
import com.livescribe.penlet.Region;
import com.livescribe.ui.ScrollLabel;
/**
 * This Penlet displays "Hello World!" as text when activated by menu.
 */
public class BasePenlet extends Penlet implements MenuEventListener, PenTipListener {
    
	/**
	 * Display, which will show information to the user
	 */
	protected Display display;
	
	/**
	 * Main scroll label, used to display informational text to the user
	 */
	protected ScrollLabel label;
	
	
	protected FSM fsm;
	
	// Application menus
	protected Vector		menuMainItems;
	protected BrowseList	menuMain;
	protected Vector		menuHelpItems;
	protected BrowseList	menuHelp;
	protected Vector		menuLevelSelectItems;
	protected BrowseList	menuLevelSelect;
	
	// Variables, required for drawing on the screen
	Image image;
	Graphics graphics;
	
    // Configuration key for example configuration reading 
    private static final String configKey = "CONFIG_DATA";
    private String configData;

    public BasePenlet() {   
    }

    /**
     * Invoked when the application is initialized.  This happens once for an application instance.
     */
    public void initApp() {
        this.logger.info("[PENLET] Initializing penlet");
        
        // Initializing display elements
        this.display = this.context.getDisplay();
        this.label = new ScrollLabel();
        
        // Initializing FSM
        this.fsm = new FSM(this);
        
        // Initializing menus
        this.menuMainItems = new Vector();
		this.menuMainItems.addElement("Start game");
		this.menuMainItems.addElement("Help");
		this.menuMainItems.addElement("About");
		this.menuMain = new BrowseList(this.menuMainItems);
		
		this.menuHelpItems = new Vector();
		this.menuHelpItems.addElement("Rules");
		this.menuHelpItems.addElement("How to draw game board");
		this.menuHelpItems.addElement("How to play the game");
		this.menuHelp = new BrowseList(this.menuHelpItems);
		
		this.menuLevelSelectItems = new Vector();
		this.menuLevelSelectItems.addElement("Easy");
		this.menuLevelSelectItems.addElement("Hard");
		this.menuLevelSelect = new BrowseList(this.menuLevelSelectItems);
		
		// Initializing graphics
		this.image = Image.createImage(96, 18);
		this.graphics = Graphics.getGraphics(this.image);
		this.graphics.setBrushColor(Display.getWhiteColor());
		this.graphics.setLineStyle(Graphics.LINE_STYLE_SOLID);
		
		this.logger.info("[PENLET] Penlet was successfully initialized");
    }
    
    /**
     * Invoked each time the penlet is activated.  Only one penlet is active at any given time.
     */
    public void activateApp(int reason, Object[] args) {
        this.logger.info("[PENLET] Penlet Main activated (reason: "+reason+")");
        
        if (reason == Penlet.ACTIVATED_BY_MENU) {
        	this.context.addStrokeListener(this.fsm);
            this.fsm.eventStartApplication();
        }
        /*
        this.context.addStrokeListener(this);

        // Prompt the user for text entry
        this.label.draw(this.context.getResourceBundle().getTextResource("icr.instruction.text"), true);
        this.display.setCurrent(this.label);
        
        
		context.addPenTipListener(this);*/
    }
    
    /**
     * Invoked when the application is deactivated.
     */
    public void deactivateApp(int reason) {
        this.logger.info("[PENLET] Penlet Main deactivated.");
        //this.context.removeStrokeListener(this);        
		//context.removePenTipListener(this);
    }
    
    /**
     * Invoked when the application is destroyed.  This happens once for an application instance.  
     * No other methods will be invoked on the instance after destroyApp is called.
     */
    public void destroyApp() {
        this.logger.info("[PENLET] Penlet Main destroyed.");
    }

    /**
     * Specifies that the penlet should respond to events
     * related to open paper
     */
    public boolean canProcessOpenPaperEvents () {
        return true;
    }

	public boolean handleMenuEvent(MenuEvent menuEvent) {
		int eventId = menuEvent.getId();
		this.logger.info("[PENLET] Menu event received (id = "+eventId+")");
		
		boolean result = false;
		
		switch (eventId) {
		case MenuEvent.MENU_LEFT:
			result = this.fsm.eventMenuLeft();
			break;
		case MenuEvent.MENU_RIGHT:
			result = this.fsm.eventMenuRight();
			break;
		case MenuEvent.MENU_UP:
			result = this.fsm.eventMenuUp();
			break;
		case MenuEvent.MENU_DOWN:
			result = this.fsm.eventMenuDown();
			break;

		default:
			break;
		}
		
		return result;
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
