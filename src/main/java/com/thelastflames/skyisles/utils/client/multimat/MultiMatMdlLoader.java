package com.thelastflames.skyisles.utils.client.multimat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.ArrayList;
import java.util.function.Function;

public class MultiMatMdlLoader implements IGeometryLoader<MultiMatMdlLoader.MultiMatGeometry> {
    @Override
    public MultiMatGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new MultiMatGeometry(jsonObject, deserializationContext);
    }

    public static class MultiMatGeometry implements IUnbakedGeometry<MultiMatGeometry> {
        JsonObject jsonObject;
        JsonDeserializationContext deserializationContext;

        public MultiMatGeometry(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            this.jsonObject = jsonObject;
            this.deserializationContext = deserializationContext;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            Minecraft mc = Minecraft.getInstance();

            ResourceLocation parent = new ResourceLocation(jsonObject.getAsJsonPrimitive("parent").getAsString());
            UnbakedModel mdl = baker.getModel(parent);
            BakedModel baked = mdl.bake(baker, (mat) -> spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft:block/stone"))), modelState, parent);

            ArrayList<ResourceLocation> layers = new ArrayList<>();
            for (JsonElement jsonElement : jsonObject.getAsJsonArray("layers")) {
                layers.add(new ResourceLocation(jsonElement.getAsString()));
            }

            return new MultiMatBakedModel(
                    (res) -> spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, res)), spriteGetter.apply(new Material(
                            TextureAtlas.LOCATION_BLOCKS,
                            new ResourceLocation(jsonObject.getAsJsonPrimitive("particle").getAsString())
                    )),
                    mc, baked, layers.toArray(new ResourceLocation[0])
            );
        }
    }
}
