/**
 * 
 */
package net.lahwran.zanminimap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;

/**
 * @author lahwran
 *
 */
public class Config {

    /** 
     * Whether to do anything. Is set at the beginning of each tick. Would normally
     * be passed around from where it's determined early in a render tick, but
     * the calculation thread needs to be able to see it.
     */
    public boolean enabled = true;

    /**
     * Config option - True hides the minimap but not coords.
     */
    public boolean hide = false;

    /** 
     * True shows the full screen map. 
     */
    public boolean full = false;

    /**
     * Current level of zoom
     */
    public int zoom = 2;

    /**
     * Config option - True to render a color minimap. False to render a black and white minimap.
     */
    public boolean color = true;

    /**
     * Config option - Zoom key setting
     */
    public int zoomKey = Keyboard.KEY_Z;

    /**
     * Config option - Menu key setting
     */
    public int menuKey = Keyboard.KEY_M;

    /**
     * Config option - True to display the map as square
     */
    public boolean squaremap = false;

    /**
     * Config option - True to show coordinates as text
     */
    public boolean coords = true;

    /**
     * Config option - True to show dynamic lighting
     */
    public boolean lightmap = true;

    /**
     * Config option - True to show terrain depth as black/white shading
     */
    public boolean heightmap = true;

    /**
     * Config option - True to show welcome message on startup
     */
    public boolean welcome = true;

    /**
     * Config option - should we be running the calc thread?
     */
    public boolean threading = false;

    /**
     * Config option - True to shift the waypoint display to locations they
     * belong in in the nether
     */
    public boolean netherpoints = false;

    /**
     * Config option - True to render the map as a cave map
     */
    public boolean cavemap = false;

    /**
     * Waypoint names and data
     */
    public ArrayList<Waypoint> wayPoints;
    
    /**
     * Color order used when cycling waypoint color
     */
    public ArrayList<Integer> colorsequence;

    /**
     * Block color array - contains 256 (blocks) * 16 (metadata) color slots.
     * @see getBlockColor
     * @see blockColorID
     */
    private final BlockColor[] blockColors = new BlockColor[4096];
    
    private final ZanMinimap minimap;

    /**
     * @param minimap minimap instance to use
     */
    public Config(ZanMinimap minimap) {
        this.minimap = minimap;
    }

    /**
     * turn a block ID and a metadata value into an index in blockColors
     * @param blockid block ID
     * @param meta metadata value
     * @return index in blockColors
     * @see blockColors
     * @see getBlockColor
     */

    final int blockColorID(int blockid, int meta)
    {
        return (blockid) | (meta << 8);
    }

    /**
     * Retrieve the BlockColor object for a block ID and block metadata.
     * @param blockid block ID
     * @param meta metadata value
     * @return BlockColor retrieved
     */

    final BlockColor getBlockColor(int blockid, int meta)
    {
        try
        {
            BlockColor col = blockColors[blockColorID(blockid, meta)];
            if (col != null) return col;
            col = blockColors[blockColorID(blockid, 0)];
            if (col != null) return col;
            col = blockColors[0];
            if (col != null) return col;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.err.println("BlockID: " + blockid + " - Meta: " + meta);
            throw e;
        }
        System.err.println("Unable to find a block color for blockid: " + blockid + " blockmeta: " + meta);
        return new BlockColor(0xff00ff, 0xff, TintType.NONE);
    }

    /**
     * Initialize everything there is to be initialized in config
     */
    
    public void initializeEverything() {
        try {
            readConfig();
        } catch (IOException e) {
            System.err.println("------- IOException while reading zanminimap config -------");
            e.printStackTrace();
        }
        initWaypointColors();
        initDefaultColors();
        try {
            readColors();
        } catch (IOException e) {
            System.err.println("------- IOException while reading zanminimap colors -------");
            e.printStackTrace();
        }
        try {
            writeColors();
        } catch (IOException e) {
            System.err.println("------- IOException while writing zanminimap colors -------");
            e.printStackTrace();
        }
        try {
            readAetherColors();
        } catch (IOException e) {
            System.err.println("------- IOException while reading zanminimap aether colors -------");
            e.printStackTrace();
        }
    }

    /**
     * initialize the waypoint color sequence
     */

