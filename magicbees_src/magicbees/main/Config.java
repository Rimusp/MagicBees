package magicbees.main;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import magicbees.block.BlockEffectJar;
import magicbees.block.BlockHive;
import magicbees.block.BlockPlanks;
import magicbees.block.BlockWoodSlab;
import magicbees.block.types.HiveType;
import magicbees.block.types.PlankType;
import magicbees.item.ItemCapsule;
import magicbees.item.ItemComb;
import magicbees.item.ItemCrystalAspect;
import magicbees.item.ItemDrop;
import magicbees.item.ItemMagicHiveFrame;
import magicbees.item.ItemMiscResources;
import magicbees.item.ItemMoonDial;
import magicbees.item.ItemNugget;
import magicbees.item.ItemPollen;
import magicbees.item.ItemPropolis;
import magicbees.item.ItemWax;
import magicbees.item.types.CapsuleType;
import magicbees.item.types.HiveFrameType;
import magicbees.item.types.NuggetType;
import magicbees.item.types.ResourceType;
import magicbees.item.types.WaxType;
import magicbees.main.utils.LocalizationManager;
import magicbees.main.utils.VersionInfo;
import magicbees.main.utils.compat.ArsMagicaHelper;
import magicbees.main.utils.compat.EquivalentExchangeHelper;
import magicbees.main.utils.compat.ForestryHelper;
import magicbees.main.utils.compat.ThaumcraftHelper;
import magicbees.storage.BackpackDefinition;
import magicbees.tileentity.TileEntityEffectJar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.BeeManager;
import forestry.api.storage.BackpackManager;
import forestry.api.storage.EnumBackpackType;

/**
 * A class to hold some data related to mod state & functions.
 * @author MysteriousAges
 *
 */
public class Config
{
	public boolean	DrawParticleEffects;	
	public boolean	BeeInfusionsAdded;
	public boolean	AddThaumcraftItemsToBackpacks;
	public boolean	DisableUpdateNotification;
	public boolean	AreMagicPlanksFlammable;
	public boolean	UseImpregnatedStickInTools;
	public boolean	MoonDialShowsPhaseInText;
	public String	ThaumaturgeExtraItems;
	public int		CapsuleStackSizeMax;
	public boolean	DoHiveRetrogen;
	public boolean	ForceHiveRegen;

	public static BlockPlanks planksWood;
	public static BlockWoodSlab slabWoodHalf;
	public static BlockWoodSlab slabWoodFull;
	public static BlockEffectJar effectJar;
	public static BlockHive hive;
	
	public static ItemComb combs;
	public static ItemWax wax;
	public static ItemPropolis propolis;
	public static ItemDrop drops;
	public static ItemPollen pollen;
	public static ItemCrystalAspect solidFlux;
	public static ItemMiscResources miscResources;
	public static ItemFood jellyBaby;
	public static Item thaumiumScoop;
	public static Item thaumiumGrafter;
	public static ItemNugget nuggets;
	public static ItemMoonDial moonDial;
	
	//----- Liquid Capsules --------------------
	public static ItemCapsule magicCapsule;
	public static ItemCapsule voidCapsule;
	
	//----- Apiary Frames ----------------------
	public static ItemMagicHiveFrame hiveFrameMagic;
	public static ItemMagicHiveFrame hiveFrameResilient;
	public static ItemMagicHiveFrame hiveFrameGentle;
	public static ItemMagicHiveFrame hiveFrameMetabolic;
	public static ItemMagicHiveFrame hiveFrameNecrotic;
	public static ItemMagicHiveFrame hiveFrameTemporal;
	public static ItemMagicHiveFrame hiveFrameOblivion;
	
	//----- Backpacks ------------------------------------------
	public static Item thaumaturgeBackpackT1;
	public static Item thaumaturgeBackpackT2;
	public static BackpackDefinition thaumaturgeBackpackDef;

