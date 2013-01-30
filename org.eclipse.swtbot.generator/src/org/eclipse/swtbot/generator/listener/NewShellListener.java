package org.eclipse.swtbot.generator.listener;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

public class NewShellListener implements IPartListener2,IPageChangedListener{

	public void partActivated(IWorkbenchPartReference arg0) {
		System.out.println("++++++++++++++++++");
		System.out.println("part activated");
		System.out.println(arg0.getTitle());
	}

	public void partBroughtToTop(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public void partClosed(IWorkbenchPartReference arg0) {
		System.out.println("++++++++++++++++++");
		System.out.println("part closed");
		System.out.println(arg0.getTitle());
		
	}

	public void partDeactivated(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public void partHidden(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public void partInputChanged(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public void partOpened(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public void partVisible(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public void pageChanged(PageChangedEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
