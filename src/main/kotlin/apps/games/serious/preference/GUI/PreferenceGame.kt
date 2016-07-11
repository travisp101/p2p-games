package apps.games.serious.preference.GUI

import apps.games.serious.preference.Bet
import apps.games.serious.preference.Pip
import apps.games.serious.preference.Suit
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import entity.User

/**
 * Created by user on 6/30/16.
 */
class PreferenceGame : Game() {
    lateinit var batch: ModelBatch
    lateinit var font: BitmapFont
    lateinit var tableScreen: TableScreen
    var loaded = false
    override fun create() {
        batch = ModelBatch()
        font = BitmapFont()
        font.data.scale(2f)
        tableScreen = TableScreen(this)
        setScreen(tableScreen)
        loaded = true
    }

    /**
     * Give a player with specified ID
     * a card from a 32 card deck
     */
    fun dealPlayer(player: Int, cardID: Int){
        tableScreen.dealPlayer(player, getCardById(cardID))
    }

    /**
     * Deal a common card. For example
     * TALON in Prefenernce or cards
     * in Texas Holdem Poker
     */
    fun dealCommon(cardID: Int){
        tableScreen.dealCommon(getCardById(cardID))
    }

    /**
     * Show bidding overlay after all other actions are complete
     */
    fun showBiddingOverlay(){
        tableScreen.actionManager.addAfterLastComplete(BiddingOverlayVisibilityAction(tableScreen.biddingOverlay, true))
    }

    /**
     * Show bidding overlay after all other actions are complete
     */
    fun hideBiddingOverlay(){
        tableScreen.actionManager.addAfterLastComplete(BiddingOverlayVisibilityAction(tableScreen.biddingOverlay, false))
    }

    /**
     * display user bets - mark them on bidding overlay
     * @param bets -vararg Pair<User, Bet> - pairs
     * describing User's Bet
     */
    fun displayBets(vararg bets: Pair<User, Bet>){
        for(bet in bets){
            if(bet.second != Bet.UNKNOWN){
                tableScreen.biddingOverlay.markBet(bet.second, bet.first)
            }

        }
    }

    /**
     * Mark all bet buttons as enabled
     */
    fun enableAllBets(){
        for(bet in Bet.values()){
            tableScreen.biddingOverlay.enableBet(bet)
        }
    }

    /**
     * Mark all bet buttons as disabled
     */
    fun disableAllBets(){
        for(bet in Bet.values()){
            tableScreen.biddingOverlay.disableBet(bet)
        }
    }

    /**
     * Enable bets from given list of bets
     * @param bets - Bets to enable
     */
    fun enableBets(vararg bets: Bet){
        for(bet in bets){
            tableScreen.biddingOverlay.enableBet(bet)
        }
    }

    /**
     * Disable bets from given list of bets
     * @param bets - Bets to disable
     */
    fun disableBets(vararg bets: Bet){
        for(bet in bets){
            tableScreen.biddingOverlay.disableBet(bet)
        }
    }

    /**
     * Add callback listener for buttons
     * corresponding to provided bets
     */
    fun <R>registerCallback(callBack: (Bet) -> (R), vararg bets: Bet){
        for(bet in bets){
            tableScreen.biddingOverlay.addCallback(bet, callBack)
        }
    }

    /**
     * In preference we have 32 card deck.
     * This function takes card ID (0 -> 32)
     * or -1 for UNKNOWN card
     * and translates it into corresponding
     * renderable object
     */
    private fun getCardById(cardID: Int): Card{
        val card: Card
        if(cardID == -1){
            card = tableScreen.deck.getCard(Suit.UNKNOWN, Pip.UNKNOWN)
        }else{
            val suitId = cardID / 8
            var pipId: Int = (cardID % 8)
            if(Pip.TWO.index <= pipId){
                pipId += 5
            }
            println(pipId)
            card = tableScreen.deck.getCard(Suit.values().first { x -> x.index == suitId },
                    Pip.values().first{x -> x.index == pipId})
        }
        return card
    }

    override fun render() {
        super.render()
    }


    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.width = 1024
    config.height = 1024
    config.forceExit = false
    val gameGUI = PreferenceGame()
    LwjglApplication(gameGUI, config)
    println("6 \u2660")
}