	//----- Forestry Blocks ------------------------------------
	//----- Forestry Items -------------------------------------
	public static Item fBeeComb;
	public static Item fHoneydew;
	public static Item fHoneyDrop;
	public static Item fPollen;
	public static Item fCraftingResource;
	//----- Thaumcraft Blocks ----------------------------------
	public static Block tcPlant;
	public static Block tcCandle;
	public static Block tcCrystal;
	public static Block tcMarker;
	public static Block tcJar;
	public static Block tcLog;
	public static Block tcLeaf;
	public static Block tcWarded;
	//----- Thaumcraft Items -----------------------------------
	public static Item tcFilledJar;
	public static Item tcMiscResource;
	public static Item tcEssentiaBottle;
	public static Item tcShard;
	public static Item tcGolem;
	public static Item tcWispEssence;
	public static Item tcNuggets;
	public static Item tcNuggetChicken;
	public static Item tcNuggetBeef;
	public static Item tcNuggetPork;
	//----- Equivalent Exchange Blocks -------------------------
	//----- Equivalent Exchange Items --------------------------
	public static Item eeMinuimShard;
	//----- Ars Magica Blocks ----------------------------------
	public static Block amBlackOrchid;
	public static Block amDesertNova;
	//----- Ars Magica Items -----------------------------------
	public static Item amVinteumDust;
	public static Item amArcaneCompound;
	public static Item amEssenceArcane;
	public static Item amEssenceEarth;
	public static Item amEssenceWater;
	public static Item amEssenceFire;
	public static Item amEssenceAir;
	public static Item amEssenceMagma;
	public static Item amEssencePlant;
	public static Item amEssenceIce;
	public static Item amEssenceLightning;
	

	//----- Config State info ----------------------------------
	private Configuration configuration;
	
	public Config(File configFile)
	{
		this.configuration = new Configuration(configFile);
		this.configuration.load();
		this.doMiscConfig();
	}
	
	public void saveConfigs()
	{
		this.configuration.save();
	}

	public void setupBlocks()
	{
		ThaumcraftHelper.getBlocks();
		ForestryHelper.getBlocks();
		EquivalentExchangeHelper.getBlocks();
		ArsMagicaHelper.getBlocks();
		
		int blockIdBase = 1750;
		
		{
			int plankId = configuration.getBlock("planksTC", blockIdBase++).getInt();
			int slabFullId = configuration.getBlock("slabFull", blockIdBase++).getInt();
			int slabHalfId = configuration.getBlock("slabHalf", blockIdBase++).getInt();
			
			if (ThaumcraftHelper.isActive())
			{
				planksWood = new BlockPlanks(plankId);
				planksWood.setUnlocalizedName("planks");

				Item.itemsList[planksWood.blockID] = null;
		        Item.itemsList[planksWood.blockID] = new ItemMultiTextureTile(planksWood.blockID - 256, planksWood, PlankType.getAllNames());

				FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", MagicBees.getConfig().planksWood.blockID + "@" + PlankType.GREATWOOD.ordinal());
				FMLInterModComms.sendMessage("BuildCraft|Transport", "add-facade", MagicBees.getConfig().planksWood.blockID + "@" + PlankType.SILVERWOOD.ordinal());
		        
		        slabWoodFull = new BlockWoodSlab(slabFullId, true);
		        slabWoodHalf = new BlockWoodSlab(slabHalfId, false);
		        
		        slabWoodFull.setUnlocalizedName("planks");
		        slabWoodHalf.setUnlocalizedName("planks");

			    Item.itemsList[slabWoodHalf.blockID] = null;
			    Item.itemsList[slabWoodHalf.blockID] = new ItemSlab(slabWoodHalf.blockID - 256, slabWoodHalf, slabWoodFull, false);
			    Item.itemsList[slabWoodFull.blockID] = null;
			    Item.itemsList[slabWoodFull.blockID] = new ItemSlab(slabWoodFull.blockID - 256, slabWoodHalf, slabWoodFull, true);
			    
		        OreDictionary.registerOre("plankWood", new ItemStack(planksWood, 1, -1));
			    OreDictionary.registerOre("slabWood", new ItemStack(slabWoodHalf, 1, -1));
			}
		}
		
		effectJar = new BlockEffectJar(configuration.getBlock("effectJar", blockIdBase++).getInt());
		GameRegistry.registerBlock(effectJar, "effectJar");
		GameRegistry.registerTileEntity(TileEntityEffectJar.class, TileEntityEffectJar.tileEntityName);
		
		hive = new BlockHive(configuration.getBlock("hives", blockIdBase++).getInt());
		GameRegistry.registerBlock(hive, "hive");
		
		for (HiveType t : HiveType.values())
		{
			MinecraftForge.setBlockHarvestLevel(hive, t.ordinal(), "scoop", 0);
		}
		
		Item.itemsList[hive.blockID] = null;
		Item.itemsList[hive.blockID] = new ItemMultiTextureTile(hive.blockID - 256, hive, HiveType.getAllNames());
	}
	
