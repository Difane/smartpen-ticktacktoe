package com.difane.games.ticktacktoe;

import com.livescribe.event.MenuEvent;
import com.livescribe.event.MenuEventListener;
import com.livescribe.penlet.Penlet;
/**
 * This Penlet displays "Hello World!" as text when activated by menu.
 */
public class BasePenlet extends Penlet implements MenuEventListener {
	/**
	 * Container
	 */
	private Container container;
	
	protected GameFSM fsm;
	
    public BasePenlet() {
    	this.container = new Container();
    	this.container.setComponent("penlet", this);
    	this.container.setComponent("logger", this.logger);
    }

    /**
     * Invoked when the application is initialized.  This happens once for an application instance.
     */
    public void initApp() {
        this.getContainer().getLoggerComponent().info("[Penlet] Initializing penlet");
        
        // Initializing GameFSM
        this.fsm = this.getContainer().getGameFSMComponent();
		
        this.getContainer().getLoggerComponent().info("[Penlet] Penlet was successfully initialized");
    }
    
    /**
     * Invoked each time the penlet is activated.  Only one penlet is active at any given time.
     */
    public void activateApp(int reason, Object[] args) {
    	this.getContainer().getLoggerComponent().info("[Penlet] Penlet Main activated (reason: "+reason+")");
        
        if (reason == Penlet.ACTIVATED_BY_MENU) {
        	this.context.addStrokeListener(this.fsm);
            this.fsm.eventStartApplication();
        }
    }
    
    /**
     * Invoked when the application is deactivated.
     */
    public void deactivateApp(int reason) {
    	this.getContainer().getLoggerComponent().info("[Penlet] Penlet Main deactivated.");
        this.context.removeStrokeListener(this.fsm);        
    }
    
    /**
     * Invoked when the application is destroyed.  This happens once for an application instance.  
     * No other methods will be invoked on the instance after destroyApp is called.
     */
    public void destroyApp() {
    	this.getContainer().getLoggerComponent().info("[Penlet] Penlet Main destroyed.");
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
		this.getContainer().getLoggerComponent().info("[Penlet] Menu event received (id = "+eventId+")");
		
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
	
	/**
	 * Returns container
	 * @return container
	 */
	public Container getContainer() {
		return container;
	}
}
