package apps.games.serious.preference.GUI

import apps.games.serious.preference.Pip
import apps.games.serious.preference.Suit
import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector3
import java.util.concurrent.BlockingDeque
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * Created by user on 7/1/16.
 */


class TableScreen(val game: PreferenceGame): InputAdapter(), Screen {
    private val atlas = TextureAtlas(Gdx.files.internal("cards/carddeck.atlas"))

    private val cam3d = PerspectiveCamera()
    private val camController = FirstPersonCameraController(cam3d)
    private val cam2d = OrthographicCamera()
    private var is2dMode: Boolean = true

    private val table: Table = Table(3, 10)

    private val environment = Environment()

    private var selecting: Card? = null

    val deck = CardDeck(atlas, 2)
    val cards: CardBatch
    var actionManager = ActionManager()
    val topDeck: Card
    var showDeck = true
    val biddingOverlay = BiddingOverlay()

    init{
        biddingOverlay.create()
        //Init cards
        val material = Material(
                TextureAttribute.createDiffuse(atlas.textures.first()),
                BlendingAttribute(false, 1f),
                FloatAttribute.createAlphaTest(0.5f))
        val selectionMaterial = Material(BlendingAttribute(0.4f))
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE))

        cards = CardBatch(material, selectionMaterial)

        //Init selectionMaterial
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE))


        //init deck placement
        topDeck = deck.getCard(Suit.UNKNOWN, Pip.UNKNOWN)
        topDeck.position.set(table.deckPosition)
        topDeck.angle = 180f
        topDeck.update()
        cards.add(topDeck)

        //Init Envirenment
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -.4f, -.4f, -.4f))


        //Init Camera
        camController.setVelocity(10f)
        Gdx.input.inputProcessor = InputMultiplexer(this, biddingOverlay.stage, camController)

    }


    /**
     * Render everything. Also in charge of spawning cards on timer
     */
    override fun render(delta: Float) {
        val delta = Math.min(1 / 30f, Gdx.graphics.deltaTime)

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        camController.update()

        actionManager.update(delta)

        game.batch.begin(getCam())
        game.batch.render(cards, environment)
        game.batch.render(table.tableTop, environment)
        game.batch.end()
        biddingOverlay.render(getCam())
    }

    /**
     * Deal specified card to a specified player
     * @param player - receives the card
     * @param card - card to give
     */
    @Synchronized fun dealPlayer(player: Player, card: Card){
        card.position.set(table.deckPosition)
        val handPos = player.hand.nextCardPosition()
        val angle = player.getAngle()
        player.hand.size ++
        card.angle = 180f
        cards.add(card)

        Gdx.app.log("Spawn", card.suit.type + " - " + card.pip)


        actionManager.addAfterLastReady(Card.animate(card, handPos.x, handPos.y, handPos.z , 0f, 1f, angle))
    }

    /**
     * Deal specified card to a player with a give playerId
     * @param player - id of the player that receives the card
     * @param card - card to give
     */
    @Synchronized fun dealPlayer(player: Int, card: Card){
        dealPlayer(table.players[player], card)
    }


    /**
     * Deal a card common to all players(e.g.
     * TALON in Preference, or cards in texas holdem poker)
     */
    @Synchronized fun dealCommon(card: Card){
        card.position.set(table.deckPosition)
        val handPos = table.commonHand.nextCardPosition()
        val angle = table.getMainPlayer().getAngle()
        table.commonHand.size ++
        card.angle = 180f
        cards.add(card)
        actionManager.addAfterLastReady(Card.animate(card, handPos.x, handPos.y, handPos.z , 0f, 1f, angle))
    }

    @Synchronized fun hideDeck(){
        if(showDeck){
            val pos = Vector3(topDeck.position).add(0f, 0f, -1f)
            actionManager.addAfterLastComplete(Card.animate(topDeck, pos.x, pos.y, pos.z, 0f, 1f, 0f))
            showDeck = true
        }

    }

    @Synchronized fun showDeck(){
        if(!showDeck){
            val pos = Vector3(topDeck.position).add(0f, 0f, 1f)
            actionManager.addAfterLastComplete(Card.animate(topDeck, pos.x, pos.y, pos.z, 0f, 1f, 0f))
            showDeck = false
        }

    }


    override fun resize(width: Int, height: Int) {
        biddingOverlay.resize(width, height)
        cam3d.viewportWidth = width.toFloat()
        cam3d.viewportHeight = height.toFloat()
        cam3d.position.set(0f, -12f, 12f) //experimental constants
        cam3d.lookAt(0f, -8f, 0f)
        cam2d.position.set(0f, 0f, 11f)
        cam2d.lookAt(0f, 0f, 0f)
        cam2d.viewportWidth = 20f
        cam2d.viewportHeight = 20f
        cam3d.update()
        cam2d.update()
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (selecting != null) {
            if(selecting == getObject(screenX, screenY)){
                setSelected(selecting!!)
            }else{
                setSelecting(getObject(screenX, screenY))
            }
            return true
        }else{
            setSelecting(getObject(screenX, screenY))
            return selecting != null
        }
    }

    /**
     * Second click on card
     */
    private fun setSelected(selecting: Card) {
        val pos = table.getMainPlayer().getCardspace()

        actionManager.addAfterLastComplete(Card.animate(selecting, pos.x, pos.y, 0.01f, 0f, 1f, 0f))
    }

    /**
     * First click on card
     */
    private fun setSelecting(newSelecting: Card?){
        if(newSelecting == null){
            return
        }

        if(selecting != null){
            (selecting as Card).isSelected = false
        }
        newSelecting.isSelected = true
        selecting = newSelecting
    }

    /**
     * Get current camera - 2d or 3d view
     */
    private fun getCam(): Camera{
        if(is2dMode){
            return cam2d
        }else{
            return cam3d
        }
    }

    /**
     * Simple implementation to pick pointed card
     */
    fun getObject(screenX: Int, screenY: Int): Card? {
        val ray = getCam().getPickRay(screenX.toFloat(), screenY.toFloat())

        var result: Card? = null
        var distance = -1f

        for (instance in cards) {
             val dist2 = ray.origin.dst2(instance.position)
            if (distance >= 0f && dist2 > distance)
                continue

            if (Intersector.intersectRaySphere(ray, instance.position, instance.radius, null)) {
                result = instance
                distance = dist2
            }
        }

        return result
    }

    override fun keyUp(keycode: Int): Boolean {
        if(keycode == Input.Keys.C){
            is2dMode = !is2dMode
            return true
        }
        return false
    }

    override fun show() { }

    override fun pause() { }

    override fun hide() { }

    override fun resume() { }

    override fun dispose() {
        atlas.dispose()
        cards.dispose()
    }
}