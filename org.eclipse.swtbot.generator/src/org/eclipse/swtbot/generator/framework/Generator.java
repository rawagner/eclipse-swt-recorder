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
package org.eclipse.swtbot.generator.framework;

import java.util.List;

public abstract class Generator {

	public List<GenerationStackRule> createStackRules(){
		return sortStackRules();
	}
	public abstract List<GenerationSimpleRule> createSimpleRules();
	public List<GenerationComplexRule> createComplexRules(){
		return sortComplexRules();
	}
	
	protected abstract List<GenerationComplexRule> sortComplexRules();
	protected abstract List<GenerationStackRule> sortStackRules();
	
	public abstract String getLabel();
	public abstract boolean useStacks();

}
