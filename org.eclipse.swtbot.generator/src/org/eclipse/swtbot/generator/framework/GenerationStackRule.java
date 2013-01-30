package org.eclipse.swtbot.generator.framework;

import java.util.List;
import java.util.Set;

public abstract class GenerationStackRule {
	
	public abstract String getRuleName();
	
	public abstract List<GenerationRule> getInitializationRules();
	
	public abstract List<String> generateInitializationPhase(List<GenerationRule> rules, Set<GenerationStackRule> usedRules);
	
	public abstract boolean appliesTo(GenerationRule rule, int i);
	
	public abstract List<GenerationStackRule> getMethods();
	
	public abstract int hashCode();
	public abstract boolean equals(Object obj);

}
