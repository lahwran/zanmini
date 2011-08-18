package deobf;
import deobf.*;
import net.lahwran.zanminimap.ZanMinimap;
import net.minecraft.client.Minecraft;

/**
 * Modloader initialization so that the mod can be used as a modloader plugin
 * 
 * @author lahwran
 */
public class mod_ZanStarter extends BaseMod {

    public ZanMinimap minimap;

    public mod_ZanStarter() {
        boolean needmodloader = true;
        try {
            uq.class.getDeclaredField("minimap");
            needmodloader = false;
            System.out.println("ZanMinimap: found modloader and GuiIngame hook: using GuiIngame hook");
        } catch (SecurityException e) {
            e.printStackTrace();
            System.out.println("ZanMinimap: found modloader, unable to check for GuiIngame hook: using modloader");
        } catch (NoSuchFieldException e) {
            System.out.println("ZanMinimap: found modloader, did not find GuiIngame hook: using modloader");
        }

        if (needmodloader) {
            this.minimap = new ZanMinimap();
            ModLoader.SetInGameHook(this, true, false);
        }
    }

    public boolean OnTickInGame(Minecraft mc) {
        this.minimap.onRenderTick(mc);
        return false;
    }

    @Override
    public String Version() {
        return ZanMinimap.zmodver + " (for minecraft " + ZanMinimap.mcvers+")";
    }

}
