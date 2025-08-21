package net.owo.cac;

public class CstState {
    private static boolean IsMeowView = false; // Camera
    public static boolean CanMeowMove = false; // Is it allowed to move by arrow?
    public static boolean IsMeowMove_old = false; // Is it moved by arrow move right before?
    public static float rot_angle = 0F;

	public static boolean[] key_pressed = {false, false, false, false, false};
	public static boolean[] key_pressed_old = {false, false, false, false, false};

    public static void switchMeowView() {
    	IsMeowView = (!IsMeowView);
    }

    public static boolean getMeowView() {
    	return IsMeowView;
    }

	public static int getKeyChanged(int KI) {
		int key_changed = -1;
		if (!key_pressed_old[KI] && key_pressed[KI]){
			key_changed = 0; // pressed
			key_pressed_old[KI] = true;
		} else if (key_pressed_old[KI] && !key_pressed[KI]) {
			key_changed = 1; // released
			key_pressed_old[KI] = false;
		}
		return key_changed;
	}
}