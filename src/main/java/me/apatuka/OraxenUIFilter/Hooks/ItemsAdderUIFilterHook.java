package me.apatuka.OraxenUIFilter.Hooks;

import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import dev.lone.itemsadder.api.ItemsAdder;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import io.lumine.mythic.core.items.MythicItem;
import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.ItemFactory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackCategory;
import io.lumine.mythic.lib.api.util.ui.FriendlyFeedbackProvider;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 *  The filter to match a mythicmobs item.
 *
 *  @author Gunging
 */
public class ItemsAdderUIFilterHook implements UIFilter {
    @NotNull @Override public String getIdentifier() { return "ia"; }

    @Override public boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check validity
        if (!isValid(argument, data, ffp)) { return false; }

        // Check counter matches
        if (cancelMatch(item, ffp)) { return false; }

        if(CustomStack.byItemStack(item) == null)
            return false;

        // Same material?
        if (argument.equals(CustomStack.byItemStack(item).getNamespacedID())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {
        if (reg) { return true; }
        ItemStack item = CustomStack.getInstance(argument).getItemStack();

        // Not Present?
        if (item == null) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No such ItemsAdder item named '$u{0}$b'. ", argument);
            return false; }

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "ItemStack found, $svalidated$b. ");
        return true;
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteArgument(@NotNull String current) {

        // Filtered
        return SilentNumbers.smartFilter(getIAItems(), current, true);
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteData(@NotNull String argument, @NotNull String current) {

        // Data is not supported
        return SilentNumbers.toArrayList("0", "(this_is_not_checked,_write_anything)");
    }

    @Override
    public boolean fullyDefinesItem() { return true; }

    @Nullable
    @Override
    public ItemStack getItemStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return null; }

        // Guaranteed to work
        ItemStack item = CustomStack.getInstance(argument).getItemStack();

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "Successfully generated $r{0}$b. ", SilentNumbers.getItemName(item));

        // Just simple like thay
        return new ItemStack(item);
    }

    @NotNull
    @Override
    public ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return ItemFactory.of(Material.BARRIER).name("\u00a7cInvalid ItemsAdder Item \u00a7e" + argument).build(); }

        // Guaranteed to work
        ItemStack item = CustomStack.getInstance(argument).getItemStack();

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "Successfully generated $r{0}$b. ", SilentNumbers.getItemName(item));

        // Just simple like thay
        return new ItemStack(item);
    }

    @NotNull
    @Override
    public ArrayList<String> getDescription(@NotNull String argument, @NotNull String data) {

        // Check validity
        if (!isValid(argument, data, null)) { return SilentNumbers.toArrayList("This mythic type is $finvalid$b."); }

        // Guaranteed to work
        ItemStack item = CustomStack.getInstance(argument).getItemStack();

        // Description is thus
        return SilentNumbers.toArrayList("This item must be a $r" + SilentNumbers.getItemName(item) + "$b.");
    }

    @Override public boolean determinateGeneration() { return true; }

    static ArrayList<String> getIAItems() { return new ArrayList<>(CustomStack.getNamespacedIdsInRegistry()); }

    /*
     *  Tracking
     */

    @NotNull
    @Override
    public String getSourcePlugin() { return "ItemsAdder"; }

    @NotNull
    @Override
    public String getFilterName() { return "ItemsAdder"; }

    @NotNull
    @Override
    public String exampleArgument() { return "GRASS_BLOCK"; }

    @NotNull
    @Override
    public String exampleData() { return "0"; }

    /**
     * Registers this filter onto the manager.
     */
    public static void register() {

        // Yes
        global = new ItemsAdderUIFilterHook();
        UIFilterManager.registerUIFilter(global);
        reg = false;
    }
    private static boolean reg = true;

    /**
     * @return The general instance of this MMOItem UIFilter.
     */
    @NotNull public static ItemsAdderUIFilterHook get() { return global; }
    static ItemsAdderUIFilterHook global;
}