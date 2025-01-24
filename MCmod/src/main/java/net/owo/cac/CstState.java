package net.owo.cac;

public class CstState {
    private static boolean IsMeowview = false;

    public static void switchMeowview() {
    	IsMeowview = (!IsMeowview);
    }

    public static boolean getMeowview() {
    	return IsMeowview;
    }
}