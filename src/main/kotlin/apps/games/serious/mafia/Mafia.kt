package apps.games.serious.mafia

import apps.chat.Chat
import apps.games.GameExecutionException
import apps.games.GameManager
import apps.games.GameManagerClass
import apps.games.serious.CardGame
import apps.games.serious.Cheat.GUI.CheatGame
import apps.games.serious.mafia.GUI.MafiaGame
import apps.games.serious.mafia.roles.*
import apps.games.serious.mafia.subgames.role.distribution.RoleDistributionGame
import apps.games.serious.mafia.subgames.role.distribution.RoleDistributionHelper
import apps.games.serious.mafia.subgames.role.generation.RoleDeck
import apps.games.serious.mafia.subgames.role.generation.RoleGenerationGame
import apps.games.serious.mafia.subgames.role.generation.RoleGenerationVerifier
import apps.games.serious.mafia.subgames.role.secret.SecretDeck
import apps.games.serious.mafia.subgames.role.secret.SecretSharingGame
import apps.games.serious.mafia.subgames.role.secret.SecretSharingVerifier
import apps.games.serious.mafia.subgames.sum.SMSfAResult
import apps.games.serious.mafia.subgames.sum.SecureMultipartySumForAnonymizationGame
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import crypto.RSA.ECParams
import crypto.RSA.RSAKeyManager
import crypto.random.randomBigInt
import crypto.random.randomString
import entity.Group
import entity.User
import org.apache.commons.codec.digest.DigestUtils
import org.bouncycastle.crypto.InvalidCipherTextException
import org.bouncycastle.math.ec.ECPoint
import proto.GameMessageProto
import java.math.BigInteger
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


/**
 * Created by user on 8/9/16.
 */

class Mafia(chat: Chat, group: Group, gameID: String, gameManager: GameManagerClass = GameManager) : CardGame(chat, group, gameID, 52, gameManager) {
    override val name: String
        get() = "mafia game"

    private enum class State{
        INIT,
        VALIDATE_KEYS,
        INIT_ID,
        DAY_PHASE_PICK,
        DAY_PHASE_VERIFY,
        DAY_PHASE_REVEAL,
        DAY_PHASE_KILL,
        NIGHT_PHASE_DOCTOR_I,
        DETECTIVE_CHANNEL,
        DETECTIVE_CHOICE,
        DETECTIVE_CHOICE_REVEAL,
        MAFIA_COMMUNICATE,
        LOOP,
        END,
    }

    private lateinit var gameGUI: MafiaGame
    private lateinit var application: LwjglApplication
    private val logger = MafiaLogger()

    private var state: State = State.INIT
    private val keyManager = RSAKeyManager()
    private val HANDSHAKE_PHRASE = "HANDSHAKE" // who would've thought
    private lateinit var role: PlayerRole
    private lateinit var roleDeck: RoleDeck
    private lateinit var secretDeck: SecretDeck
    private lateinit var roleGenerationVerifier: RoleGenerationVerifier
    private lateinit var secretSharingVerivier: SecretSharingVerifier
    private lateinit var roleDistributionHelper: RoleDistributionHelper
    private val ids: Array<BigInteger>
    private val alive = mutableSetOf(*group.users.toTypedArray())
    private val dead = mutableSetOf<User>()
    private val userRoles = mutableMapOf<User, Role>()

    private val N: Int
    private val M: Int //total mafia
    private var mafiaLeft: Int
    private lateinit var toKill: User


    private lateinit var detectiveExpSMS: SMSfAResult
    private lateinit var detectiveModSMS: SMSfAResult
    private lateinit var detextiveExp: BigInteger
    private lateinit var detextiveMod: BigInteger


    init {
        N = group.users.size
        ids = Array(N, {i -> BigInteger.ZERO})
        //if(N < 3) throw GameExecutionException("mafia is only viable with 4+ players")
        M = Math.sqrt(N * 1.0).toInt()
        for(user in playerOrder){
            userRoles[user] = Role.UNKNOWN
        }
        mafiaLeft = M
        initGame()
    }



