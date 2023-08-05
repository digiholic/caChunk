package com.cachunk;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "CAChunk"
)
public class CaChunkPlugin extends Plugin
{
	public static final int CA_POINTS_TOTAL = 14815;

	private int unlockedChunks;
	private int caPoints;
	private boolean isLoggedIn;

	@Inject
	private Client client;
	@Inject
	private CaChunkConfig config;
	@Inject
	private ConfigManager configManager;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private CaChunkOverlay infoOverlay;
	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(infoOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(infoOverlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			isLoggedIn = true;
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
			caPoints = getCAPoints();
			unlockedChunks = getChunkUnlockCount();
		} else if (gameStateChanged.getGameState() == GameState.LOGIN_SCREEN){
			isLoggedIn = false;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick t)
	{
		if (isLoggedIn){
			caPoints = getCAPoints();
			unlockedChunks = getChunkUnlockCount();
		}
	}

	@Provides
	CaChunkConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CaChunkConfig.class);
	}

	private String getChunkUnlockString(){
		return configManager.getConfiguration("regionlocker", "unlockedRegions");
	}

	public int getChunkUnlockCount(){
		String csvChunkString = getChunkUnlockString();
		List<String> chunkStrings = Text.fromCSV(csvChunkString);
		return chunkStrings.size();
	}

	public int getCAPoints(){
		return client.getVarbitValue(CA_POINTS_TOTAL);
	}
}
