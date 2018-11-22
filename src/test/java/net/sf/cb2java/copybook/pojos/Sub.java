package net.sf.cb2java.copybook.pojos;

import net.sf.cb2java.copybook.CB2J;

public class Sub {
	
	@CB2J(path="E")
	private String e;
	@CB2J(path="F")
	private String f;
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
}
