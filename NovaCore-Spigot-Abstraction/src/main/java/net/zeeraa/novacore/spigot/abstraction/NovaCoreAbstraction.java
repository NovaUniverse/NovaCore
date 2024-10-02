package net.zeeraa.novacore.spigot.abstraction;

import org.bukkit.Bukkit;

/**
 * This is used to get the NMS version
 * 
 * @author Zeeraa
 */
public class NovaCoreAbstraction {

	public static String getNMSVersion() {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();

		String version = packageName.substring(packageName.lastIndexOf('.') + 1);
		if (version.equals("craftbukkit.")) version = ""; // Using paper on latest versions
		return version;
	}
}