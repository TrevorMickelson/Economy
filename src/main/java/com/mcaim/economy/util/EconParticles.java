package com.mcaim.economy.util;

import com.mcaim.economy.EcoMain;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Objects;

/**
 * Spawns money sign above players
 * head because literally why not
 */
public class EconParticles {
    private static final String[][] bankNote = {
        {" -, -, -, -, -, -, -, -, _, X, _, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, _, X, _, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, X, X, X, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, X, X, X, X, X, X, X, -, -, -, -, -, -"},
        {" -, -, -, -, -, X, X, X, -, X, -, X, X, -, -, -, -, -, -"},
        {" -, -, -, -, -, X, X, -, -, X, -, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, X, X, X, -, X, -, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, X, X, X, X, X, X, X, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, -, X, -, X, X, X, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, -, X, -, -, X, X, -, -, -, -, -"},
        {" -, -, -, -, -, -, X, X, -, X, -, X, X, X, -, -, -, -, -"},
        {" -, -, -, -, -, -, X, X, X, X, X, X, X, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, X, X, X, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, -, -"},
        {" -, -, -, -, -, -, -, -, -, X, -, -, -, -, -, -, -, -, -"}
    };

    public static void spawnBankNoteParticle(Player player) {
        Location loc = player.getLocation();

        new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                if (time == 5)
                    cancel();

                double addX = 0.7;
                double addY = 3.5;
                double addZ = 0.7;
                int amount = 10;
                double particleSpace = 0.1;
                String[][] bluePrint = bankNote;

                double tempX = loc.getX() - particleSpace * bluePrint.length - particleSpace + addX;
                double x = tempX;
                double y = loc.clone().getY() + addY;
                double z = loc.getZ() + addZ;
                double angle = -((loc.getYaw() + 180.0F) / 59.0F);
                angle += ((loc.getYaw() < -180.0F) ? 3.25D : 2.985D);

                for (String[] aShape : bluePrint) {
                    for (String anAShape : Arrays.toString(aShape).split(", ")) {
                        if(anAShape.equalsIgnoreCase("X"))
                        {
                            // Set target location for particles
                            Location target = loc.clone();
                            target.setX(x);
                            target.setY(y);
                            target.setZ(z);

                            // Get back vector
                            Vector v = target.toVector().subtract(loc.toVector());
                            Vector v2 = getBackVector(loc);
                            v = rotateAroundAxisY(v, angle);
                            v2.setY(0).multiply(-0.5D);
                            loc.add(v);
                            loc.add(v2);

                            // Spawn particles
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.YELLOW, 0.3F);
                            (Objects.requireNonNull(loc.getWorld())).spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(), amount, 0, 0, 0, 0, dustOptions);

                            loc.subtract(v2);
                            loc.subtract(v);
                        }

                        x += particleSpace;

                    }

                    y -= particleSpace;
                    x = tempX;
                }

                time++;
            }
        }.runTaskTimerAsynchronously(EcoMain.getInstance(), 0L, 1L);
    }

    private static Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    private static Vector getBackVector(Location loc) {
        float newZ = (float)(loc.getZ() + Math.sin(Math.toRadians((loc.getYaw() + 90.0F))));
        float newX = (float)(loc.getX() + Math.cos(Math.toRadians((loc.getYaw() + 90.0F))));
        return new Vector(newX - loc.getX(), 0.0D, newZ - loc.getZ());
    }
}
