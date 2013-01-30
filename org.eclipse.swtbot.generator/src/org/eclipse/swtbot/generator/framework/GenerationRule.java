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

public abstract class GenerationRule {
	
	public String generateCode() {
		return getWidgetAccessor() + getActon();
	}

	protected abstract String getWidgetAccessor();
	protected abstract String getActon();
	public abstract String getRuleName();
	public abstract int hashCode();
	public abstract boolean equals(Object obj);

}
