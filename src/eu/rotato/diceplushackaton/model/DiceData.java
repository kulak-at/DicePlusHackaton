package eu.rotato.diceplushackaton.model;

public class DiceData
{
	public int currentFace;
	public int currentYaw;
	public int baseYaw;
	public int resultColor;
	public int previousRoll;
	public boolean ignoreYaw;
	public int colorVal;
	public boolean firstYam = false;
	public float prevVal = -1000.0f;
	
	public void setFirstYam(boolean fYam) {
		this.firstYam = fYam;
	}
	
	public boolean isFirstYam() {
		return this.firstYam;
	}
	
	public int getColorVal() {
		return colorVal;
	}
	public void setColorVal(int colorVal) {
		this.colorVal = colorVal;
	}
	public boolean isIgnoreYaw() {
		return ignoreYaw;
	}
	public void setIgnoreYaw(boolean ignoreYaw) {
		this.ignoreYaw = ignoreYaw;
	}
	public int getPreviousRoll() {
		return previousRoll;
	}
	public void setPreviousRoll(int previousRoll) {
		this.previousRoll = previousRoll;
	}
	public int getCurrentFace() {
		return currentFace;
	}
	public void setCurrentFace(int currentFace) {
		this.currentFace = currentFace;
	}
	public int getCurrentYaw() {
		return currentYaw;
	}
	public void setCurrentYaw(int currentYaw) {
		this.currentYaw = currentYaw;
	}
	public int getBaseYaw() {
		return baseYaw;
	}
	public void setBaseYaw(int baseYaw) {
		this.baseYaw = baseYaw;
	}
	public int getResultColor() {
		return resultColor;
	}
	public void setResultColor(int resultColor) {
		this.resultColor = resultColor;
	}
	
	public float getPrevVal() {
		return this.prevVal;
	}
	
	public void setPrevVal(float prev) {
		this.prevVal = prev;
	}
}
