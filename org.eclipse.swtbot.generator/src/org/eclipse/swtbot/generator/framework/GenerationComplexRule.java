package org.eclipse.swtbot.generator.framework;

import java.util.List;



public abstract class GenerationComplexRule extends GenerationRule{


	public abstract boolean appliesTo(GenerationSimpleRule rule, int i);
	
	public abstract List<GenerationSimpleRule> getInitializationRules();
	
	public abstract void initializeForRules(List<GenerationSimpleRule> rules) ;
	
}
