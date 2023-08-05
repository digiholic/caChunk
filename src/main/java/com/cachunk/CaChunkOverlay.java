package com.cachunk;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;


public class CaChunkOverlay extends OverlayPanel {
    private final Client client;
    private final CaChunkConfig config;
    private final CaChunkPlugin plugin;

    private final static String UNLOCKED_CHUNKS_STRING = "Chunks Unlocked:";
    private final static String CA_POINTS_STRING = "Combat Achievement Points:";
    private final static String AVAILABLE_CHUNKS = "Available Chunks:";
    private final static String[] STRINGS = new String[] {
            UNLOCKED_CHUNKS_STRING,
            CA_POINTS_STRING,
            AVAILABLE_CHUNKS,
    };

    @Inject
    private CaChunkOverlay(Client client, CaChunkConfig config, CaChunkPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setPriority(OverlayPriority.MED);
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "CAChunk Mode Overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        int unlockedChunks = plugin.getChunkUnlockCount();
        int caPoints = plugin.getCAPoints();
        int availableChunks = caPoints - unlockedChunks;

        panelComponent.getChildren().add(LineComponent.builder()
                .left(UNLOCKED_CHUNKS_STRING)
                .leftColor(getTextColor())
                .right(String.valueOf(unlockedChunks))
                .rightColor(getTextColor())
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left(CA_POINTS_STRING)
                .right(String.valueOf(caPoints))
                .build());


        panelComponent.getChildren().add(LineComponent.builder()
                .left(AVAILABLE_CHUNKS)
                .right(String.valueOf(availableChunks))
                .build());

        String[] valueStrings = new String[] {
                String.valueOf(unlockedChunks), String.valueOf(caPoints), String.valueOf(availableChunks)
        };
        panelComponent.setPreferredSize(new Dimension(
                getLongestStringWidth(STRINGS, graphics)
                        + getLongestStringWidth(valueStrings, graphics),
                0));

        return super.render(graphics);
    }

    private Color getTextColor() {
        if (plugin.getCAPoints() < plugin.getChunkUnlockCount()){
            return Color.RED;
        }
        return Color.WHITE;
    }

    private int getLongestStringWidth(String[] strings, Graphics2D graphics) {
        int longest = graphics.getFontMetrics().stringWidth("000000");
        for(String i: strings) {
            int currentItemWidth = graphics.getFontMetrics().stringWidth(i);
            if(currentItemWidth > longest) {
                longest = currentItemWidth;
            }
        }
        return longest;
    }
}