    public void initWaypointColors() {
        int[] colorarrray = new int[] { 0xfe0000, 0xfe8000, 0xfefe00,
                0x80fe00, 0x00fe00, 0x00fe80, 0x00fefe, 0x0000fe, 0x8000fe,
                0xfe00fe, 0xfefefe, 0x7f0000, 0x7f4000, 0x7f7f00, 0x407f00,
                0x007f00, 0x007f40, 0x007f7f, 0x00007f, 0x40007f, 0x7f007f,
                0x7f7f7f
        };
        colorsequence = new ArrayList<Integer>();
        for (int colorvalue:colorarrray) {
            colorsequence.add(colorvalue);
        }
    }

    /**
     * Initialize the default colors in the block color array
     */
    
    public void initDefaultColors() {
        for (int i = 0; i < blockColors.length; i++)
            blockColors[i] = null;
    
        blockColors[blockColorID(0, 0)] = new BlockColor(0xff00ff, 0, TintType.NONE); //air
        int wood = 0xbc9862; //reused colors
        int water = 0x3256ff;
        int lava = 0xd96514;
        blockColors[blockColorID(1, 0)] = new BlockColor(0x686868, 0xff, TintType.NONE); //stone
        blockColors[blockColorID(2, 0)] = new BlockColor(0x74b44a, 0xff, TintType.GRASS); //grass
        blockColors[blockColorID(3, 0)] = new BlockColor(0x79553a, 0xff, TintType.NONE); //dirt
        blockColors[blockColorID(4, 0)] = new BlockColor(0x959595, 0xff, TintType.NONE); //cobble
        blockColors[blockColorID(5, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //wood
        blockColors[blockColorID(6, 0)] = new BlockColor(0xa2c978, 0x80, TintType.FOLIAGE); //sapling 1
        blockColors[blockColorID(6, 1)] = new BlockColor(0xa2c978, 0x80, TintType.PINE);    //sapling 2
        blockColors[blockColorID(6, 2)] = new BlockColor(0xa2c978, 0x80, TintType.BIRCH);   //sapling 3
        blockColors[blockColorID(7, 0)] = new BlockColor(0x333333, 0xff, TintType.NONE); //bedrock
        blockColors[blockColorID(8, 0)] = new BlockColor(water, 0xc0, TintType.NONE); //water
        blockColors[blockColorID(9, 0)] = new BlockColor(water, 0xb0, TintType.NONE); //moving water
        blockColors[blockColorID(10, 0)] = new BlockColor(lava, 0xff, TintType.NONE); //lava
        blockColors[blockColorID(11, 0)] = new BlockColor(lava, 0xff, TintType.NONE); //moving lava
        blockColors[blockColorID(12, 0)] = new BlockColor(0xddd7a0, 0xff, TintType.NONE); //sand
        blockColors[blockColorID(13, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //gravel
        blockColors[blockColorID(14, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //gold ore
        blockColors[blockColorID(15, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //iron ore
        blockColors[blockColorID(16, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //coal ore
        
        blockColors[blockColorID(17, 0)] = new BlockColor(0x675132, 0xff, TintType.NONE); //log 1
        blockColors[blockColorID(17, 1)] = new BlockColor(0x342919, 0xff, TintType.NONE); //log 2
        blockColors[blockColorID(17, 2)] = new BlockColor(0xc8c29f, 0xff, TintType.NONE); //log 3
        
        blockColors[blockColorID(18, 0)] = new BlockColor(0x164d0c, 0xa0, TintType.NONE); //leaf
        blockColors[blockColorID(19, 0)] = new BlockColor(0xe5e54e, 0xff, TintType.NONE); //sponge
        blockColors[blockColorID(20, 0)] = new BlockColor(0xffffff, 0x80, TintType.NONE); //glass
        blockColors[blockColorID(21, 0)] = new BlockColor(0x6d7484, 0xff, TintType.NONE); //lapis ore
        blockColors[blockColorID(22, 0)] = new BlockColor(0x1542b2, 0xff, TintType.NONE); //lapis
        blockColors[blockColorID(23, 0)] = new BlockColor(0x585858, 0xff, TintType.NONE); //dispenser
        blockColors[blockColorID(24, 0)] = new BlockColor(0xc6bd6d, 0xff, TintType.NONE); //sandstone
        blockColors[blockColorID(25, 0)] = new BlockColor(0x784f3a, 0xff, TintType.NONE); //noteblock
        blockColors[blockColorID(26, 0)] = new BlockColor(0xa95d5d, 0xff, TintType.NONE); //bed
        
        //skip 27, 28, 30, 31, and 32 as they are all nonsolid and
        //notch's height map skips them
        
        blockColors[blockColorID(35, 0)] = new BlockColor(0xe1e1e1, 0xff, TintType.NONE); //colored wool
        blockColors[blockColorID(35, 1)] = new BlockColor(0xeb8138, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 2)] = new BlockColor(0xc04cca, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 3)] = new BlockColor(0x698cd5, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 4)] = new BlockColor(0xc5b81d, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 5)] = new BlockColor(0x3cbf30, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 6)] = new BlockColor(0xda859c, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 7)] = new BlockColor(0x434343, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 8)] = new BlockColor(0x9fa7a7, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 9)] = new BlockColor(0x277697, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 10)] = new BlockColor(0x7f33c1, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 11)] = new BlockColor(0x26339b, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 12)] = new BlockColor(0x57331c, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 13)] = new BlockColor(0x384e18, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 14)] = new BlockColor(0xa52d28, 0xff, TintType.NONE);
        blockColors[blockColorID(35, 15)] = new BlockColor(0x1b1717, 0xff, TintType.NONE); //end colored wool
        
        blockColors[blockColorID(37, 0)] = new BlockColor(0xf1f902, 0xff, TintType.NONE); //yellow flower
        blockColors[blockColorID(38, 0)] = new BlockColor(0xf7070f, 0xff, TintType.NONE); //red flower
        blockColors[blockColorID(39, 0)] = new BlockColor(0x916d55, 0xff, TintType.NONE); //brown mushroom
        blockColors[blockColorID(40, 0)] = new BlockColor(0x9a171c, 0xff, TintType.NONE); //red mushroom
        blockColors[blockColorID(41, 0)] = new BlockColor(0xfefb5d, 0xff, TintType.NONE); //gold block
        blockColors[blockColorID(42, 0)] = new BlockColor(0xe9e9e9, 0xff, TintType.NONE); //iron block
        
        blockColors[blockColorID(43, 0)] = new BlockColor(0xa8a8a8, 0xff, TintType.NONE); //double slabs
        blockColors[blockColorID(43, 1)] = new BlockColor(0xe5ddaf, 0xff, TintType.NONE);
        blockColors[blockColorID(43, 2)] = new BlockColor(0x94794a, 0xff, TintType.NONE);
        blockColors[blockColorID(43, 3)] = new BlockColor(0x828282, 0xff, TintType.NONE);

        blockColors[blockColorID(44, 0)] = new BlockColor(0xa8a8a8, 0xff, TintType.NONE); //single slabs
        blockColors[blockColorID(44, 1)] = new BlockColor(0xe5ddaf, 0xff, TintType.NONE);
        blockColors[blockColorID(44, 2)] = new BlockColor(0x94794a, 0xff, TintType.NONE);
        blockColors[blockColorID(44, 3)] = new BlockColor(0x828282, 0xff, TintType.NONE);
        
        blockColors[blockColorID(45, 0)] = new BlockColor(0xaa543b, 0xff, TintType.NONE); //brick
        blockColors[blockColorID(46, 0)] = new BlockColor(0xdb441a, 0xff, TintType.NONE); //tnt
        blockColors[blockColorID(47, 0)] = new BlockColor(0xb4905a, 0xff, TintType.NONE); //bookshelf
        blockColors[blockColorID(48, 0)] = new BlockColor(0x1f471f, 0xff, TintType.NONE); //mossy cobble
        blockColors[blockColorID(49, 0)] = new BlockColor(0x101018, 0xff, TintType.NONE); //obsidian
        blockColors[blockColorID(50, 0)] = new BlockColor(0xffd800, 0xff, TintType.NONE); //torch
        blockColors[blockColorID(51, 0)] = new BlockColor(0xc05a01, 0xff, TintType.NONE); //fire
        blockColors[blockColorID(52, 0)] = new BlockColor(0x265f87, 0xff, TintType.NONE); //spawner
        blockColors[blockColorID(53, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //wood steps
        blockColors[blockColorID(54, 0)] = new BlockColor(0x8f691d, 0xff, TintType.NONE); //chest
        blockColors[blockColorID(55, 0)] = new BlockColor(0x480000, 0xff, TintType.NONE); //redstone wire
        blockColors[blockColorID(56, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //diamond ore
        blockColors[blockColorID(57, 0)] = new BlockColor(0x82e4e0, 0xff, TintType.NONE); //diamond block
        blockColors[blockColorID(58, 0)] = new BlockColor(0xa26b3e, 0xff, TintType.NONE); //craft table
        blockColors[blockColorID(59, 0)] = new BlockColor(0x00e210, 0xff, TintType.NONE); //crops
        blockColors[blockColorID(60, 0)] = new BlockColor(0x633f24, 0xff, TintType.NONE); //cropland
        blockColors[blockColorID(61, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //furnace
        blockColors[blockColorID(62, 0)] = new BlockColor(0x808080, 0xff, TintType.NONE); //furnace, powered
        blockColors[blockColorID(63, 0)] = new BlockColor(0xb4905a, 0xff, TintType.NONE); //fence
        blockColors[blockColorID(64, 0)] = new BlockColor(0x7a5b2b, 0xff, TintType.NONE); //door
        blockColors[blockColorID(65, 0)] = new BlockColor(0xac8852, 0xff, TintType.NONE); //ladder
        blockColors[blockColorID(66, 0)] = new BlockColor(0xa4a4a4, 0xff, TintType.NONE); //track
        blockColors[blockColorID(67, 0)] = new BlockColor(0x9e9e9e, 0xff, TintType.NONE); //cobble steps
        blockColors[blockColorID(68, 0)] = new BlockColor(0x9f844d, 0xff, TintType.NONE); //sign
        blockColors[blockColorID(69, 0)] = new BlockColor(0x695433, 0xff, TintType.NONE); //lever
        blockColors[blockColorID(70, 0)] = new BlockColor(0x8f8f8f, 0xff, TintType.NONE); //stone pressureplate
        blockColors[blockColorID(71, 0)] = new BlockColor(0xc1c1c1, 0xff, TintType.NONE); //iron door
        blockColors[blockColorID(72, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //wood pressureplate
        blockColors[blockColorID(73, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //redstone ore
        blockColors[blockColorID(74, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //glowing redstone ore
        blockColors[blockColorID(75, 0)] = new BlockColor(0x290000, 0xff, TintType.NONE); //redstone torch, off
        blockColors[blockColorID(76, 0)] = new BlockColor(0xfd0000, 0xff, TintType.NONE); //redstone torch, lit
        blockColors[blockColorID(77, 0)] = new BlockColor(0x747474, 0xff, TintType.NONE); //button
        blockColors[blockColorID(78, 0)] = new BlockColor(0xfbffff, 0xff, TintType.NONE); //snow
        blockColors[blockColorID(79, 0)] = new BlockColor(0x8ebfff, 0xff, TintType.NONE); //ice
        blockColors[blockColorID(80, 0)] = new BlockColor(0xffffff, 0xff, TintType.NONE); //snow block
        blockColors[blockColorID(81, 0)] = new BlockColor(0x11801e, 0xff, TintType.NONE); //cactus
        blockColors[blockColorID(82, 0)] = new BlockColor(0xbbbbcc, 0xff, TintType.NONE); //clay
        blockColors[blockColorID(83, 0)] = new BlockColor(0xa1a7b2, 0xff, TintType.NONE); //reeds
        blockColors[blockColorID(84, 0)] = new BlockColor(0xaadb74, 0xff, TintType.NONE); //record player
        blockColors[blockColorID(85, 0)] = new BlockColor(wood, 0xff, TintType.NONE); //fence
        blockColors[blockColorID(86, 0)] = new BlockColor(0xa25b0b, 0xff, TintType.NONE); //pumpkin
        blockColors[blockColorID(87, 0)] = new BlockColor(0x582218, 0xff, TintType.NONE); //netherrack
        blockColors[blockColorID(88, 0)] = new BlockColor(0x996731, 0xff, TintType.NONE); //slow sand
        blockColors[blockColorID(89, 0)] = new BlockColor(0xcda838, 0xff, TintType.NONE); //glowstone
        blockColors[blockColorID(90, 0)] = new BlockColor(0x732486, 0xff, TintType.NONE); //portal
        blockColors[blockColorID(91, 0)] = new BlockColor(0xa25b0b, 0xff, TintType.NONE); //jackolantern
    }

    /**
     * Read block colors from config file (if it exists)
     * @throws IOException any exceptions thrown by reading utilities
     */

    public void readColors() throws IOException {
        File settingsFile = new File(ObfHub.getAppDir("minecraft"), "minimapcolors_099");
        Pattern colorline = Pattern.compile("^([0-9]*)\\.([0-9]*): color=([0-9a-fA-F]*).alpha=([0-9a-fA-F]*) tint=(.*)$");
        if (settingsFile.exists())
        {
            BufferedReader in = new BufferedReader(new FileReader(settingsFile));
            String sCurrentLine;

            while ((sCurrentLine = in.readLine()) != null)
            {
                if (sCurrentLine.startsWith("#")) continue;
                Matcher match = colorline.matcher(sCurrentLine);
                // new
                if (match.matches())
                {
                    int id = Integer.parseInt(match.group(1));
                    int meta = Integer.parseInt(match.group(2));
                    int col = Integer.parseInt(match.group(3), 16);
                    int alpha = Integer.parseInt(match.group(4), 16);
                    TintType tint = TintType.get(match.group(5));
                    if (tint == null) tint = TintType.NONE;
                    blockColors[blockColorID(id, meta)] = new BlockColor(col, alpha, tint);
                }
                else
                {
                    // old
                    String[] curLine = sCurrentLine.split(":");
                    try
                    {
                        if (curLine[0].equals("Block") && curLine.length == 3)
                        {
                            int newcol = Integer.parseInt(curLine[2], 16);
                            int id = Integer.parseInt(curLine[1]);
                            if (getBlockColor(id, 0).color != newcol) // only act if it's not default
                                blockColors[blockColorID(id, 0)] = new BlockColor(newcol, 0xff, TintType.NONE);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                        // just keep on trucking ...
                    }
                }
            }
            
            in.close();
        }
    }

    /**
     * Write block colors to config file
     * @throws IOException any exceptions thrown while writing file
     */

    public void writeColors() throws IOException {
        File settingsFile = new File(ObfHub.getAppDir("minecraft"), "minimapcolors_099");
        PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
        out.print("#Available tints: ");
        TintType[] availtints = TintType.values();
        for (int i = 0; i < availtints.length; i++)
        {
            out.print(availtints[i].name());
            if (i < availtints.length - 1) out.print(", ");
        }
        out.println();
        out.println("#format: blockid.metadata: color=RRGGBB/alpha=AA tint=TINTTYPE");
        for (int key = 1; key < blockColors.length; key++)
        {
            if (blockColors[key] == null) continue;
            int meta = key >> 8;
            int id = key & 0xff;
            out.println("" + id + "." + meta + ": color=" + Integer.toHexString(blockColors[key].color) + "/alpha=" + Integer.toHexString(blockColors[key].alpha) + " tint=" + blockColors[key].tintType.name());
        }
        out.close();
    }


    /**
     * Check for the aethermod, and if found, initialize default colors for
     * aether blocks, read any values from the config, and write out and
     * reformat the file.
     * @throws IOException any exceptions thrown while reading or writing
     */
    public void readAetherColors() throws IOException {
        try {
            Class<?> aether = Class.forName("mod_Aether");
            HashMap<String,BlockColor> aetherColors = new HashMap<String,BlockColor>();
            for(Field f:aether.getDeclaredFields())
            {
                if (f.getName().startsWith("idBlock"))
                {
                    aetherColors.put(f.getName().substring("idBlock".length())+".0", new BlockColor(0xde00ff, 0xff, TintType.NONE));
                }
            }
            
            aetherColors.put("Aercloud.0", new BlockColor(0xf3f3f3, 0xa6, TintType.NONE));
            aetherColors.put("Aerogel.0", new BlockColor(0xc3c9e3, 0xbc, TintType.NONE));
            aetherColors.put("AetherDirt.0", new BlockColor(0x646f73, 0xff, TintType.NONE));
            aetherColors.put("AetherGrass.0", new BlockColor(0x71a583, 0xff, TintType.NONE));
            aetherColors.put("AetherPortal.0", new BlockColor(0x275dff, 0x89, TintType.NONE));
            aetherColors.put("AmbrosiumOre.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("AmbrosiumTorch.0", new BlockColor(0xaeac00, 0xff, TintType.NONE));
            aetherColors.put("ChestMimic.0", new BlockColor(0x8f691d, 0xff, TintType.NONE));
            aetherColors.put("DungeonStone.0", new BlockColor(0x797979, 0xff, TintType.NONE));
            aetherColors.put("DungeonStone.1", new BlockColor(0x9c8663, 0xff, TintType.NONE));
            aetherColors.put("DungeonStone.2", new BlockColor(0x9d8763, 0xff, TintType.NONE));
            aetherColors.put("EnchantedGravitite.0", new BlockColor(0xde76bd, 0xff, TintType.NONE));
            aetherColors.put("Enchanter.0", new BlockColor(0x393a2b, 0xff, TintType.NONE));
            aetherColors.put("GoldenOakLeaves.0", new BlockColor(0x393a2b, 0x9c, TintType.NONE));
            aetherColors.put("GoldenOakSapling.0", new BlockColor(0x968f38, 0xff, TintType.NONE));
            aetherColors.put("GravititeOre.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("Holystone.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("Icestone.0", new BlockColor(0x989c9c, 0xff, TintType.NONE));
            aetherColors.put("Incubator.0", new BlockColor(0x2a2a21, 0xff, TintType.NONE));
            aetherColors.put("LightDungeonStone.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("LockedDungeonStone.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("LockedLightDungeonStone.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("Log.0", new BlockColor(0x796945, 0xff, TintType.NONE));
            aetherColors.put("Pillar.0", new BlockColor(0xe3d3c2, 0xff, TintType.NONE));
            aetherColors.put("Plank.0", new BlockColor(0x555540, 0xff, TintType.NONE));
            aetherColors.put("Quicksoil.0", new BlockColor(0xccc67b, 0xff, TintType.NONE));
            aetherColors.put("SkyrootLeaves.0", new BlockColor(0x9db361, 0x9a, TintType.NONE));
            aetherColors.put("SkyrootSapling.0", new BlockColor(0x8da457, 0x4a, TintType.NONE));
            aetherColors.put("Trap.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            aetherColors.put("TreasureChest.0", new BlockColor(0x8f691d, 0xff, TintType.NONE));
            aetherColors.put("ZaniteOre.0", new BlockColor(0x9e9e9e, 0xff, TintType.NONE));
            
            File settingsFile = new File(ObfHub.getAppDir("minecraft"), "minimapcolors_aether");
            ArrayList<String> commentLines = new ArrayList<String>();
            Pattern colorline = Pattern.compile("^([^. ]*.[0-9][0-9]?): color=([0-9a-fA-F]*).alpha=([0-9a-fA-F]*) tint=(.*)$");
            if (settingsFile.exists())
            {
                BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                String sCurrentLine;
    
                while ((sCurrentLine = in.readLine()) != null)
                {
                    if (sCurrentLine.startsWith("#")) continue;
                    Matcher match = colorline.matcher(sCurrentLine);
                    // new
                    if (match.matches())
                    {
                        String id = match.group(1);
                        int col = Integer.parseInt(match.group(2), 16);
                        int alpha = Integer.parseInt(match.group(3), 16);
                        TintType tint = TintType.get(match.group(4));
                        if (tint == null) tint = TintType.NONE;
                        if(aetherColors.containsKey(id.split("\\.")[0]+".0"))
                        {
                            aetherColors.put(id, new BlockColor(col, alpha, tint));
                        }
                        else
                            commentLines.add("#aetherblock does not exist: "+sCurrentLine);
                    }
                    else
                    {
                        commentLines.add("#incorrect format: "+sCurrentLine);
                    }
                }
                
                in.close();
            }
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
            out.print("#Available tints: ");
            TintType[] availtints = TintType.values();
            for (int i = 0; i < availtints.length; i++)
            {
                out.print(availtints[i].name());
                if (i < availtints.length - 1) out.print(", ");
            }
            out.println();
            /*out.print("#Available aether blocks: ");
            List<String> sortedBlocks = asSortedList(aetherBlocks);+
            int printed = "#Available aether blocks: ".length();
            for (int i=0; i<sortedBlocks.size(); i++)
            {
                int lastprinted = printed;
                String thisone = sortedBlocks.get(i);
                printed += thisone.length();
                out.print(thisone);
                if (i < sortedBlocks.size() - 1)
                {
                    out.print(", ");
                    printed += 2;
                }
                if ((lastprinted % 80) + (printed-lastprinted) > 80 )
                {
                    out.print("\n#");
                    printed += 1;
                }
            }*/
            out.println("#format: AetherName.metadata: color=RRGGBB/alpha=AA tint=TINTTYPE");
            List<String> aethernames = asSortedList(aetherColors.keySet());
            for (String key:aethernames)
            {
                BlockColor val = aetherColors.get(key);
                out.println("" + key + ": color=" + Integer.toHexString(val.color) + "/alpha=" + Integer.toHexString(val.alpha) + " tint=" + val.tintType.name());
            }
            for(String line:commentLines)
            {
                out.println(line);
            }
            out.close();
            for(String key:aethernames)
            {
                String[] split = key.split("\\.");
                int id=aether.getDeclaredField("idBlock"+split[0]).getInt(null);
                blockColors[blockColorID(id, Integer.parseInt(split[1]))] = aetherColors.get(key);
            }
            System.out.println("zanminimap: aether found, loaded aether block colors");
        } catch (ClassNotFoundException c)
        {
            System.out.println("zanminimap: aether not found, not attempting to load aether block colors");
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            System.out.println("zanminimap: you seem to be running me in a sandbox that prevents me from accessing mod_Aether or one of it's fields. nice going ...");
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            System.out.println("zanminimap: you seem to be running me in a sandbox that prevents me from accessing mod_Aether or one of it's fields. nice going ...");
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            System.out.println("zanminimap: aether seems to have changed, bug lahwran about it! give him this error message:");
            e.printStackTrace();
        }
    }
    
    /**
     * Utility to convert a collection to a sorted list. Used in readAetherColors().
     * @param <T> Collection type
     * @param c Collection to sort
     * @return sorted collection
     */
    
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list);
      return list;
    }

    /**
     * Read configuration.
     * @throws IOException any exceptions thrown while reading
     */

    public void readConfig() throws IOException {
        File settingsFile = new File(ObfHub.getAppDir("minecraft"), "zan.settings");
        
        if (settingsFile.exists())
        {
            BufferedReader in = new BufferedReader(new FileReader(settingsFile));
            String sCurrentLine;
            while ((sCurrentLine = in.readLine()) != null)
            {
                String[] curLine = sCurrentLine.split(":");

                if (curLine[0].equals("Show Minimap"))
                    squaremap = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Show Coordinates"))
                    coords = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Dynamic Lighting"))
                    lightmap = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Terrain Depth"))
                    heightmap = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Welcome Message"))
                    welcome = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Zoom Key"))
                    zoomKey = Keyboard.getKeyIndex(curLine[1]);
                else if (curLine[0].equals("Menu Key"))
                    menuKey = Keyboard.getKeyIndex(curLine[1]);
                else if (curLine[0].equals("Threading"))
                    threading = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Color"))
                    color = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Netherpoints"))
                    netherpoints = Boolean.parseBoolean(curLine[1]);
                else if (curLine[0].equals("Cavemap"))
                    cavemap = Boolean.parseBoolean(curLine[1]);

                if(cavemap && !(lightmap ^ heightmap))
                {
                    lightmap = true;
                    heightmap = false;
                }

                if(cavemap)
                {
                    this.full = false;
                    this.zoom=1;
                    minimap.menu.error = "Cavemap zoom (2.0x)";
                }
            }
            in.close();
        }
    }

    /**
     * Save configuration. Any exceptions thrown are caught and printed.
     */
    void saveConfig()
    {
        try {
            File settingsFile = new File(ObfHub.getAppDir("minecraft"), "zan.settings");
    
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));
            out.println("Show Minimap:" + Boolean.toString(squaremap));
            out.println("Show Coordinates:" + Boolean.toString(coords));
            out.println("Dynamic Lighting:" + Boolean.toString(lightmap));
            out.println("Terrain Depth:" + Boolean.toString(heightmap));
            out.println("Welcome Message:" + Boolean.toString(welcome));
            out.println("Zoom Key:" + Keyboard.getKeyName(zoomKey));
            out.println("Menu Key:" + Keyboard.getKeyName(menuKey));
            out.println("Threading:" + Boolean.toString(threading));
            out.println("Color:" + Boolean.toString(color));
            out.println("Netherpoints:" + Boolean.toString(netherpoints));
            out.println("Cavemap:" + Boolean.toString(cavemap));
            out.close();
        } catch (IOException e) {
            minimap.obfhub.chatInfo("§EError Saving Settings");
            e.printStackTrace();
        }
    }

    /**
     * Save waypoints.
     */
    void saveWaypoints()
    {
        File settingsFile = new File(ObfHub.getAppDir("minecraft"), minimap.worldname + ".points");

        try
        {
            PrintWriter out = new PrintWriter(new FileWriter(settingsFile));

            for (Waypoint pt : wayPoints)
            {
                if (!pt.name.startsWith("^"))
                    out.println(pt.name + ":" + pt.x + ":" + pt.z + ":" + Boolean.toString(pt.enabled) + ":" + pt.red + ":" + pt.green + ":" + pt.blue);
            }

            out.close();
        }
        catch (Exception e)
        {
            minimap.obfhub.chatInfo("§EError Saving Waypoints");
            e.printStackTrace();
        }
    }

    /**
     * Reset waypoints and load from file
     */
    void loadWaypoints()
    {
        wayPoints = new ArrayList<Waypoint>();
        File settingsFile = new File(ObfHub.getAppDir("minecraft"), minimap.worldname + ".points");

        try
        {
            if (settingsFile.exists())
            {
                BufferedReader in = new BufferedReader(new FileReader(settingsFile));
                String sCurrentLine;

                while ((sCurrentLine = in.readLine()) != null)
                {
                    String[] curLine = sCurrentLine.split(":");

                    if (curLine.length == 4)
                        wayPoints.add(new Waypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), Boolean.parseBoolean(curLine[3])));
                    else
                        wayPoints.add(new Waypoint(curLine[0], Integer.parseInt(curLine[1]), Integer.parseInt(curLine[2]), Boolean.parseBoolean(curLine[3]), Float.parseFloat(curLine[4]), Float.parseFloat(curLine[5]), Float.parseFloat(curLine[6])));
                }

                in.close();
                minimap.obfhub.chatInfo("§EWaypoints loaded for " + minimap.worldname);
            }
            else
                minimap.obfhub.chatInfo("§EError: No waypoints exist for this world/server.");
        }
        catch (Exception local)
        {
            minimap.obfhub.chatInfo("§EError Loading Waypoints");
        }
    }
    
    /**
     * Increment zoom level
     */
    void nextZoom()
    {
        if (minimap.menu.fudge > 0) return;

        if (minimap.menu.iMenu != 0)
        {
            minimap.menu.iMenu = 0;

            if (minimap.obfhub.getMenu() != null) minimap.obfhub.setMenuNull();
        }
        else
        {
            if(cavemap)
            {
                this.full = false;
                minimap.menu.error = "Cavemap zoom ";
                if (this.zoom != 1)
                {
                    this.zoom = 1;
                    minimap.menu.error += "(2.0x)";
                }
                else
                {
                    this.zoom = 0;
                    minimap.menu.error += "(4.0x)";
                }
            }
            else
            {
                if (this.zoom == 3)
                {
                    if (!this.full)
                        this.full = true;
                    else
                    {
                        this.zoom = 2;
                        this.full = false;
                        minimap.menu.error = "Zoom Level: (1.0x)";
                    }
                }
                else if (this.zoom == 0)
                {
                    this.zoom = 3;
                    minimap.menu.error = "Zoom Level: (0.5x)";
                }
                else if (this.zoom == 2)
                {
                    this.zoom = 1;
                    minimap.menu.error = "Zoom Level: (2.0x)";
                }
                else
                {
                    this.zoom = 0;
                    minimap.menu.error = "Zoom Level: (4.0x)";
                }
            }
            minimap.mapcalc.timer = 500;
        }
        minimap.menu.fudge = 20;
    }
}
