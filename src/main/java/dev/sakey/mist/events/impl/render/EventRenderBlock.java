package dev.sakey.mist.events.impl.render;

import dev.sakey.mist.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;

@AllArgsConstructor
public class EventRenderBlock extends Event {
	@Getter
	@Setter
	private Block block;
	@Getter
	@Setter
	private boolean hidden;

	public EventRenderBlock(Block block) {
		this.block = block;
	}

	// Block:shouldSideBeRendered
}
