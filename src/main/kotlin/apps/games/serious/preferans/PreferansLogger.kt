package apps.games.serious.preferans

import apps.games.GameExecutionException
import apps.games.primitives.EncryptedDeck
import com.sun.org.apache.bcel.internal.classfile.Unknown
import org.apache.commons.codec.digest.DigestUtils
import java.math.BigInteger

/**
 * Created by user on 7/15/16.
 */

class RoundLogger(val N: Int,val  DECK_SIZE: Int,val  TALON_SIZE: Int){
    //map: position in shuffled Deck and user -> keys
    private val keyMap = Array(N, {i -> Array<BigInteger?>(DECK_SIZE, {j ->
        null})})

    private var gameType: Bet = Bet.UNKNOWN

    private var talon = Array(TALON_SIZE, {i -> getCardById32(-1)})
    private var talonDiscard = Array(TALON_SIZE, {i -> getCardById32(-1)})

    //todo - check talon. only main player can play it's cards

    private var fail: Boolean = false

    // log of plays <user, card>
    private val log = mutableListOf<Pair<Int, Card>>()

    //who starts turn
    private val mainPlayer: Int = -1

    var enforcedSuit = Suit.UNKNOWN

    lateinit var gameBet: Bet


    /**
     * Register that
     * @param userID - user with this ID
     * @param card - holds for this card
     * (position in shuffled deck)
     * @param key - has that key
     */
    fun registerCardKey(userID: Int, card: Int, key: BigInteger){
        keyMap[userID][card] = key
    }

    /**
     * Assuming N - is the number of players, register
     * set of N Keys - cards played starting from the
     * first player of this round(one, who goes first)
     * @param plays - set of plays, starting from the
     * first player. Pair<Int, Int> . First item - playerID
     * second - CardId
     *
     * @return id of the player who starts next round
     * @throws GameExecutionException - if someone played
     * inconsistent with rules
     */
    fun registerPlay(play: Pair<Int, Int>): Int? {
        log.add(play.first to getCardById32(play.second))
        //If current turn has ended - update
        if(log.isNotEmpty() && log.size % N == 0){
            val plays = log.slice(log.size-N..log.size-1)
            val maxCard = maxWithTrump(plays.map { x -> x.second }, gameBet
                    .trump, enforcedSuit)
            enforcedSuit = Suit.UNKNOWN
            return plays.first { x -> x.second == maxCard }.first
        }
        //first play in turn - update enforced Bet
        if(log.size % N == 1){
            enforcedSuit = getCardById32(play.second).suit
        }
        return (play.first + 1) % N
    }

    /**
     * Register Talon cards. Update enforced bet
     * if needed
     * @param talonCards - list of talon cards
     */
    fun registerTalon(talonCards: List<Int>){
        if(talonCards.size != TALON_SIZE){
            throw GameExecutionException("Invalid talon")
        }
        for(i in 0..TALON_SIZE-1){
            talon[i] = getCardById32(talonCards[i])
        }
    }

    fun updateBet(bet: Bet){
        gameBet = bet
        if(gameBet == Bet.PASS){
            enforcedSuit = talon[0].suit
        }
    }

    /**
     * @return - true if new round just started
     * (awaiting first turn)
     */
    fun newTurnStarted(): Boolean{
        return log.size % N == 0
    }


    /**
     * get selector that checks, wether card is enforced
     * or trump
     */
    fun getEnforcedSelector(): (Card) -> (Boolean){
        return { x ->
            val trumpSuit = gameBet.trump
            (enforcedSuit != Suit.UNKNOWN) &&
            (x.suit == enforcedSuit || x.suit == trumpSuit)
        }
    }

    /**
     * Take a collection of cards. Filter cards, that are playable
     * ont the next turn
     * @param cards - mutable collection of cards to filter
     */

    fun filterPlayableCards(cards: MutableCollection<Card>){
        if(!cards.any(getEnforcedSelector())){
            return
        }
        if(cards.any { x -> x.suit == enforcedSuit }){
            cards.retainAll {x -> x.suit == enforcedSuit}
        }else{
            cards.retainAll{ x -> x.suit == gameBet.trump }
        }
    }

    /**
     * check if tound is finished(all cards are played)
     */
    fun roundFinished(): Boolean{
        return (TALON_SIZE + log.size) == DECK_SIZE
    }

    /**
     * Check, that every player submitted allowed card for each turn. This
     * is only verifyable at the end, when all plays(keys) are known
     */
    fun verifyRoundPlays(): Boolean{
        if(!roundFinished()){
            return false
        }
        val playedCards = mutableMapOf<Int, MutableSet<Card>>()
        for(entry in log){
            if(playedCards[entry.first] == null){
                playedCards[entry.first] = mutableSetOf()
            }
            playedCards[entry.first]!!.add(entry.second)
        }
        for(i in 0..DECK_SIZE-TALON_SIZE step N){
            val turn = log.slice(i..i+N-1)
            if(turn.distinctBy { x -> x.first }.size != N){
                return false
            }
            val enforsedSuit: Suit = turn[0].second.suit
            for(j in 1..N){
                val player = turn[j].first
                val suit = turn[j].second.suit
                if(!playedCards.containsKey(player)){
                    return false
                }
                if(suit != enforsedSuit){
                    if(playedCards[player]!!.any { x -> x.suit == enforsedSuit }){
                        return false
                    }
                    if(suit != gameBet.trump && playedCards[player]!!.any { x ->
                                                        x.suit == gameBet.trump }){
                        return false
                    }
                }
                playedCards[player]!!.remove(turn[j].second)
            }
        }
        return true
    }

    /**
     * Calculate discarded talon
     * @return list of talon card positions in original deck
     * null - if can calculate based on current information
     */
    fun getDiscardedTalon(): List<Int>?{
        if(log.size != DECK_SIZE-TALON_SIZE){
            return null
        }
        val talon = mutableListOf<Int>()
        for(i in 0..DECK_SIZE-1){
            if(!log.any { x -> x.second == getCardById32(i) }){
                talon.add(i)
            }
        }
        talon.sort()
        return talon
    }

    /**
     * Calculate hash for user key set - used to
     * validate, that no card exchange cooperation was present
     * @param player - id of player, whose key hash is being calculated
     * @return Sting - resulting hash. Null if current information is
     * insuffitient to calculate requested hash
     */
    fun getUserKeysHash(player: Int): String?{
        if(keyMap[player].contains(null)){
            return null
        }
        return DigestUtils.sha256Hex(keyMap[player].joinToString(" "))
    }
}



class GameLogger(val N: Int,val  DECK_SIZE: Int,val  TALON_SIZE: Int){
    private val pastLogs = mutableListOf<RoundLogger>()
    var log = RoundLogger(N, DECK_SIZE, TALON_SIZE)

    fun newRound(){
        pastLogs.add(log)
        log = RoundLogger(N, DECK_SIZE,TALON_SIZE)
    }
}