    override fun evaluate(responses: List<GameMessageProto.GameStateMessage>): String {
        for (msg in responses) {
            chat.showMessage("[${msg.user.name}] said ${msg.value}")
        }
        when(state){

            State.INIT -> {
                for (msg in responses) {
                    keyManager.registerUserPublicKey(User(msg.user), msg.value)
                }
                currentPlayerID = -1
                state = State.VALIDATE_KEYS
            }
            State.VALIDATE_KEYS -> {
                if (playerID == currentPlayerID) {
                    for (msg in responses) {
                        try {
                            val s = keyManager.decodeString(msg.value)
                            val handshake = s.split(" ").last()
                            if (handshake != HANDSHAKE_PHRASE) {
                                throw GameExecutionException("Invalid RSA key")
                            }
                        } catch (e: InvalidCipherTextException) {
                            throw GameExecutionException("Malformed RSA key")
                        }
                    }
                }

                currentPlayerID++
                if (currentPlayerID != N) {
                    val s = randomString(512) + " " + HANDSHAKE_PHRASE
                    return keyManager.encodeForUser(playerOrder[currentPlayerID], s)
                }else{
                    state = State.INIT_ID
                    chat.sendMessage("RSA is OK. Generating deck")
                    return randomBigInt(ECParams.n).toString()
                }
            }
            State.INIT_ID -> {
                for(msg in responses){
                    val user = User(msg.user)
                    val userID = getUserID(user)
                    ids[userID] = BigInteger(msg.value)
                }
                for(i in 0..N-1){
                    for(j in 0..N-1){
                        if((i != j && ids[i] == ids[j]) || ids[i] == BigInteger.valueOf(2) * ids[j]){
                            return randomBigInt(ECParams.n).toString()
                        }
                    }
                }
                initRoles()
                shareSecrets()
                // state = State.DAY_PHASE_PICK
                //DEBUG
                state = State.DETECTIVE_CHANNEL
            }
            State.DAY_PHASE_PICK -> {
                state = State.DAY_PHASE_VERIFY
                if(chat.me() in alive){
                    val pick = pickUser()
                    return getUserID(pick).toString()
                }
            }
            State.DAY_PHASE_VERIFY -> {
                val voices = mutableMapOf<User, Int>()
                for(msg in responses){
                    val user = User(msg.user)
                    if(user in dead){
                        continue
                    }
                    toKill = playerOrder[msg.value.toInt()]
                    logger.registerDayVote(user, toKill)
                    if(toKill !in alive){
                        throw GameExecutionException("What is dead may never die")
                    }
                    if(toKill !in voices){
                        voices[toKill] = 0
                    }
                    voices[toKill] = voices[toKill]!! + 1
                }
                val deadUser = voices.maxBy { x -> x.value }?.key ?: throw GameExecutionException("Something wrong in this town")
                val draw = voices.filter { x -> x.value == voices[deadUser] }.size > 1
                if(draw){
                    state = State.DAY_PHASE_PICK
                    logger.resetDay()
                } else{
                    state = State.DAY_PHASE_REVEAL
                }
                return getUserID(deadUser).toString()
            }
            State.DAY_PHASE_REVEAL -> {
                val distinctResults = responses.distinctBy { x -> x.value }.size
                if(distinctResults > 1){
                    throw GameExecutionException("Something went wrong during day phase")
                }
                state = State.DAY_PHASE_KILL
                toKill = playerOrder[responses[0].value.toInt()]
                if(toKill == chat.me()){ // if we were killed
                    return roleDeck.roleKeys.elementAt(playerID).toString()
                }
            }
            State.DAY_PHASE_KILL -> {
                for(msg in responses){
                    val user = User(msg.user)
                    val userID = getUserID(user)
                    if(user == toKill){
                        roleDistributionHelper.registerRoleKey(user, userID, BigInteger(msg.value))
                        val role = if(user == chat.me()) role.role else roleDistributionHelper.getRole(userID)
                        killUser(user, role)
                        break
                    }
                }
                //state = State.NIGHT_PHASE_DOCTOR_I
                state = State.DETECTIVE_CHANNEL
            }
            State.NIGHT_PHASE_DOCTOR_I -> {
                TODO() //TEST
                if(dead.filter{x -> userRoles[x] == Role.DOCTOR}.isEmpty()){
                    processDoctorPick()
                }
                state = State.DETECTIVE_CHANNEL
            }
            State.DETECTIVE_CHANNEL -> {
                if(dead.filter{x -> userRoles[x] == Role.DOCTOR}.isNotEmpty()){
                    state = State.MAFIA_COMMUNICATE
                    return ""
                }
                processDetectiveRSA()
                state = State.DETECTIVE_CHOICE
                return "${detectiveModSMS.R} ${detectiveExpSMS.R}"
            }
            State.DETECTIVE_CHOICE -> {
                regiserDetectiveRSA(responses)
                processDetectivePick()
                state = State.DETECTIVE_CHOICE_REVEAL
                return logger.getDetectiveNoisedInput()
            }
            State.DETECTIVE_CHOICE_REVEAL -> {
                var noise = BigInteger.ZERO
                val hashes = mutableMapOf<User, String>()
                for(msg in responses){
                    val user = User(msg.user)
                    hashes[user] = DigestUtils.sha256Hex(msg.value)
                    val split = msg.value.split(" ")
                    noise += BigInteger(split[1])
                }
                if(!logger.verifyLastDetectiveRHashes(hashes)){
                    throw GameExecutionException("Someone cheated with his SMSfA value inputes")
                }
                val targetID = (logger.getDetectiveSum() - noise)
                state = State.MAFIA_COMMUNICATE
                return keyManager.encodeWithParams(detextiveMod, detextiveExp, secretDeck.getSecretForId(targetID).getEncoded(false))

            }
            State.MAFIA_COMMUNICATE -> {
                finalizeDetectiveChoice(responses)
                state = State.DAY_PHASE_PICK
                logger.nextDay()
                //TODO
            }
            State.LOOP -> {
                Thread.sleep(2000)
            }
            State.END -> TODO()
        }
        return ""
    }

