package net.saullmc.pezntz.client.armor;

import net.saullmc.pezntz.item.custom.ArmaduraTacticaItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ArmaduraTacticaRenderer extends GeoArmorRenderer<ArmaduraTacticaItem> {

    public ArmaduraTacticaRenderer() {
        super(new ArmaduraTacticaModel());
    }
}