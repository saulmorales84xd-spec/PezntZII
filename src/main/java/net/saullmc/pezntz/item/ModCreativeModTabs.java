package net.saullmc.pezntz.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PezntZMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEMS = CREATIVE_MODE_TABS.register("items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TELA.get()))
                    .title(Component.translatable("creativetab.items"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModItems.TELA.get());
                        pOutput.accept(ModItems.BACKPACK.get());
                        pOutput.accept(ModItems.CHATARRA.get());
                        pOutput.accept(ModItems.CANDADO.get());
                        pOutput.accept(ModItems.LLAVE_CANDADO.get());
                        pOutput.accept(ModItems.LLAVE_VEHICULO.get());
                        pOutput.accept(ModItems.PEZNSINITA_BRUTE.get());
                        pOutput.accept(ModItems.PEZNSINITA_INGOT.get());
                        pOutput.accept(ModItems.TITANIUM_BRUTE.get());
                        pOutput.accept(ModItems.TITANIUM_INGOT.get());
                        pOutput.accept(ModItems.CLAVOS.get());
                        pOutput.accept(ModItems.PLACA_ACERO.get());
                        pOutput.accept(ModItems.AGUJA_HILO.get());
                        pOutput.accept(ModItems.AZUFRE.get());
                        pOutput.accept(ModItems.CARBON_NITRIDO.get());
                        pOutput.accept(ModItems.LATA_VACIA.get());
                        pOutput.accept(ModItems.BOTELLA_ALCOHOL.get());
                        pOutput.accept(ModItems.RESORTE.get());
                        pOutput.accept(ModItems.TUERCA.get());
                        pOutput.accept(ModItems.CHATARRA_REFORZADA.get());
                        pOutput.accept(ModItems.CHATARRA_ELECTRONICA.get());
                        pOutput.accept(ModItems.CABLES.get());
                        pOutput.accept(ModItems.CINTA.get());
                        pOutput.accept(ModItems.LATA_ATUN_CERRADA.get());
                        pOutput.accept(ModItems.LATA_POLLO_CERRADA.get());
                        pOutput.accept(ModItems.LATA_CARNE_CERRADA.get());

                        pOutput.accept(ModItems.LATA_ATUN.get());
                        pOutput.accept(ModItems.LATA_POLLO.get());
                        pOutput.accept(ModItems.LATA_CARNE.get());
                        pOutput.accept(ModItems.CEREALES.get());
                        pOutput.accept(ModItems.NACHOS.get());
                        pOutput.accept(ModItems.PAQUETE_MRE.get());
                        pOutput.accept(ModItems.BARRA_CHOCOLATE.get());
                        pOutput.accept(ModItems.BARRA_GRANOLA.get());

                        pOutput.accept(ModItems.LANTERN.get());
                        pOutput.accept(ModItems.ABRELATAS.get());



                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