    /**
     * Start GUI for the Cheat game
     */
    private fun initGame(): String {
        Role.reset()
        val config = LwjglApplicationConfiguration()
        config.width = 1024
        config.height = 1024
        config.forceExit = false
        config.title = "Cheat Game[${chat.username}]"

        gameGUI = MafiaGame(group, logger)
        application = LwjglApplication(gameGUI, config)
        while (!gameGUI.loaded) {
            Thread.sleep(200)
        }
        return ""
    }

    /**
     * process role generation and distribution.
     * after this step [role] holds current player role
     * with the list of known comrades
     */
    private fun initRoles(){
        val rolesCount = mutableMapOf<Role, Int>()
        for(role in Role.values()){
            when(role){
                Role.MAFIA -> rolesCount[role] = M
                else -> if(role != Role.INNOCENT && role != Role.UNKNOWN) rolesCount[role] = 1
            }
        }
        rolesCount[Role.INNOCENT] = N - rolesCount.values.sum()
        val roleGenerationGame = RoleGenerationGame(chat, group, subGameID(), ECParams, rolesCount, gameManager)
        try {
            val roleGenerationFuture = runSubGame(roleGenerationGame)
            roleGenerationVerifier = roleGenerationFuture.get().second
            roleDeck = roleGenerationFuture.get().first
            val roleFuture = runSubGame(RoleDistributionGame(chat, group, subGameID(), ECParams, roleDeck, gameManager))
            role = roleFuture.get().first
            roleDistributionHelper = roleFuture.get().second
        }catch (e: CancellationException){
            return
        }catch (e: Exception){
            throw GameExecutionException(e.message ?: "Something went wrong in role generation/distribution")
        }
        for(user in role.getComrades()){
            logger.registerUserRole(user, role.role)
        }
        gameGUI.setRole(role.role)
        for(i in 0..N-1){
            if(playerOrder[i] in role.getComrades()){
                gameGUI.dealPlayer(getTablePlayerId(i), role.role, logger.getUserRolePosition(playerOrder[i]))
                userRoles[playerOrder[i]] = role.role
            }else{
                gameGUI.dealPlayer(getTablePlayerId(i), Role.UNKNOWN)
            }
        }

    }

    /**
     * create share secrets via SecretSharing game
     */
    private fun shareSecrets(){
        try {
            val secretFuture = runSubGame(SecretSharingGame(chat, group, subGameID(), ECParams, role, ids[playerID], gameManager))
            secretDeck = secretFuture.get().first
            secretSharingVerivier = secretFuture.get().second
        } catch (e: CancellationException){
            return
        } catch (e: Exception){
            throw GameExecutionException(e.message ?: "Something went wrong in secret sharing")
        }
    }

    /**
     * Let users pick his target
     */
    private fun pickUser(millis: Long = Long.MAX_VALUE): User {
        val userPickQueue = LinkedBlockingQueue<User>(1)
        val callback = { x: User -> userPickQueue.offer(x) }
        gameGUI.resetAllUserPicks()
        gameGUI.registerUserPickCallback(callback, playerOrder)
        gameGUI.disableUserPicks(dead)
        gameGUI.showUserPickOverlay()
        gameGUI.showHint("Pick user to kill (DAY PHASE)")
        val res = userPickQueue.poll(millis, TimeUnit.SECONDS) ?: chat.me()
        //val res = userPickQueue.poll(millis, TimeUnit.SECONDS)
        gameGUI.hideUserPickOverlay()
        return res
    }

    /**
     * given user and his role - register him as dead
     */
    private fun killUser(user: User, role: Role){
        userRoles[user] = role
        alive.remove(user)
        dead.add(user)
        chat.showMessage("Killed [${user.name}] whose role was [${role.name}]")
        if(role == Role.MAFIA){
            mafiaLeft --
        }
        if(role != this.role.role){
            gameGUI.revealPlayerRole(getTablePlayerId(getUserID(user)), role, logger.getUserRolePosition(user))
        }
        gameGUI.animateRolePlay(role, logger.getUserRolePosition(user))
    }

