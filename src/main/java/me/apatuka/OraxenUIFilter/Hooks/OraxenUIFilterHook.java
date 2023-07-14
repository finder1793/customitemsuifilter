package me.apatuka.OraxenUIFilter.Hooks;

import io.lumine.mythic.lib.api.crafting.uifilters.UIFilter;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;

import io.lumine.mythic.lib.api.crafting.uimanager.UIFilterManager;
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

public class OraxenUIFilterHook implements UIFilter {
    @NotNull @Override public String getIdentifier() { return "ox"; }

    @Override public boolean matches(@NotNull ItemStack item, @NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check validity
        if (!isValid(argument, data, ffp)) { return false; }

        // Check counter matches
        if (cancelMatch(item, ffp)) { return false; }

        // Same material?
        if (argument.equals(OraxenItems.getIdByItem(item))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isValid(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {
        if (reg) { return true; }

        Optional<ItemBuilder> hasOraxenItem = OraxenItems.getOptionalItemById(argument);

        // Not Present?
        if (!hasOraxenItem.isPresent()) {

            FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.ERROR,
                    "No such oraxen item named '$u{0}$b'. ", argument);
            return false; }

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "ItemStack found, $svalidated$b. ");
        return true;
    }

    @NotNull
    @Override
    public ArrayList<String> tabCompleteArgument(@NotNull String current) {

        // Filtered
        return SilentNumbers.smartFilter(getOraxenItems(), current, true);
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
        Optional<ItemBuilder> hasOraxenItem = OraxenItems.getOptionalItemById(argument);

        // Thing
        ItemStack item = hasOraxenItem.get().build();

        FriendlyFeedbackProvider.log(ffp, FriendlyFeedbackCategory.SUCCESS,
                "Successfully generated $r{0}$b. ", SilentNumbers.getItemName(item));

        // Just simple like thay
        return new ItemStack(item);
    }

    @NotNull
    @Override
    public ItemStack getDisplayStack(@NotNull String argument, @NotNull String data, @Nullable FriendlyFeedbackProvider ffp) {

        // Check that its valid
        if (!isValid(argument, data, ffp)) { return ItemFactory.of(Material.BARRIER).name("\u00a7cInvalid OraxenItem \u00a7e" + argument).build(); }

        // Guaranteed to work
        Optional<ItemBuilder> hasOraxenItem = OraxenItems.getOptionalItemById(argument);

        // Thing
        ItemStack item = hasOraxenItem.get().build();

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
        Optional<ItemBuilder> hasOraxenItem = OraxenItems.getOptionalItemById(argument);

        // Description is thus
        return SilentNumbers.toArrayList("This item must be a $r" + SilentNumbers.getItemName(hasOraxenItem.get().build()) + "$b.");
    }

    @Override public boolean determinateGeneration() { return true; }

    static ArrayList<String> getOraxenItems() { return new ArrayList<>(Arrays.asList(OraxenItems.getItemNames())); }

    /*
     *  Tracking
     */

    @NotNull
    @Override
    public String getSourcePlugin() { return "MythicLib"; }

    @NotNull
    @Override
    public String getFilterName() { return "Oraxen"; }

    @NotNull
    @Override
    public String exampleArgument() { return "SKELETAL_CROWN"; }

    @NotNull
    @Override
    public String exampleData() { return "0"; }

    /**
     * Registers this filter onto the manager.
     */
    public static void register() {

        // Yes
        global = new OraxenUIFilterHook();
        UIFilterManager.registerUIFilter(global);
        reg = false;
    }
    private static boolean reg = true;

    /**
     * @return The general instance of this MMOItem UIFilter.
     */
    @NotNull public static OraxenUIFilterHook get() { return global; }
    static OraxenUIFilterHook global;
}