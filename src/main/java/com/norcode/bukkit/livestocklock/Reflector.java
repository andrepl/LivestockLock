package com.norcode.bukkit.livestocklock;

import net.minecraft.server.v1_5_R3.EntityCreature;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.PathfinderGoal;
import net.minecraft.server.v1_5_R3.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R3.util.UnsafeList;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Reflector {

    LivestockLock plugin;
    Field goalSelectorField;
    Field aListField;
    Field bListField;
    Field lureItemIdField;
    Field speedField;
    Field requireLookField;

    public Reflector(LivestockLock plugin) {
        this.plugin = plugin;
        try {
            goalSelectorField = EntityLiving.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);
            aListField = PathfinderGoalSelector.class.getDeclaredField("a");
            aListField.setAccessible(true);
            bListField = PathfinderGoalSelector.class.getDeclaredField("b");
            bListField.setAccessible(true);
            lureItemIdField = net.minecraft.server.v1_5_R3.PathfinderGoalTempt.class.getDeclaredField("k");
            lureItemIdField.setAccessible(true);
            speedField = net.minecraft.server.v1_5_R3.PathfinderGoalTempt.class.getDeclaredField("b");
            speedField.setAccessible(true);
            requireLookField = net.minecraft.server.v1_5_R3.PathfinderGoalTempt.class.getDeclaredField("l");
            requireLookField.setAccessible(true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void claimAnimal(org.bukkit.entity.LivingEntity e) {
        EntityLiving entity = ((CraftLivingEntity) e).getHandle();
        List<PathfinderGoal> toAdd = new LinkedList<PathfinderGoal>();

        try {
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(entity);
            UnsafeList aList = (UnsafeList) aListField.get(goalSelector);
            UnsafeList bList = (UnsafeList) bListField.get(goalSelector);
            Iterator it = aList.iterator();
            while (it.hasNext()) {
                // TODO This is all wrong. stupid PathfinderGoalSelectorItem threw a wrench in the plan.
                // probably have to empty and repopulate the goalSelector, differently for each animal :\
//                try {
//                    goal = (PathfinderGoal) it.next();
//                } catch (ClassCastException ex) {
//                    continue;
//                }
//                if (goal instanceof net.minecraft.server.v1_5_R3.PathfinderGoalTempt) {
//                    net.minecraft.server.v1_5_R3.PathfinderGoalTempt tempt = (net.minecraft.server.v1_5_R3.PathfinderGoalTempt) goal;
//                    int itemId = (Integer) lureItemIdField.get(tempt);
//                    float speed = (Float) speedField.get(tempt);
//                    boolean requireLook = (Boolean) requireLookField.get(tempt);
//                    toAdd.add(new PathfinderGoalTempt(plugin, (EntityCreature) e, speed, itemId, requireLook));
//                    it.remove();
//                }
            }
            it = bList.iterator();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
