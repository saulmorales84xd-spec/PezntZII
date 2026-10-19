package net.saullmc.pezntz.client.block;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class CofreCandadoModel {

    public static final String TAPA = "lid";
    public static final String BASE = "bottom";
    public static final String CERRADURA = "lock";

    public static LayerDefinition createBodyLayer() {
        MeshDefinition malla = new MeshDefinition();
        PartDefinition raiz = malla.getRoot();

        raiz.addOrReplaceChild(BASE,
                CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F),
                PartPose.ZERO);

        raiz.addOrReplaceChild(TAPA,
                CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 1.0F, 14.0F, 5.0F, 14.0F),
                PartPose.offset(0.0F, 9.0F, 1.0F));

        raiz.addOrReplaceChild(CERRADURA,
                CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 9.0F, 1.0F));

        return LayerDefinition.create(malla, 64, 64);
    }

    private CofreCandadoModel() {
    }
}