package iss.team6.thememorygame;

import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {
    private Card [][] shuffleCards = new Card[4][3];
    private int state=0;//to judge if players can swap card or not
    private Card firstSwappedCard;
    private Card secondSwappedCard;
    private int points;

    public GameLogic(String[] imageNameList) {
        initCard(imageNameList);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Card getFirstSwappedCard() {
        return firstSwappedCard;
    }

    public void setFirstSwappedCard(Card firstSwappedCard) {
        this.firstSwappedCard = firstSwappedCard;
    }

    public void initCard(String[] imageNameList)
    {
        ArrayList<Card> cards=new ArrayList<Card>();
        cards.clear();
        int id = 0;
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<imageNameList.length;j++)
            {
                Card card=new Card();
                card.setImageName(imageNameList[j]);
                card.setImageId(id++);
                cards.add(card);
            }
        }
        Collections.shuffle(cards);
        for(int i=0;i<cards.size();i++)
        {
            Card temp= cards.get(i);
            temp.setX(i/shuffleCards[0].length);
            temp.setY(i% shuffleCards[0].length);
            shuffleCards[i/shuffleCards[0].length][i% shuffleCards[0].length]= temp;
        }
    }

    public  Card getCardByPosition(int x,int y)
    {
        Card card=shuffleCards[x][y];
        return card;
    }

    public boolean canSwapCard(int x,int y)
    {
        Card card =getCardByPosition(x,y);
        if(card.isSwapped())
        {
            return false;
        }
        if(card.isRemoved())
        {
            return false;
        }
        if(state==0 || state==1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Card[] swapAndMatchCard(int x, int y)
    {
        Card[] cards;
        Card card=getCardByPosition(x,y);
        if(state ==0)
        {
            firstSwappedCard=card;
            firstSwappedCard.swapCard();
            state=1;
            cards=new Card[]{firstSwappedCard};
            return cards;
        }
        else if(state==1)
        {
            secondSwappedCard = card;
            if(firstSwappedCard.getImageName().equals(card.getImageName()))
            {
                firstSwappedCard.removeCard();
                card.removeCard();
                card.swapCard();
                state=2;
                cards=new Card[]{firstSwappedCard,card};
                points=points+1;
                return cards;
            }
            else
            {
                card.swapCard();
                state=2;
                cards=new Card[]{firstSwappedCard, card};
                return cards;
            }
        }
        return null;
    }

    public void resetState() {
        state = 0;
        if (firstSwappedCard != null && !firstSwappedCard.isRemoved()) {
            firstSwappedCard.setSwapped(false);
            firstSwappedCard = null;
        }
        if (secondSwappedCard != null && !secondSwappedCard.isRemoved()) {
            secondSwappedCard.setSwapped(false);
            secondSwappedCard = null;
        }
    }

    public String getUpdatedScore()
    {
        String temp=Integer.toString(points);
        String marks=temp+" of 6 matches";
        return marks;
    }

    public Boolean isGameEnded()
    {
        if(points==6)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
