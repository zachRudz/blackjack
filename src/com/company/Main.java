package com.company;

import com.company.cards.Card;
import com.company.cards.Deck;
import com.company.cards.Hand;

public class Main {

    public static void main(String[] args) {
        Deck d = new Deck();
        d.shuffle();
        
        Hand h = new Hand();
        h.draw(d, 2);
        h.printCards();
        
        d.collectCards(h);
        d.shuffle();
	    h.printCards();
		
    }
}