package com.gaming.project.rdrcampaccountant.mapper;


import com.gaming.project.rdrcampaccountant.model.Actions;
import com.gaming.project.rdrcampaccountant.model.CampAction;
import com.gaming.project.rdrcampaccountant.model.dto.CampActionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampActionMapper {
    @Mapping(target = "actionValue", expression = "java(getValueFromDescription(campAction))")
    @Mapping(target = "actions", expression = "java(getActionFromDescription(campAction))")
    @Mapping(target = "item", expression = "java(getItemFromDescription(campAction))")
    CampActionDto fromPojoToDto(CampAction campAction);

    default Double getValueFromDescription(CampAction campAction) {
        String transString = "";
        if (campAction.getDescription().contains(Actions.DONATION.getValue())) {
            return getValueFromDescriptionDonationDelivery(campAction);
        } else if (campAction.getDescription().contains(Actions.DELIVERY.getValue())) {
            return getValueFromDescriptionDonationDelivery(campAction);
        } else if (campAction.getDescription().contains(Actions.WITHDRAWAL.getValue())) {
            return getValueFromOtherDollarSign(campAction);
        } else if (campAction.getDescription().contains(Actions.DEPOSIT.getValue())) {
            return getValueFromOtherDollarSign(campAction);
        } else if (campAction.getDescription().contains(Actions.ADD_TAX.getValue())) {
            return getValueFromOtherDollarSign(campAction);
        } else if (campAction.getDescription().contains(Actions.SALE.getValue())) {
            return getValueFromOtherDollarSign(campAction);
        } else if (campAction.getDescription().contains(Actions.DEPOSIT_ITEM.getValue())) {
            return getValueFromDepositWithdrawal(campAction);
        } else if (campAction.getDescription().contains(Actions.WITHDRAW_ITEM.getValue())) {
            return getValueFromDepositWithdrawal(campAction);
        } else {
            return (double) 0;
        }
    }

    default Actions getActionFromDescription(CampAction campAction) {
        if (campAction.getDescription().contains(Actions.DONATION.getValue())) {
            return Actions.DONATION;
        } else if (campAction.getDescription().contains(Actions.DELIVERY.getValue())) {
            return Actions.DELIVERY;
        } else if (campAction.getDescription().contains(Actions.WITHDRAWAL.getValue())) {
            return Actions.WITHDRAWAL;
        } else if (campAction.getDescription().contains(Actions.DEPOSIT.getValue())) {
            return Actions.DEPOSIT;
        } else if (campAction.getDescription().contains(Actions.ADD_TAX.getValue())) {
            return Actions.ADD_TAX;
        } else if (campAction.getDescription().contains(Actions.SALE.getValue())) {
            return Actions.SALE;
        } else if (campAction.getDescription().contains(Actions.DEPOSIT_ITEM.getValue())) {
            return Actions.DEPOSIT_ITEM;
        } else if (campAction.getDescription().contains(Actions.WITHDRAW_ITEM.getValue())) {
            return Actions.WITHDRAW_ITEM;
        } else {
            return Actions.FAIL_SALE_OR_DELIVERY;
        }
    }

    default String getItemFromDescription(CampAction campAction) {
        if (campAction.getDescription().contains(Actions.DEPOSIT_ITEM.getValue())) {
            return getItemDescription(campAction);
        }
        if (campAction.getDescription().contains(Actions.WITHDRAW_ITEM.getValue())) {
            return getItemDescription(campAction);
        }
        return "";
    }

    default String getItemDescription(CampAction campAction) {
        String transString = "";
        transString = campAction.getDescription().substring(0, campAction.getDescription().indexOf(" ID:"));
        return transString.substring(transString.lastIndexOf(":") + 2).replaceAll("[^a-zA-Z\s]", "");
    }

    default Double getValueFromDescriptionDonationDelivery(CampAction campAction) {
        String transString = "";
        transString = campAction.getDescription().substring(0, campAction.getDescription().indexOf(" ID:"));
        return Double.valueOf(transString.substring(transString.lastIndexOf(": ") + 2));
    }

    default Double getValueFromOtherDollarSign(CampAction campAction) {
        String transString = "";
        transString = campAction.getDescription().substring(0, campAction.getDescription().indexOf(" ID:"));
        return Double.valueOf(transString.substring(transString.lastIndexOf("$") + 1));
    }

    default Double getValueFromDepositWithdrawal(CampAction campAction) {
        String transString = campAction.getDescription().substring(0, campAction.getDescription().indexOf(" ID:"));
        return Double.valueOf(transString.replaceAll("[^0-9]", ""));
    }
}
