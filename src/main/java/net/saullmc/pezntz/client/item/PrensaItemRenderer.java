package net.saullmc.pezntz.client.item;

import net.saullmc.pezntz.item.custom.PrensaBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PrensaItemRenderer extends GeoItemRenderer<PrensaBlockItem> {

    public PrensaItemRenderer() {
        super(new PrensaItemModel());
    }
}