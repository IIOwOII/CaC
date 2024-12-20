package net.mcreator.cac.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.IntTag;

import net.mcreator.cac.network.CacModVariables;

public class FncFieldWallProcedure {
	public static Vec3 execute(LevelAccessor world, double x, double z, Entity entity) {
		if (entity == null)
			return Vec3.ZERO;
		ListTag list_vertice_start;
		ListTag list_vertice_end;
		ListTag list_line;
		double offset_x = 0;
		double offset_z = 0;
		double sca_L_A = 0;
		double sca_L_B = 0;
		double sca_D = 0;
		double sca_K = 0;
		Vec3 vec_field = Vec3.ZERO;
		Vec3 vec_opponent = Vec3.ZERO;
		Vec3 vec_PA = Vec3.ZERO;
		Vec3 vec_PB = Vec3.ZERO;
		Vec3 basis_u = Vec3.ZERO;
		Vec3 basis_n = Vec3.ZERO;
		Vec3 vec_H = Vec3.ZERO;
		Vec3 vec_subfield = Vec3.ZERO;
		list_line = new ListTag();
		list_vertice_start = new ListTag();
		list_vertice_end = new ListTag();
		offset_x = CacModVariables.MapVariables.get(world).Pos_offset.x();
		offset_z = CacModVariables.MapVariables.get(world).Pos_offset.z();
		vec_field = Vec3.ZERO;
		vec_opponent = new Vec3((x - (offset_x + 0.5)), 0, (z - (offset_z + 0.5)));
		list_line = CacModVariables.MapVariables.get(world).List_wall.copy();
		sca_K = entity.getPersistentData().getDouble("K_Wall");
		for (Tag dataelementiterator : list_line) {
			list_vertice_start = (((dataelementiterator instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).get(0)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).copy();
			list_vertice_end = (((dataelementiterator instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).get(1)) instanceof ListTag _listTag ? _listTag.copy() : new ListTag()).copy();
			vec_PA = (new Vec3(((double) ((list_vertice_start.get(0)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0)), 0, ((double) ((list_vertice_start.get(1)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0)))).subtract(vec_opponent);
			vec_PB = (new Vec3(((double) ((list_vertice_end.get(0)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0)), 0, ((double) ((list_vertice_end.get(1)) instanceof IntTag _intTag ? _intTag.getAsInt() : 0)))).subtract(vec_opponent);
			basis_u = (vec_PA.subtract(vec_PB)).normalize();
			sca_L_A = vec_PA.dot(basis_u);
			sca_L_B = vec_PB.dot(basis_u);
			vec_H = ((vec_PB.scale(sca_L_A)).subtract((vec_PA.scale(sca_L_B)))).scale((1 / (vec_PA.subtract(vec_PB)).length()));
			sca_D = vec_H.length();
			if (sca_D < 0.01) {
				vec_subfield = basis_u.scale((sca_K * (1 / sca_L_A - 1 / sca_L_B)));
			} else {
				basis_n = vec_H.normalize();
				vec_subfield = (basis_u.scale((sca_K / vec_PA.length() - sca_K / vec_PB.length()))).add((basis_n.scale(((-(sca_K / sca_D)) * (sca_L_A / vec_PA.length() - sca_L_B / vec_PB.length())))));
			}
			vec_field = vec_field.add(vec_subfield);
		}
		return vec_field;
	}
}
