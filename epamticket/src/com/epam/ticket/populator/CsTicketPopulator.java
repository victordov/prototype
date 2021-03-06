package com.epam.ticket.populator;

import com.epam.dto.EpamTicket;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class CsTicketPopulator implements Populator<EpamTicket, CsTicketModel> {

    private DateFormat dateFormatter;

    public CsTicketPopulator(DateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public void populate(EpamTicket source, CsTicketModel target) throws ConversionException {
        checkNotNull(source, "Source model should not be null");
        target.setTicketID(source.getTicketId());
        //TODO populate User,Agent and Group. Cover with test
        /*UserModel customer = new UserModel();
        customer.setUid(source.getCustomerUid());
        customer.setName(source.getCustomerDisplayName());
        target.setCustomer(customer);*/

        AbstractOrderModel order = new AbstractOrderModel();

        order.setCode(source.getOrder());
        target.setOrder(order);

        target.setCategory(CsTicketCategory.valueOf(source.getCategory()));
        target.setPriority(CsTicketPriority.valueOf(source.getPriority()));
        target.setState(CsTicketState.valueOf(source.getState()));

        /*EmployeeModel agent = new EmployeeModel();
        agent.setUid(source.getAssignedAgent());
        target.setAssignedAgent(agent);

        CsAgentGroupModel group = new CsAgentGroupModel();
        group.setUid(source.getAssignedGroup());
        target.setAssignedGroup(group);*/

        target.setHeadline(source.getHeadline());
        target.setCreationtime(convertDate(source.getCreationTime()));
        target.setModifiedtime(convertDate(source.getModifyTime()));
        target.setCategory(CsTicketCategory.valueOf(source.getCategory()));
        target.setPriority(CsTicketPriority.valueOf(source.getPriority()));
        target.setHeadline(source.getHeadline());
    }

    private Date convertDate(String stringDate) {
        try {
            return dateFormatter.parse(stringDate);
        } catch (ParseException e) {
            throw new IllegalStateException("Cannot convert date: " + stringDate);
        }
    }

}
