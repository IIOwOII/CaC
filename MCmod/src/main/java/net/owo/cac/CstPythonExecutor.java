package net.owo.cac;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.owo.cac.network.CacModVariables;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CstPythonExecutor {
	
	public static void runPupil() {
		String dir_comp = CacModVariables.Dir_components;
		try {
			// /home/owo/anaconda3/envs/py39/bin/python
			List<String> command = Arrays.asList("python", dir_comp + "/PupilMessage.py");
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
	                System.out.println("Python Output: " + line);
	            }
			} 
			int exitCode = process.waitFor();
	        System.out.println("Exit Code: " + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
