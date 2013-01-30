package org.eclipse.swtbot.generator.framework;

import org.eclipse.swt.widgets.Event;

public abstract class GenerationSimpleRule extends GenerationRule{

	public abstract boolean appliesTo(Event event);

	public abstract void initializeForEvent(Event event) ;
	
}
