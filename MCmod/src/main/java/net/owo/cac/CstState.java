package net.owo.cac;

public class CstState {
    private static boolean IsMeowView = false; // Camera
    public static boolean CanMeowMove = false; // Is it allowed to move by arrow?
    public static boolean IsMeowMove_old = false; // Is it moved by arrow move right before?
    public static float rot_angle = 0F;

    public static int arrow_right = 0;
    public static int arrow_left = 0;
    public static int arrow_up = 0;
    public static int arrow_down = 0;

    public static int arrow_tick = 0;

    public static void switchMeowView() {
    	IsMeowView = (!IsMeowView);
    }

    public static boolean getMeowView() {
    	return IsMeowView;
    }

}