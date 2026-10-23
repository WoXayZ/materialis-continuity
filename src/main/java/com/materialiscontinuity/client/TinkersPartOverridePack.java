package com.materialiscontinuity.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.materialiscontinuity.Materialis;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.locating.IModFile;

/**
 * PNGs in {@code assets/materialis/textures/tinkers_parts/<material>/} override
 * Tinkers unique textures ({@code *_materialis_<material>.png}).
 * <p>
 * Nested files mirror Tinkers paths, e.g.
 * {@code fairy/item/tool/pickaxe/handle.png}.
 * Flat aliases (pick_head.png, …) still work for the part items.
 */
public class TinkersPartOverridePack extends AbstractPackResources {

	public static final String PACK_ID = "materialis_tinkers_part_overrides";
	private static final List<String> MATERIALS = List.of("fairy", "better_gold", "plaslitherite");
	private static final Map<String, String> PART_ALIASES = Map.ofEntries(
			Map.entry("pick_head", "textures/item/tool/pickaxe/head"),
			Map.entry("hammer_head", "textures/item/tool/sledge_hammer/head"),
			Map.entry("small_axe_head", "textures/item/tool/hand_axe/head"),
			Map.entry("broad_axe_head", "textures/item/tool/broad_axe/blade"),
			Map.entry("small_blade", "textures/item/tool/parts/small_blade"),
			Map.entry("broad_blade", "textures/item/tool/cleaver/head"),
			Map.entry("adze_head", "textures/item/tool/pickadze/adze"),
			Map.entry("large_plate", "textures/item/tool/parts/large_plate"),
			Map.entry("bow_limb", "textures/item/tool/longbow/limb_bottom"),
			Map.entry("bow_grip", "textures/item/tool/crossbow/body"),
			Map.entry("tool_handle", "textures/item/tool/parts/tool_handle"),
			Map.entry("tough_handle", "textures/item/tool/parts/tough_handle"),
			Map.entry("tool_binding", "textures/item/tool/parts/tool_binding"),
			Map.entry("tough_binding", "textures/item/tool/parts/tough_binding"),
			Map.entry("repair_kit", "textures/item/tool/parts/repair_kit"),
			Map.entry("maille", "textures/item/tool/parts/maille"),
			Map.entry("plating_helmet", "textures/item/tool/armor/plate/helmet/plating"),
			Map.entry("plating_chestplate", "textures/item/tool/armor/plate/chestplate/plating"),
			Map.entry("plating_leggings", "textures/item/tool/armor/plate/leggings/plating"),
			Map.entry("plating_boots", "textures/item/tool/armor/plate/boots/plating"));
	private static final String PACK_MCMETA = """
			{
			  "pack": {
			    "pack_format": 15,
			    "description": "Materialis Tinkers part texture overrides"
			  }
			}
			""";

	private final Path partsRoot;

	public TinkersPartOverridePack(String packId, Path partsRoot) {
		super(packId, true);
		this.partsRoot = partsRoot;
	}

	public static void addPackFinder(AddPackFindersEvent event) {
		if (event.getPackType() != PackType.CLIENT_RESOURCES) {
			return;
		}
		event.addRepositorySource(consumer -> {
			IModFile file = ModList.get().getModFileById(Materialis.MOD_ID).getFile();
			Path root = file.findResource("assets", "materialis", "textures", "tinkers_parts");
			Pack pack = Pack.readMetaAndCreate(
					PACK_ID,
					Component.literal("Materialis Part Texture Overrides"),
					true,
					id -> new TinkersPartOverridePack(id, root),
					PackType.CLIENT_RESOURCES,
					Pack.Position.TOP,
					PackSource.BUILT_IN);
			if (pack != null) {
				consumer.accept(pack);
			}
		});
	}

	@Nullable
	private Path resolveOverride(ResourceLocation location) {
		if (!"tconstruct".equals(location.getNamespace())) {
			return null;
		}
		String path = location.getPath();
		boolean meta = path.endsWith(".png.mcmeta");
		if (meta) {
			path = path.substring(0, path.length() - ".mcmeta".length());
		}
		if (!path.endsWith(".png")) {
			return null;
		}
		for (String material : MATERIALS) {
			String suffix = "_materialis_" + material + ".png";
			if (!path.endsWith(suffix)) {
				continue;
			}
			String base = path.substring(0, path.length() - suffix.length());
			String ext = meta ? ".png.mcmeta" : ".png";
			if (base.startsWith("textures/")) {
				Path nested = partsRoot.resolve(material).resolve(base.substring("textures/".length()) + ext);
				if (Files.isRegularFile(nested)) {
					return nested;
				}
			}
			for (Map.Entry<String, String> entry : PART_ALIASES.entrySet()) {
				if (entry.getValue().equals(base)) {
					Path alias = partsRoot.resolve(material).resolve(entry.getKey() + ext);
					if (Files.isRegularFile(alias)) {
						return alias;
					}
				}
			}
		}
		return null;
	}

	@Override
	@Nullable
	public IoSupplier<InputStream> getRootResource(String... path) {
		if (path.length == 1 && "pack.mcmeta".equals(path[0])) {
			return () -> new ByteArrayInputStream(PACK_MCMETA.getBytes(StandardCharsets.UTF_8));
		}
		return null;
	}

	@Override
	@Nullable
	public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
		if (type != PackType.CLIENT_RESOURCES) {
			return null;
		}
		Path file = resolveOverride(location);
		return file == null ? null : IoSupplier.create(file);
	}

	@Override
	public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
		if (type != PackType.CLIENT_RESOURCES || !"tconstruct".equals(namespace) || !Files.isDirectory(partsRoot)) {
			return;
		}
		String prefix = path.endsWith("/") ? path : path + "/";
		for (String material : MATERIALS) {
			Path materialRoot = partsRoot.resolve(material);
			if (!Files.isDirectory(materialRoot)) {
				continue;
			}
			try (Stream<Path> stream = Files.walk(materialRoot)) {
				stream.filter(Files::isRegularFile).forEach(file -> {
					String mapped = mapFile(materialRoot.relativize(file).toString().replace('\\', '/'), material);
					if (mapped == null) {
						return;
					}
					if (!mapped.equals(path) && !mapped.startsWith(prefix)) {
						return;
					}
					output.accept(new ResourceLocation("tconstruct", mapped), IoSupplier.create(file));
				});
			} catch (IOException ignored) {
			}
		}
	}

	@Nullable
	private static String mapFile(String relative, String material) {
		boolean meta = relative.endsWith(".png.mcmeta");
		String withoutExt;
		if (meta) {
			withoutExt = relative.substring(0, relative.length() - ".png.mcmeta".length());
		} else if (relative.endsWith(".png")) {
			withoutExt = relative.substring(0, relative.length() - ".png".length());
		} else {
			return null;
		}
		String ext = meta ? ".png.mcmeta" : ".png";
		if (withoutExt.contains("/")) {
			return "textures/" + withoutExt + "_materialis_" + material + ext;
		}
		String base = PART_ALIASES.get(withoutExt);
		return base == null ? null : base + "_materialis_" + material + ext;
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		Set<String> namespaces = new HashSet<>();
		if (type == PackType.CLIENT_RESOURCES) {
			namespaces.add("tconstruct");
		}
		return namespaces;
	}

	@Override
	public void close() {}
}
