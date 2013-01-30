/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.GenerationStackRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class BotGeneratorEventDispatcher implements Listener {


	public static interface CodeGenerationListener {
		public void handleCodeGenerated(String code);
	}

	private List<GenerationSimpleRule> listOfSimpleRules = new ArrayList<GenerationSimpleRule>();
	private List<GenerationRule> listOfRules = new ArrayList<GenerationRule>();
	private Set<GenerationStackRule> setOfUsedStackRules = new HashSet<GenerationStackRule>();
	
	private boolean useStacks;
	private Generator generator;
	private List<CodeGenerationListener> listeners = new ArrayList<CodeGenerationListener>();
	private Shell ignoredShell;
	private boolean recording;
	private Event lastModifyEvent;
	
	private GenerationStackRule longestMatchedStack;
	private GenerationComplexRule longestMatchedComplex;
	
	private boolean newGenerationStackRules = true;
	private boolean newGenerationComplexRules = true;
	private List<GenerationStackRule> modifGenerationStackRules = new ArrayList<GenerationStackRule>();
	private List<GenerationComplexRule> modifGenerationComplexRules = new ArrayList<GenerationComplexRule>();
	
	private String currentShellView;

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
	
	public void addListener(CodeGenerationListener listener) {
		this.listeners.add(listener);
	}

	public void ignoreShell(Shell shell) {
		this.ignoredShell = shell;
	}

	public boolean isRecording() {
		return this.recording;
	}

	public void switchRecording() {
		this.recording = !this.recording;
	}

	public Generator getCurrentGenerator() {
		return this.generator;
	}

	public boolean useStacks() {
		return useStacks;
	}

	public void useStacks(boolean useStacks) {
		this.useStacks = useStacks;
	}
	
	public void handleEvent(Event event) {
		if (!recording) {
			return;
		}
		if (this.ignoredShell != null && event.widget instanceof Control
				&& this.ignoredShell.equals(WidgetUtils.getShell((Control) event.widget))) {
			return;
		}
		if (!(event.widget instanceof Shell) && event.widget instanceof Control
				&& !(((Control) event.widget).isFocusControl()
						&& ((Control) event.widget).isVisible() 
							&& ((Control) event.widget).isEnabled())) {
			return;
		}
		if(event.type == SWT.DefaultSelection){
			System.out.println("DEF SELECTIOn");
		}
		

		/*
		 * Excpetion 1: Modify Events are a stream, only last one is interesting
		 * We should check whether an event was supported by another rule
		 * between 2 modifies. If yes => It's a new setText, apply setText rule
		 * if any If no => It's still the same setText, event is stored for
		 * later
		 */
		if (this.lastModifyEvent != null) {
			// unrelated event
			if (event.type != SWT.Modify
					|| event.widget != this.lastModifyEvent.widget) {
				processRules(this.lastModifyEvent);
				this.lastModifyEvent = null;
			}
		}
		if (event.type == SWT.Modify) {
			if(event.widget instanceof Text){
				if(((Text)event.widget).getMessage() != null){
					if(((Text)event.widget).getMessage().equals(((Text)event.widget).getText())){
						return;
					} else if(((Text)event.widget).getText().isEmpty()){
						return;
					}
				}
			}
 			Control control = (Control) event.widget;
			// new event or next one on same widget
			if (this.lastModifyEvent == null
					|| this.lastModifyEvent.widget == control) {
				this.lastModifyEvent = event;
	 			// Store for later usage so it can be overriden if a newer
				// ModifyEvent on samme widget happen
				return;
			}
		}
		
		processRules(event);
	}
	
	private void processRules(Event event) {
		if(event.widget instanceof Shell){
			System.out.println("CurrentShell"+((Shell)event.widget).getText());
		}
		for (GenerationSimpleRule rule : generator.createSimpleRules()) {
			if (rule.appliesTo(event)) {
				rule.initializeForEvent(event);
				listOfSimpleRules.add(rule);
			}
		}
		if(!listOfSimpleRules.isEmpty()){  //find complex rules			
			
			if(newGenerationComplexRules){
				modifGenerationComplexRules.addAll(generator.createComplexRules());
			}
			newGenerationComplexRules = true;
			Set<GenerationComplexRule> toDelete = matchComplexRules(modifGenerationComplexRules);
			modifGenerationComplexRules.removeAll(toDelete);
			if(longestMatchedComplex != null && ((modifGenerationComplexRules.size() == 0)||
					(modifGenerationComplexRules.size() == 1 && longestMatchedComplex.equals(modifGenerationComplexRules.get(0))))){ //match
					processComplexMatch();
			} else if(modifGenerationComplexRules.size() == 0 && longestMatchedComplex == null){//no match,
				processComplexNoMatch(); //keep simple rule and move on
			} else if(modifGenerationComplexRules.size() > 0){
				newGenerationComplexRules = false; //get next event
				return;
			}
		}
			
		
		
		if (useStacks && !listOfRules.isEmpty()) {
			if(newGenerationStackRules){
				modifGenerationStackRules.addAll(generator.createStackRules());
				for(GenerationStackRule used: setOfUsedStackRules){
					if(used.getMethods() != null){
						modifGenerationStackRules.addAll(used.getMethods());
					}
				}
			}
			newGenerationStackRules=true;
			Set<GenerationStackRule> toDelete = matchStackRules(modifGenerationStackRules);
			modifGenerationStackRules.removeAll(toDelete);
			if(longestMatchedStack != null && ((modifGenerationStackRules.size() == 0)||
				(modifGenerationStackRules.size() == 1 && longestMatchedStack.equals(modifGenerationStackRules.get(0))))){ //match,generate eclipse
				processStackMatch();
			} else if(modifGenerationStackRules.size() == 0 && longestMatchedStack == null){//no match, generate swt
				//swt code for first item it listOfRules
				//others check again if any stack is matches
				processStackNoMatch();
			} else if(modifGenerationStackRules.size() > 0){
				newGenerationStackRules = false; //get next event
				return;
			}
		}
	}
	
	private void processComplexMatch(){
		List<GenerationSimpleRule> rulesToKeep = new ArrayList<GenerationSimpleRule>();
		int toKeep = listOfSimpleRules.size()-longestMatchedComplex.getInitializationRules().size();
		for(int i=toKeep; i>0; i--){
			rulesToKeep.add(listOfSimpleRules.get(listOfSimpleRules.size()-i));
		}
		listOfSimpleRules.removeAll(rulesToKeep);
		longestMatchedComplex.initializeForRules(listOfSimpleRules);
		if(useStacks){
			listOfRules.add(longestMatchedComplex);
		} else {
			dispatchCodeGenerated(longestMatchedComplex.generateCode());
		}
		longestMatchedComplex=null;
		listOfSimpleRules = new ArrayList<GenerationSimpleRule>();
		listOfSimpleRules.addAll(rulesToKeep);
	}
	
	private void processComplexNoMatch(){
		if(useStacks){
			listOfRules.add(listOfSimpleRules.get(0));
		} else {
			dispatchCodeGenerated(listOfSimpleRules.get(0).generateCode());
		}
		listOfSimpleRules.remove(0);
	}
	
	
	private void processStackMatch(){
		List<GenerationRule> rulesToKeep = new ArrayList<GenerationRule>();
		int toKeep = listOfRules.size()-longestMatchedStack.getInitializationRules().size();
		for(int i=toKeep; i>0; i--){
			rulesToKeep.add(listOfRules.get(listOfRules.size()-i));
		}
		listOfRules.removeAll(rulesToKeep);
		List<String> toGenerate = longestMatchedStack.generateInitializationPhase(listOfRules,setOfUsedStackRules);
		for(String generate: toGenerate){
			dispatchCodeGenerated(generate);
		}
		setOfUsedStackRules.add(longestMatchedStack);
		longestMatchedStack=null;
		listOfRules.clear();
		listOfRules.addAll(rulesToKeep);
	}
	
	private void processStackNoMatch(){
		dispatchCodeGenerated((listOfRules.get(0)).generateCode());
		listOfRules.remove(0);
	}
	
	
	private Set<GenerationStackRule> matchStackRules(List<GenerationStackRule> modifGenerationStackRules){
		Set<GenerationStackRule> toDelete= new HashSet<GenerationStackRule>();
		for(int i=0; i<listOfRules.size(); i++){
			for(GenerationStackRule stackRule: modifGenerationStackRules){
				if(stackRule.getInitializationRules().size() < listOfRules.size()){ //stack rule has less rules that listofRules
					toDelete.add(stackRule);
				}else{
					if(!stackRule.appliesTo(listOfRules.get(i), i)){
						toDelete.add(stackRule);
					} else if(stackRule.getInitializationRules().size()-1==i){
						longestMatchedStack=stackRule;
					}
				}
			}
		}
		return toDelete;
	}
	
	private Set<GenerationComplexRule> matchComplexRules(List<GenerationComplexRule> modifGenerationComplexRules){
		Set<GenerationComplexRule> toDelete= new HashSet<GenerationComplexRule>();
		for(int i=0; i<listOfSimpleRules.size(); i++){
			for(GenerationComplexRule complexRule: modifGenerationComplexRules){
				if(complexRule.getInitializationRules().size() < listOfSimpleRules.size()){ //complex rule has less rules that listofRules
					toDelete.add(complexRule);
				}else{
					if(!complexRule.appliesTo(listOfSimpleRules.get(i), i)){
						toDelete.add(complexRule);
					} else if(complexRule.getInitializationRules().size()-1==i){
						longestMatchedComplex=complexRule;
					}
				}
			}
		}
		return toDelete;
	}

	private void dispatchCodeGenerated(String code) {
		for (CodeGenerationListener listener : this.listeners) {
			listener.handleCodeGenerated(code);
		}
	}

}