	public void setupItems()
	{
		ThaumcraftHelper.getItems();
		ForestryHelper.getItems();
		EquivalentExchangeHelper.getItems();
		ArsMagicaHelper.getItems();
		
		int itemIDBase = 26090;

		combs= new ItemComb(configuration.getItem("combs", itemIDBase++).getInt() - 256);
		wax = new ItemWax(configuration.getItem("wax", itemIDBase++).getInt() - 256);		
		propolis = new ItemPropolis(configuration.getItem("propolis", itemIDBase++).getInt() - 256);
		drops = new ItemDrop(configuration.getItem("drop", itemIDBase++).getInt() - 256);
		miscResources = new ItemMiscResources(configuration.getItem("miscResources", itemIDBase++).getInt() - 256);
		
		// Make Aromatic Lumps a swarmer inducer. Chance is /1000.
		BeeManager.inducers.put(miscResources.getStackForType(ResourceType.AROMATIC_LUMP), 95);
		
		{
			int tier1 = configuration.getItem("thaumaturgePack1", itemIDBase++).getInt() - 256;
			int tier2 = configuration.getItem("thaumaturgePack2", itemIDBase++).getInt() - 256;
			
			if (ThaumcraftHelper.isActive())
			{
				try
				{
					// 0x8700C6 = purpleish.
					String backpackName = LocalizationManager.getLocalizedString("backpack.thaumaturge");
					BackpackDefinition def = new BackpackDefinition("thaumaturge", backpackName, 0x8700C6);
					thaumaturgeBackpackT1 = BackpackManager.backpackInterface.addBackpack(tier1, def, EnumBackpackType.T1);					
					thaumaturgeBackpackT2 = BackpackManager.backpackInterface.addBackpack(tier2, def, EnumBackpackType.T2);
					// Add additional items from configs to backpack.
					if (MagicBees.getConfig().ThaumaturgeExtraItems.length() > 0)
					{
						FMLLog.info("Attempting to add extra items to Thaumaturge's backpack. If you get an error, check your MagicBees.conf.");
						FMLInterModComms.sendMessage("Forestry", "add-backpack-items", "thaumaturge@" + MagicBees.getConfig().ThaumaturgeExtraItems);
					}
				}
				catch (Exception e)
				{
					FMLLog.severe("MagicBees encountered a problem during loading!");
					FMLLog.severe("Could not register backpacks via Forestry. Check your FML Client log and see if Forestry crashed silently.");
				}
			}
		}
		
		magicCapsule = new ItemCapsule(CapsuleType.MAGIC, configuration.getItem("magicCapsule", itemIDBase++).getInt() - 256, this.CapsuleStackSizeMax);
		pollen = new ItemPollen(configuration.getItem("pollen", itemIDBase++).getInt() - 256);
		
		{
			int crystalId = configuration.getItem("fluxCrystal", itemIDBase++).getInt() - 256;
			if (ThaumcraftHelper.isActive())
			{
				solidFlux = new ItemCrystalAspect(crystalId);
			}
		}
		
		hiveFrameMagic = new ItemMagicHiveFrame(configuration.getItem("frameMagic", itemIDBase++).getInt() - 256, HiveFrameType.MAGIC);
		hiveFrameResilient = new ItemMagicHiveFrame(configuration.getItem("frameResilient", itemIDBase++).getInt() - 256, HiveFrameType.RESILIENT);
		hiveFrameGentle = new ItemMagicHiveFrame(configuration.getItem("frameGentle", itemIDBase++).getInt() - 256, HiveFrameType.GENTLE);
		hiveFrameMetabolic = new ItemMagicHiveFrame(configuration.getItem("frameMetabolic", itemIDBase++).getInt() - 256, HiveFrameType.METABOLIC);
		hiveFrameNecrotic = new ItemMagicHiveFrame(configuration.getItem("frameNecrotic", itemIDBase++).getInt() - 256, HiveFrameType.NECROTIC);
		hiveFrameTemporal = new ItemMagicHiveFrame(configuration.getItem("frameTemporal", itemIDBase++).getInt() - 256, HiveFrameType.TEMPORAL);
		hiveFrameOblivion = new ItemMagicHiveFrame(configuration.getItem("frameOblivion", itemIDBase++).getInt() - 256, HiveFrameType.OBLIVION);
		// Future frames, so they all are clumped together.
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		itemIDBase++;
		
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(hiveFrameOblivion), 1, 1, 18));
		ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(hiveFrameOblivion), 1, 3, 23));

		jellyBaby = new ItemFood(configuration.getItem("jellyBabies", itemIDBase++).getInt() - 256, 1, false).setAlwaysEdible()
				.setPotionEffect(Potion.moveSpeed.id, 5, 1, 1f);
		jellyBaby.setUnlocalizedName(VersionInfo.ModName.toLowerCase() + ":jellyBabies");
		GameRegistry.registerItem(jellyBaby, VersionInfo.ModName.toLowerCase() + ":jellyBabies");
		
		
		voidCapsule = new ItemCapsule(CapsuleType.VOID, configuration.getItem("voidCapsule", itemIDBase++).getInt() - 256, this.CapsuleStackSizeMax);

		{
			int scoopID = configuration.getItem("thaumiumScoop", itemIDBase++).getInt() - 256;
			int grafterID = configuration.getItem("thaumiumGrafter", itemIDBase++).getInt() - 256;
			if (ThaumcraftHelper.isActive())
			{
				try {
					// Reflecting avoids the need to directly include the Thaumcraft API in the jar. BAM!
					Constructor ctor1 = Class.forName("magicbees.item.ItemThaumiumScoop").getConstructor(int.class);
					thaumiumScoop = (Item)ctor1.newInstance(scoopID);
					MinecraftForge.setToolClass(thaumiumScoop, "scoop", 3);
					GameRegistry.registerItem(thaumiumScoop, thaumiumScoop.getUnlocalizedName());
					
					Constructor ctor2 = Class.forName("magicbees.item.ItemThaumiumGrafter").getConstructor(int.class);
					thaumiumGrafter = (Item)ctor2.newInstance(grafterID);
					MinecraftForge.setToolClass(thaumiumGrafter, "grafter", 3);
					GameRegistry.registerItem(thaumiumGrafter, thaumiumGrafter.getUnlocalizedName());
				} catch (Exception e) { } 
			}
		}
		
		moonDial = new ItemMoonDial(configuration.getItem("moonDial", itemIDBase++).getInt() - 256);
		
		nuggets = new ItemNugget(configuration.getItem("beeNuggets", itemIDBase++).getInt() - 256);
		
		OreDictionary.registerOre("beeComb", new ItemStack(combs, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("waxMagical", wax.getStackForType(WaxType.MAGIC));
		OreDictionary.registerOre("waxMagical", wax.getStackForType(WaxType.AMNESIC));
		OreDictionary.registerOre("nuggetIron", nuggets.getStackForType(NuggetType.IRON));
		OreDictionary.registerOre("nuggetCopper", nuggets.getStackForType(NuggetType.COPPER));
		OreDictionary.registerOre("nuggetTin", nuggets.getStackForType(NuggetType.TIN));
		OreDictionary.registerOre("nuggetSilver", nuggets.getStackForType(NuggetType.SILVER));
		OreDictionary.registerOre("nuggetLead", nuggets.getStackForType(NuggetType.LEAD));
		OreDictionary.registerOre("shardDiamond", nuggets.getStackForType(NuggetType.DIAMOND));
		OreDictionary.registerOre("shardEmerald", nuggets.getStackForType(NuggetType.EMERALD));
	}
	
	private void doMiscConfig()
	{
		Property p;
		
		// Pull config from Forestry via reflection
		Field f;
		try
		{
			f = Class.forName("forestry.core.config.Config").getField("enableParticleFX");
			this.DrawParticleEffects = f.getBoolean(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		p = configuration.get("general", "backpack.thaumaturge.additionalItems", "");
		p.comment = "Add additional items to the Thaumaturge's Backpack." +
				"\n Format is the same as Forestry's: id:meta;id;id:meta (etc)";
		this.ThaumaturgeExtraItems = p.getString();
		
		p = configuration.get("general", "backpack.forestry.addThaumcraftItems", true);
		p.comment = "Set to true if you want MagicBees to add several Thaumcraft blocks & items to Forestry backpacks." +
				"\n Set to false to disable.";
		this.AddThaumcraftItemsToBackpacks = p.getBoolean(true);
		
		p = configuration.get("general", "capsuleStackSize", 64);
		p.comment = "Allows you to edit the stack size of the capsules in MagicBees if using GregTech, \n" +
				"or the reduced capsule size in Forestry & Railcraft. Default: 64";
		this.CapsuleStackSizeMax = p.getInt();
		
		p = configuration.get("general", "disableVersionNotification", false);
		p.comment = "Set to true to stop ThaumicBees from notifying you when new updates are available. (Does not supress critical updates)";
		this.DisableUpdateNotification = p.getBoolean(false);
		
		p = configuration.get("general", "areMagicPlanksFlammable", false);
		p.comment = "Set to true to allow Greatwood & Silverwood planks to burn in a fire.";
		this.AreMagicPlanksFlammable = p.getBoolean(false);
		
		p = configuration.get("general", "useImpregnatedStickInTools", false);
		p.comment = "Set to true to make Thaumium Grafter & Scoop require impregnated sticks in the recipe.";
		this.UseImpregnatedStickInTools = p.getBoolean(false);
		
		p = configuration.get("general", "moonDialShowText", false);
		p.comment = "set to true to show the current moon phase in mouse-over text.";
		this.MoonDialShowsPhaseInText = p.getBoolean(false);
		
		p = configuration.get("Retrogen", "doHiveRetrogen", false);
		p.comment = "Set to true to enable retroactive worldgen of Magic Bees hives.";
		this.DoHiveRetrogen = p.getBoolean(false);
		
		p = configuration.get("Retrogen", "forceHiveRegen", false);
		p.comment = "Set to true to force a regeneration of Magic Bees hives. Will set config option to false after parsed. (Implies doHiveRetrogen=true)";
		this.ForceHiveRegen = p.getBoolean(false);
		
		if (this.ForceHiveRegen)
		{
			FMLLog.info("Magic Bees will aggressively regenerate hives in all chunks for this game instance. Config option set to false.");
			p.set(false);
			this.DoHiveRetrogen = true;
		}
		else if (this.DoHiveRetrogen)
		{
			FMLLog.info("Magic Bees will attempt to regenerate hives in chunks that were generated before the mod was added.");
		}
	}

}