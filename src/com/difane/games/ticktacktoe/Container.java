package com.difane.games.ticktacktoe;

import java.util.Hashtable;
import java.util.NoSuchElementException;

import com.livescribe.penlet.Logger;

public class Container {

	/*
	 * Available components: GameBoard GameLogic GameFSM GameDisplay Logger
	 */

	/**
	 * Parameters
	 */
	Hashtable parameters = new Hashtable();
	
	/**
	 * Components
	 */
	Hashtable components = new Hashtable();

	/**
	 * Sets new parameter for given key
	 * 
	 * @param key
	 *            Key to set parameter for
	 * @param value
	 *            Value of the parameter
	 * @return Previous parameter value or nul if not set before
	 */
	public Object setParameter(String key, Object value) {
		return parameters.put(key, value);
	}

	/**
	 * Returns parameters value for given key
	 * 
	 * @param key
	 *            Key to get parameter for
	 * @return Parameter value or null if no parameter with given name exist
	 */
	public Object getParameter(String key) {
		return parameters.get(key);
	}

	public Object getComponent(String name) {
		return null;
	}

	public GameBoard getGameBoardComponent() {
		if(false == components.containsKey("gameboard"))
		{
			components.put("gameboard", new GameBoard(this));
		}
		
		return (GameBoard)components.get("gameboard");
	}

	public GameLogic getGameLogicComponent() {
		if(false == components.containsKey("gamelogic"))
		{
			components.put("gamelogic", new GameLogic(this));
		}
		
		return (GameLogic)components.get("gamelogic");
	}

	public GameFSM getGameFSMComponent() {
		if(false == components.containsKey("gamefsm"))
		{
			components.put("gamefsm", new GameFSM(this));
		}
		
		return (GameFSM)components.get("gamefsm");
	}

	public GameDisplay getGameDisplayComponent() {
		if(false == components.containsKey("gamedisplay"))
		{
			components.put("gamedisplay", new GameDisplay(this));
		}
		
		return (GameDisplay)components.get("gamedisplay");
	}

	public Logger getLoggerComponent() {
		if(false == components.containsKey("logger"))
		{
			throw new NoSuchElementException();
		}
		
		return (Logger)components.get("logger");
	}
	
	public BasePenlet getPenletComponent()
	{
		if(false == components.containsKey("penlet"))
		{
			throw new NoSuchElementException();
		}
		
		return (BasePenlet)components.get("penlet");
	}
	
	public Object setComponent(String key, Object value)
	{
		return components.put(key, value);
	}

}
