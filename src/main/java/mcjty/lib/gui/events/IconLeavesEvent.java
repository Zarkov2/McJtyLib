package mcjty.lib.gui.events;

import mcjty.lib.gui.icons.IIcon;
import mcjty.lib.gui.widgets.IconHolder;

public interface IconLeavesEvent {

    /// Return false if you don't want the icon to go away
    boolean iconLeaves(IconHolder parent, IIcon icon);
}
