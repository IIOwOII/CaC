package net.owo.cac;

public class CstState {
    public static boolean IsMeowView = false; // Camera
    public static boolean CanMeowMove = false; // Is it allowed to move by arrow?
    public static boolean IsMeowMove_old = false; // Is it moved by arrow move right before?
    public static int[] meowmove_tick = {0, 0, 0, 0, 0, 0, 0, 0}; // Total tick not reset
    public static float rot_angle = 0F;

	public static boolean[] key_pressed = {false, false, false, false, false, false};
	public static boolean[] key_pressed_old = {false, false, false, false, false, false};
	public static int[] key_pressed_tick = {0, 0, 0, 0};

    public static void switchMeowView() {
    	IsMeowView = (!IsMeowView);
    }

    public static boolean getMeowView() {
    	return IsMeowView;
    }

    public static void KeyTickUpdate() {
    	for (int KI=0; KI<4; KI++) {
    		if (key_pressed[KI]) {
    			key_pressed_tick[KI] += 1;
    		} else {
    			key_pressed_tick[KI] = 0;
    		}
    	}
    	
    	int ang = getKeyCase();
    	if (ang != -1) {
    		meowmove_tick[ang] += 1;
    	}
    }

    public static int getKeyCase() {
    	/*
    	-1 : no input
    	n (0~7) : n*45 degree
    	*/
    	int ang = -1;
    	int godel = (int) Math.round(Math.pow(2, 1+(key_pressed[0]?1:0)-(key_pressed[1]?1:0)) * Math.pow(3, 1+(key_pressed[2]?1:0)-(key_pressed[3]?1:0)));
		switch (godel) {
			case 1: ang = 5; break; // 0,0
			case 2: ang = 6; break; // 1,0
			case 4: ang = 7; break; // 2,0
			case 3: ang = 4; break; // 0,1
			case 6: ang = -1; break; // 1,1
			case 12: ang = 0; break; // 2,1
			case 9: ang = 3; break; // 0,2
			case 18: ang = 2; break; // 1,2
			case 36: ang = 1; break; // 2,2
		}
		return ang;
    }

    public static void AngleUpdate() {
    	int ang = getKeyCase();
    	if (ang == -1) {
    		return;
    	}
    	
    	float rot = 0.0F;
    	switch (ang) {
    		case 0: rot = -90.0F; break;
    		case 1: rot = -135.0F; break;
    		case 2: rot = 180.0F; break;
    		case 3: rot = 135.0F; break;
    		case 4: rot = 90.0F; break;
    		case 5: rot = 45.0F; break;
    		case 6: rot = 0.0F; break;
    		case 7: rot = -45.0F; break;
    	}
    	rot_angle = rot;
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