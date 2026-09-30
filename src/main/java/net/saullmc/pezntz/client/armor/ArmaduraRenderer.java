package net.saullmc.pezntz.client.armor;

import net.saullmc.pezntz.item.custom.ArmaduraModItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaduraRenderer extends GeoArmorRenderer<ArmaduraModItem> {

    public ArmaduraRenderer(String modelo, String textura) {
        super(new ArmaduraGeoModel(modelo, textura));
    }
}