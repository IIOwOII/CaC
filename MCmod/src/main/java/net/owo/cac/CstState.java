package net.owo.cac;

public class CstState {
    private static boolean IsMeowView = false; // Camera
    public static boolean CanMeowMove = false; // Is it allowed to move by arrow?
    public static boolean IsMeowMove_old = false; // Is it moved by arrow move right before?
    public static float rot_angle = 0F;

    public static void switchMeowView() {
    	IsMeowView = (!IsMeowView);
    }

    public static boolean getMeowView() {
    	return IsMeowView;
    }

}