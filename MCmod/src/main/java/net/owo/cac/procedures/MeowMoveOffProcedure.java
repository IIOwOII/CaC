package net.owo.cac.procedures;

import com.mojang.blaze3d.platform.InputConstants;

import net.owo.cac.CstState;
import net.minecraft.client.KeyMapping;

public class MeowMoveOffProcedure {
	public static void execute() {
		CstState.CanMeowMove = false;
		if (CstState.IsMeowMove_old) {
			InputConstants.Key key_forward = InputConstants.getKey("key.keyboard.w");
			KeyMapping.set(key_forward, false);
			CstState.IsMeowMove_old = false;
		}
	}
}
