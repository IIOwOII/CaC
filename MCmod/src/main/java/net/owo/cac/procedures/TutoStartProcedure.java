package net.owo.cac.procedures;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.owo.cac.network.CacModVariables;
import net.owo.cac.CstTutorial;

import net.minecraft.world.level.LevelAccessor;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class TutoStartProcedure {
	public static void execute(LevelAccessor world) {
		JsonObject obj_file = new JsonObject();
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(CacModVariables.Pool_tutorial));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				obj_file = new Gson().fromJson(jsonstringbuilder.toString(), JsonObject.class);
				CstTutorial.tuto_que = obj_file.get(CacModVariables.MapVariables.get(world).Exp_session).getAsJsonArray();
				CstTutorial.updateTutoQue();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
