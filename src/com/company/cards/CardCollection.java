package com.company.cards;
import java.util.Random;

/**
 * Created by zach on 20/05/17.
 */
public class CardCollection {
    // Can't use this constructor as is
    protected CardCollection() {}
    public CardCollection(int maxNumCards) { this.maxNumCards = maxNumCards; }
    
    
    // Print to stdout
    public void printCards() {
	    System.out.println("Cards: [" + numCards + " / " + maxNumCards + "]");
	    for(Card c : cards) {
            System.out.println(c);
        }
    }
	
	
	/*****************
	 * Card Operations
	 * @return
	 */
    // Peeks at the card at the top of the collection (LIFO)
	public Card peek() {
		if(numCards > 1)
			return cards[numCards - 1];
		
		return null;
	}
	
	// Peeks at the card at position k
    public Card peek(int position) {
        if(position >= numCards)
	        return null;
        
        return cards[position];
    }
    
    // Removes the card at the top of the deck, returns it
    public Card pop() {
        try {
            // Make sure we have a card to give away
            if(numCards < 1)
                throw new tooFewCardsInCollectionException();
    
            // Returning the top-most card
            Card tmp = cards[--numCards];   // Get the card at the top of the deck
            cards[numCards] = null;       // Null out the pointer in our decklist
            return tmp;
        } catch(Exception e){
            System.out.println(e);
	        return null;
        }
    }
    
    // Add a card to the top of the deck
    public void push(Card c) {
        try {
            // Make sure we have enough space to add a card to our collection
            if(numCards >= maxNumCards)
                throw new tooManyCardsInCollectionException(maxNumCards);
    
            cards[numCards++] = c;
        }  catch(Exception e) {
	        System.out.println(e);
        }
    }
    
    // Shuffle the deck in place
    public void shuffle() {
        Card tmp;
        Random rand = new Random();
        int randIndex;
        
        // Swap each index of the card collection with another card at a random index
        for(int i = 0; i < numCards; i++) {
            // Getting random index
            randIndex = rand.nextInt(numCards);
            
            // Swapping card
            tmp = cards[i];
            cards[i] = cards[randIndex];
            cards[randIndex] = tmp;
        }
    }
    
    public int getNumCards() {
        return numCards;
    }
    
    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }
    
    public int getMaxNumCards() {
        return maxNumCards;
    }
    
    public void setMaxNumCards(int maxNumCards) {
        this.maxNumCards = maxNumCards;
    }
    
    protected int numCards, maxNumCards;
    protected Card[] cards;
}