    /**
     * Run first phase of doctor actions:
     * doctor picks his target via SMSfA
     */
    private fun processDoctorPick(){
        val deadline = Calendar.getInstance()
        deadline.add(Calendar.SECOND, DoctorRole.TIMEOUT.toInt())
        val targetID: BigInteger
        if(role is DoctorRole){
            val user = pickUser(DoctorRole.TIMEOUT)
            targetID = ids[getUserID(user)]
            (role as DoctorRole).registerTarget(targetID)
        }else{
            targetID = BigInteger.ZERO
        }
        val currentTime = Calendar.getInstance()
        if(currentTime < deadline){
            Thread.sleep(deadline.timeInMillis - currentTime.timeInMillis)
        }
        val targetFuture = gameManager.initSubGame(SecureMultipartySumForAnonymizationGame(chat, group, subGameID(), keyManager, targetID, ECParams.n, gameManager))
        logger.registerDoctorFirstPhase(targetFuture.get())
    }


    /**
     * Run detective choice phase:
     * detective picks his target and
     * broadcasts encoded id via SMSfA
     */
    private fun processDetectivePick(){
        val deadline = Calendar.getInstance()
        deadline.add(Calendar.SECOND, DetectiveRole.TIMEOUT.toInt())
        val encryptedTargetId: BigInteger
        if(role is DetectiveRole){
            val user = pickUser(DetectiveRole.TIMEOUT)
            val userID = getUserID(user)
            encryptedTargetId = ids[userID] * (role as DetectiveRole).getUserK(user)
            (role as DetectiveRole).registerTarget(user, ids[userID])
        }else{
            encryptedTargetId = BigInteger.ZERO
        }
        val currentTime = Calendar.getInstance()
        if(currentTime < deadline){
            Thread.sleep(deadline.timeInMillis - currentTime.timeInMillis)
        }
        val targetFuture = gameManager.initSubGame(SecureMultipartySumForAnonymizationGame(chat, group, subGameID(), keyManager, encryptedTargetId, ECParams.n, gameManager))
        logger.registerDetectiveChoiceSMS(targetFuture.get())
    }

    /**
     * exchange detective RSA public paramenters via SMSfA
     */
    private fun processDetectiveRSA(){
        val mod: BigInteger
        val exp: BigInteger


        if(role is DetectiveRole){
            mod = (role as DetectiveRole).getModulus()
            exp = (role as DetectiveRole).getPulicExponent()
        }else{
            mod = BigInteger.ZERO
            exp = BigInteger.ZERO
        }
        val maxValue = BigInteger.valueOf(2).pow(DetectiveRole.KEY_LENGTH)
        val modFuture = gameManager.initSubGame(SecureMultipartySumForAnonymizationGame(chat, group, subGameID(), keyManager, mod, maxValue, gameManager))
        val expFuture = gameManager.initSubGame(SecureMultipartySumForAnonymizationGame(chat, group, subGameID(), keyManager, exp, maxValue, gameManager))
        //No checks for this stage is required at this pont: if something is wrong -detective decryption will fail
        //so we can only store data about sum
        detectiveModSMS = modFuture.get()
        detectiveExpSMS = expFuture.get()
    }

    /**
     * compute and store RSA parameters
     */
    private fun regiserDetectiveRSA(responses: List<GameMessageProto.GameStateMessage>){
        var modSum = BigInteger.ZERO
        var expSum = BigInteger.ZERO
        for(msg in responses){
            val split = msg.value.split(" ").map { x -> BigInteger(x) }
            modSum += split[0]
            expSum += split[1]
        }
        detextiveExp = detectiveExpSMS.sum - expSum
        detextiveMod = detectiveModSMS.sum - modSum
    }

    private fun finalizeDetectiveChoice(responses: List<GameMessageProto.GameStateMessage>){
        if(role !is DetectiveRole){
            return
        }
        var sum: ECPoint = ECParams.curve.infinity
        for(msg in responses){
            val part = (role as DetectiveRole).decodeSecretPart(msg.value)
            sum  = sum.add(part)
        }
        for(i in 0..N-1){
            if(ECParams.g.multiply(ids[i]) == sum){
                (role as DetectiveRole).registerTargetResult(ids[i], false)
                logger.registerDetectivePlay(playerOrder[i], false)
            }
            if(ECParams.g.multiply(ids[i] * BigInteger.valueOf(2)) == sum){
                (role as DetectiveRole).registerTargetResult(ids[i], true)
                logger.registerDetectivePlay(playerOrder[i], true)
            }
        }
    }

    override fun getInitialMessage(): String {
        return keyManager.getPublicKey()
    }

    override fun getResult() {
    }

    override fun isFinished(): Boolean {
        return  state == State.END
    }

    override fun close() {
        application.stop()
    }
}