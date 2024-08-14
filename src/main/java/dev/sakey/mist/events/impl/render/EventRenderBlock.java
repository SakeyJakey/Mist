package dev.sakey.mist.events.impl.render;

import dev.sakey.mist.events.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;

@Data
@AllArgsConstructor
public class EventRenderBlock extends Event {
	private Block block;
	private boolean hidden;

	public EventRenderBlock(Block block) {
		this.block = block;
	}

	// Block:shouldSideBeRendered
}
