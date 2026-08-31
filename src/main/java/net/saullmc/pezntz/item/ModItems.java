package net.saullmc.pezntz.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.custom.Adrenalina;
import net.saullmc.pezntz.item.custom.Backpack;
import net.saullmc.pezntz.item.custom.FlashlightItem;
import net.saullmc.pezntz.item.custom.Vendas;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries. ITEMS, PezntZMod.MOD_ID);

    public static final RegistryObject<Item> TELA = ITEMS.register("tela",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BACKPACK = ITEMS.register("backpack",
            () -> new Backpack(new Item.Properties()));

    public static final RegistryObject<Item> CHATARRA = ITEMS.register("chatarra",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CANDADO = ITEMS.register("candado",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LLAVE_CANDADO = ITEMS.register("llave_candado",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LLAVE_VEHICULO = ITEMS.register("llave_vehiculo",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEZNSINITA_BRUTE = ITEMS.register("peznsinita_brute",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PEZNSINITA_INGOT = ITEMS.register("peznsinita_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TITANIUM_BRUTE = ITEMS.register("titanium_brute",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CLAVOS = ITEMS.register("clavos",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PLACA_ACERO = ITEMS.register("placa_acero",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AGUJA_HILO = ITEMS.register("aguja_hilo",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AZUFRE = ITEMS.register("azufre",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CARBON_NITRIDO = ITEMS.register("carbon_nitrido",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LATA_VACIA = ITEMS.register("lata_vacia",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BOTELLA_ALCOHOL = ITEMS.register("botella_alcohol",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RESORTE = ITEMS.register("resorte",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TUERCA = ITEMS.register("tuerca",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHATARRA_REFORZADA = ITEMS.register("chatarra_reforzada",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHATARRA_ELECTRONICA = ITEMS.register("chatarra_electronica",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CABLES = ITEMS.register("cables",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CINTA = ITEMS.register("cinta",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LATA_ATUN_CERRADA = ITEMS.register("lata_atun_cerrada",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LATA_POLLO_CERRADA = ITEMS.register("lata_pollo_cerrada",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LATA_CARNE_CERRADA = ITEMS.register("lata_carne_cerrada",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> LATA_ATUN = ITEMS.register("lata_atun",
            () -> new Item(new Item.Properties().food(ModFoods.LATA_ATUN)));

    public static final RegistryObject<Item> LATA_POLLO = ITEMS.register("lata_pollo",
            () -> new Item(new Item.Properties().food(ModFoods.LATA_POLLO)));

    public static final RegistryObject<Item> LATA_CARNE = ITEMS.register("lata_carne",
            () -> new Item(new Item.Properties().food(ModFoods.LATA_CARNE)));

    public static final RegistryObject<Item> CEREALES = ITEMS.register("cereales",
            () -> new Item(new Item.Properties().food(ModFoods.CEREALES)));

    public static final RegistryObject<Item> NACHOS = ITEMS.register("nachos",
            () -> new Item(new Item.Properties().food(ModFoods.NACHOS)));

    public static final RegistryObject<Item> PAQUETE_MRE = ITEMS.register("paquete_mre",
            () -> new Item(new Item.Properties().food(ModFoods.PAQUETE_MRE)));

    public static final RegistryObject<Item> BARRA_CHOCOLATE = ITEMS.register("barra_chocolate",
            () -> new Item(new Item.Properties().food(ModFoods.BARRA_CHOCOLATE)));

    public static final RegistryObject<Item> BARRA_GRANOLA = ITEMS.register("barra_granola",
            () -> new Item(new Item.Properties().food(ModFoods.BARRA_GRANOLA)));

    public static final RegistryObject<Item> LANTERN = ITEMS.register("lantern",
            () -> new FlashlightItem(new Item.Properties()));

    public static final RegistryObject<Item> ABRELATAS = ITEMS.register("abrelatas",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ADRENALINA = ITEMS.register("adrenalina",
            () -> new Adrenalina(new Item.Properties()));

    public static final RegistryObject<Item> VENDAS = ITEMS.register("vendas",
            () -> new Vendas(new Item.Properties()));

    public static final RegistryObject<Item> BOLA_ACIDO = ITEMS.register("bola_acido",
            () -> new Item(new Item.Properties()));

    public static void register (IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
