package com.aegisLan.weather.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;

/**
 * Created by AegisLan on 2016.1.31.
 */
public class CityCardAdapter extends CardArrayAdapter {
    Map<String, Card> mCardMap;

    public CityCardAdapter(Context context, List<Card> cards) {
        super(context, cards);
        mCardMap = new HashMap<>();
        for (Card card : cards) {
            mCardMap.put(card.getTitle(),card);
        }
    }

    public Card getCarByTag(String tag) {
        return mCardMap.get(tag);
    }

    @Override
    public void add(Card card) {
        super.add(card);
        mCardMap.put(card.getTitle(), card);
    }

    @Override
    public void addAll(Collection<? extends Card> cardCollection) {
        super.addAll(cardCollection);
        mCardMap.clear();
        for (Card card : cardCollection) {
            mCardMap.put(card.getTitle(),card);
        }
    }

    @Override
    public void addAll(Card... cards) {
        super.addAll(cards);
        mCardMap.clear();
        for (Card card : cards) {
            mCardMap.put(card.getTitle(),card);
        }
    }

    @Override
    public void clear() {
        super.clear();
        mCardMap.clear();
    }

    @Override
    public void insert(Card card, int index) {
        super.insert(card, index);
        mCardMap.put(card.getTitle(), card);
    }
}
