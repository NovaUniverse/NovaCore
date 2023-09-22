package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent.Builder;

public class StaticText extends ScoreboardEntry {
	private String text;

	public StaticText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public Builder apply(Builder builder) {
		builder.addStaticLine(Component.text(text));
		return builder;
	}
}