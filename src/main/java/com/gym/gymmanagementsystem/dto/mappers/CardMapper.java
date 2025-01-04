package com.gym.gymmanagementsystem.dto.mappers;

import com.gym.gymmanagementsystem.dto.CardDto;
import com.gym.gymmanagementsystem.entities.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    // Převod z CardDto na Card entitu
    public Card toEntity(CardDto dto) {
        if (dto == null) {
            return null;
        }

        Card card = new Card();
        card.setCardID(dto.getCardID());
        card.setCardNumber(dto.getCardNumber());
        card.setLost(dto.getLost());
        card.setCardType(dto.getCardType());
        return card;
    }

    // Převod z Card entity na CardDto
    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }

        CardDto dto = new CardDto();
        dto.setCardID(card.getCardID());
        dto.setCardNumber(card.getCardNumber());
        dto.setLost(card.getLost());
        dto.setCardType(card.getCardType());
        return dto;
    }
}
