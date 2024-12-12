// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelCaC_Mouse<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "cac_mouse"), "main");
	private final ModelPart Body;
	private final ModelPart Body_behind;
	private final ModelPart Tail_1;
	private final ModelPart Tail_2;
	private final ModelPart Tail_3;

	public ModelCaC_Mouse(ModelPart root) {
		this.Body = root.getChild("Body");
		this.Body_behind = root.getChild("Body_behind");
		this.Tail_1 = root.getChild("Tail_1");
		this.Tail_2 = this.Tail_1.getChild("Tail_2");
		this.Tail_3 = this.Tail_2.getChild("Tail_3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body",
				CubeListBuilder.create().texOffs(0, 4)
						.addBox(-2.0F, -3.0F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(2, 0)
						.addBox(-1.5F, -2.0F, -3.5F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(10, 0)
						.addBox(-2.5F, -3.5F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.5F, -3.5F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Body_behind = partdefinition.addOrReplaceChild("Body_behind", CubeListBuilder.create()
				.texOffs(3, 10).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 23.0F, 1.5F));

		PartDefinition Tail_1 = partdefinition.addOrReplaceChild("Tail_1", CubeListBuilder.create().texOffs(6, 12)
				.addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 23.0F, 2.5F));

		PartDefinition Tail_2 = Tail_1.addOrReplaceChild("Tail_2", CubeListBuilder.create().texOffs(6, 13).addBox(0.0F,
				-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition Tail_3 = Tail_2.addOrReplaceChild("Tail_3", CubeListBuilder.create().texOffs(6, 14).addBox(0.0F,
				-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body_behind.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Tail_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}