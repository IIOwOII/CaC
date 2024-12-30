package net.owo.cac;

public class CstState {
    private static boolean IsHotbarVisible = true; // Start with hotbar visible
    private static boolean IsMeowview = false;

    public static void switchHotbar() {
    	IsHotbarVisible = (!IsHotbarVisible);
    }

    public static void switchMeowview() {
    	IsMeowview = (!IsMeowview);
    }

    public static boolean getHotbar() {
    	return IsHotbarVisible;
    }

    public static boolean getMeowview() {
    	return IsMeowview;
    }
}