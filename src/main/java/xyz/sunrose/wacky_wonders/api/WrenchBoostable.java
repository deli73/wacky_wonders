package xyz.sunrose.wacky_wonders.api;

public interface WrenchBoostable {
	/**
	 * An interface for block entities which can be "boosted" by the pipe wrench!
	 *
	 * accelerate() should attempt to skip the given number of ticks of operation,
	 * and then return whether or not any time has been successfully skipped.
	 **/
	boolean accelerate(int ticks);

}
