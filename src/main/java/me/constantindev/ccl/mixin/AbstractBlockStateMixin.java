package me.constantindev.ccl.mixin;

import me.constantindev.ccl.etc.base.Module;
import me.constantindev.ccl.etc.reg.ModuleRegistry;
import me.constantindev.ccl.module.RENDER.Freecam;
import me.constantindev.ccl.module.ext.XRAY;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    Module xray;
    Module freecam;

    @Inject(at = {@At("HEAD")}, method = "getLuminance", cancellable = true)
    public void getLuminace(CallbackInfoReturnable<Integer> cir) {
        if (xray == null) xray = ModuleRegistry.search(XRAY.class);
        if (xray.isEnabled()) {
            cir.setReturnValue(15);
        }
    }

    @Inject(at = {@At("TAIL")}, method = "isFullCube", cancellable = true)
    private void isFullCube(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (freecam == null) freecam = ModuleRegistry.search(Freecam.class);
        if (freecam.isEnabled()) {
            cir.setReturnValue(false);
        }
    }
}