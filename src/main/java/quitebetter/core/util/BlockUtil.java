package quitebetter.core.util;

import net.minecraft.core.util.helper.Direction;

public class BlockUtil {
	public static class Stairs {
		public static final int MASK_HDIRECTION = 0b00000011;
		public static final int MASK_VDIRECTION = 0b00001000;

		public static class StairsDirection {
			public Direction hDir;
			public Direction vDir;

			@Override
			public boolean equals(Object obj) {
				return obj instanceof StairsDirection
					&& ((StairsDirection) obj).hDir.equals(this.hDir)
					&& ((StairsDirection) obj).vDir.equals(this.vDir);
			}

			public StairsDirection(Direction hDir, Direction vDir) {
				this.hDir = hDir;
				this.vDir = vDir;
			}
		}

		public static StairsDirection getDirection(int meta) {
			return new StairsDirection(getHDir(meta),getVDir(meta));
		}

		public static int getMeta(StairsDirection dir) {
			int meta = 0;
			int hd;
			switch (dir.hDir) {
				case NORTH: hd = 2; break;
				case EAST: hd = 1; break;
				case SOUTH: hd = 3; break;
				case WEST: hd = 0; break;
				default: hd = 0;
			}
			meta |= hd;
			switch (dir.vDir) {
				case UP: hd = 8; break;
				case DOWN: hd = 0; break;
				default: hd = 0;
			}
			meta |= hd;
			return meta;
		}

		public static Direction getHDir(int meta) {
			switch (meta & MASK_HDIRECTION) {
				case 2: return Direction.NORTH;
				case 1: return Direction.EAST;
				case 3: return Direction.SOUTH;
				case 0: return Direction.WEST;
				default: return Direction.NONE;
			}
		}
		public static Direction getVDir(int meta) {
			return ((meta & MASK_VDIRECTION) == 8) ? Direction.UP : Direction.DOWN;
		}

	}
}
