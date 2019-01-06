package ch.openech.dancer.frontend;

import org.minimalj.backend.Backend;
import org.minimalj.frontend.action.Action;

import ch.openech.dancer.backend.DanceCubeCrawler;

public class DanceCubeAction extends Action {

	@Override
	public void action() {
		System.out.println(Backend.execute(new DanceCubeCrawler()));
	}

}