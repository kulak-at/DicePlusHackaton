package eu.rotato.diceplushackaton.model;

public class DiceData
{
	public int currentFace;
	public int currentYaw;
	public int baseYaw;
	public int resultColor;
	
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
}