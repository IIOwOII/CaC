package net.owo.cac;

public class CstState {
    private static boolean IsMeowview = false;
    public static boolean rot_old = false;
    public static float rot_angle = 0F;

    public static void switchMeowview() {
    	IsMeowview = (!IsMeowview);
    }

    public static boolean getMeowview() {
    	return IsMeowview;
    }

}