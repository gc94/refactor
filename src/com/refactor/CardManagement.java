package com.refactor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CardManagement {

    private static List<Card> cards = new ArrayList<>();

    private CommandProcessor commandProcessor;

    public CardManagement(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public void performOperation() {
        switch (commandProcessor.getAction()) {
            case "CREATE":
                Card card = new Card();
                card.setActive(true);
                card.setBalance(BigDecimal.ZERO);
                card.setCurrency("EUR");
                card.setDateCreated(new Date());
                card.setIban(UUID.randomUUID().toString());
                card.setPin("1234");
                if(commandProcessor.getAdditionalParams().get("type") != null) {
                    if(commandProcessor.getAdditionalParams().get("type").toString().equalsIgnoreCase("BRONZE")) {
                        card.setType("BRONZE");
                        card.setTypeDescription("Bronze. This is the default card type.");
                    } else if(commandProcessor.getAdditionalParams().get("type").toString().equalsIgnoreCase("SILVER")) {
                        card.setType("SILVER");
                        card.setType(("Silver. This card type offers you extra features."));
                    } else if(commandProcessor.getAdditionalParams().get("type").toString().equalsIgnoreCase("GOLD")) {
                        card.setType("GOLD");
                        card.setTypeDescription("Gold. Having this card gives you discounts and other stuff.");
                    } else if(commandProcessor.getAdditionalParams().get("type").toString().equalsIgnoreCase("PLATINUM")) {
                        card.setType("PLATINUM");
                        card.setTypeDescription("Platinum. 50% discounts for every payment.");
                    } else {
                        card.setType("BRONZE");
                        card.setTypeDescription("BRONZE. This is the default card type.");
                    }
                }
                Long cardId = Long.valueOf(cards.size() + 1);
                card.setId(cardId);
                cards.add(card);
                System.out.println(String.format("Card with id %s was created.", cardId));
            break;
            case "DISABLE":
                if(commandProcessor.getAdditionalParams().get("cardId") != null) {
                    for(Card searchedCard : cards) {
                        if(searchedCard.getId().equals(Long.valueOf(commandProcessor.getAdditionalParams().get("cardId").toString()))) {
                            searchedCard.deactivate();
                        } else {
                            System.out.println("Unable to find the card you've search for!");
                        }
                    }
                } else {
                    System.out.println("Invalid card id!");
                }
            break;
        }
    }

    public Card getCard(Long id) {
        if(cards.size() > 0) {
            for (Card card : cards) {
                if(card.getId().equals(id) && card.isActive()) {
                    return card;
                }
            }
        }
        return null;
    }

    public boolean deleteCard(Long id) {
        if(cards.size() > 0) {
            for(Card card : cards) {
                if(card.getId().equals(id)) {
                    cards.remove(card);
                    return true;
                }
            }
        }
        return false;
    }

}
