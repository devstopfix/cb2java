package net.sf.cb2java.copybook.pojos;

import java.util.List;

import net.sf.cb2java.copybook.CB2J;

public class B {
	
	@CB2J(path="ROOT.SUB")
	private List<Sub> subs;

	public List<Sub> getSubs() {
		return subs;
	}

	public void setSubs(List<Sub> subs) {
		this.subs = subs;
	}

}
