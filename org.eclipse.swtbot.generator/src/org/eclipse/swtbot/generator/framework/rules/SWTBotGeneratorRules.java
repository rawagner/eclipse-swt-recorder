/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.GenerationStackRule;
import org.eclipse.swtbot.generator.framework.Generator;

public class SWTBotGeneratorRules extends Generator {

	public List<GenerationSimpleRule> createSimpleRules() {
		List<GenerationSimpleRule> res = new ArrayList<GenerationSimpleRule>();
		/*
		res.add(new PushButtonClickedRule());
		res.add(new CheckboxClickedRule());
		res.add(new RadioButtonClickedRule());
		res.add(new ComboSelectionRule());
		res.add(new CComboSelectionRule());
		res.add(new ExpandTreeItemRule());
		res.add(new DoubleClickTreeItemRule());
		res.add(new MenuClickedRule());
		res.add(new SelectTreeItemRule());
		res.add(new ModifyTextRule());
		*/
		return res;
		
	}

	public String getLabel() {
		return "SWTBot";
	}

	public boolean useStacks() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected List<GenerationComplexRule> sortComplexRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<GenerationStackRule> sortStackRules() {
		// TODO Auto-generated method stub
		return null;
	}
}
