package net.pkhsolutions.idispatch.dws.ui.data;

import net.pkhsolutions.idispatch.entity.TicketType;

/**
 * @author Petter Holmström
 */
public class TicketTypeItem extends BuilderItem<TicketType, TicketType.Builder> {

    public TicketTypeItem() {
        super(TicketType.class, TicketType.Builder.class);
    }
}
