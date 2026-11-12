package net.saullmc.pezntz.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.saullmc.pezntz.PezntZMod;
import net.saullmc.pezntz.item.ModItems;

@Mod.EventBusSubscriber(modid = PezntZMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecargaLinterna extends CustomRecipe {

    public static final float CARGA_POR_BATERIA = 0.20F;

    public static final int MAXIMO_BATERIAS = 8;

    public static final boolean NO_DESPERDICIAR = true;

    public static final String NOMBRE = "recarga_linterna";

    public static final SimpleCraftingRecipeSerializer<RecargaLinterna> SERIALIZADOR =
            new SimpleCraftingRecipeSerializer<>(RecargaLinterna::new);

    public RecargaLinterna(ResourceLocation id, CraftingBookCategory categoria) {
        super(id, categoria);
    }

    @Override
    public boolean matches(CraftingContainer mesa, Level nivel) {
        return !resultado(mesa).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer mesa, RegistryAccess registros) {
        return resultado(mesa);
    }

    private static ItemStack resultado(CraftingContainer mesa) {
        ItemStack linterna = ItemStack.EMPTY;
        int baterias = 0;

        for (int i = 0; i < mesa.getContainerSize(); i++) {
            ItemStack cosa = mesa.getItem(i);
            if (cosa.isEmpty()) continue;

            if (cosa.is(ModItems.LANTERN.get())) {
                if (!linterna.isEmpty()) return ItemStack.EMPTY;
                linterna = cosa;
            } else if (cosa.is(ModItems.BATERIAS.get())) {
                baterias++;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (linterna.isEmpty() || baterias == 0 || baterias > MAXIMO_BATERIAS) return ItemStack.EMPTY;

        int gastado = linterna.getDamageValue();
        if (gastado <= 0) return ItemStack.EMPTY;

        int porBateria = Math.max(1, Math.round(linterna.getMaxDamage() * CARGA_POR_BATERIA));

        if (NO_DESPERDICIAR && (baterias - 1) * porBateria >= gastado) return ItemStack.EMPTY;

        ItemStack recargada = linterna.copy();
        recargada.setCount(1);
        recargada.setDamageValue(Math.max(0, gastado - baterias * porBateria));
        return recargada;
    }

    @Override
    public boolean canCraftInDimensions(int ancho, int alto) {
        return ancho * alto >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZADOR;
    }

    @SubscribeEvent
    public static void alRegistrar(RegisterEvent evento) {
        evento.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS,
                ayudante -> ayudante.register(new ResourceLocation(PezntZMod.MOD_ID, NOMBRE), SERIALIZADOR));
    }
}