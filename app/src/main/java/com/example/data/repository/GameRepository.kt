package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.PlayerProfileEntity
import com.example.data.db.ProgressDao
import com.example.data.db.ProgressEntity
import com.example.data.db.RelicEntity
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GameRepository(private val database: AppDatabase) {
    private val dao: ProgressDao = database.progressDao()

    val allProgress: Flow<List<ProgressEntity>> = dao.getAllProgress()
    val allRelics: Flow<List<RelicEntity>> = dao.getAllRelics()
    val playerProfile: Flow<PlayerProfileEntity?> = dao.getPlayerProfile()

    val worlds: List<WorldInfo> = listOf(
        WorldInfo(
            id = 1,
            nameEn = "Whispering Forest",
            nameHi = "रहस्यमयी वन",
            subtitleEn = "Ancient trees and mossy stone gates",
            subtitleHi = "प्राचीन वृक्ष और काईदार द्वार",
            accentColorHex = 0xFF10B981,
            iconName = "forest",
            levelRange = 1..5
        ),
        WorldInfo(
            id = 2,
            nameEn = "Sunken Temple of Enigma",
            nameHi = "प्राचीन सूर्य मन्दिर",
            subtitleEn = "Golden gears and sun dials",
            subtitleHi = "स्वर्ण चक्र और सूर्य यंत्र",
            accentColorHex = 0xFFF59E0B,
            iconName = "temple",
            levelRange = 6..10
        ),
        WorldInfo(
            id = 3,
            nameEn = "Citadel of Shadows",
            nameHi = "छायाओं का दुर्ग",
            subtitleEn = "Echoing halls and cryptic obelisks",
            subtitleHi = "अंधकार भरे गलियारे और गुप्त स्तम्भ",
            accentColorHex = 0xFF8B5CF6,
            iconName = "citadel",
            levelRange = 11..15
        ),
        WorldInfo(
            id = 4,
            nameEn = "Astral Sanctuary",
            nameHi = "नक्षत्र लोक",
            subtitleEn = "Cosmic portals and trials of destiny",
            subtitleHi = "ब्रह्मांडीय द्वार और अंतिम परीक्षा",
            accentColorHex = 0xFF06B6D4,
            iconName = "astral",
            levelRange = 16..20
        )
    )

    val defaultRelics: List<RelicEntity> = listOf(
        RelicEntity(
            relicId = "relic_forest_emerald",
            nameEn = "Emerald of the Dryad",
            nameHi = "वनदेवी का पन्ना",
            descriptionEn = "A pulsating green gem granting intuition in the forest.",
            descriptionHi = "एक चमकीला हरा रत्न जो प्रकृति के रहस्यों को उजागर करता है।",
            worldId = 1,
            isUnlocked = false
        ),
        RelicEntity(
            relicId = "relic_sun_scarab",
            nameEn = "Scarab of the Sun God",
            nameHi = "सूर्य देव का स्वर्ण भौंरा",
            descriptionEn = "An ornate golden talisman that dispels shadows.",
            descriptionHi = "एक प्राचीन स्वर्ण ताबीज जो अंधकार को दूर करता है।",
            worldId = 2,
            isUnlocked = false
        ),
        RelicEntity(
            relicId = "relic_shadow_dagger",
            nameEn = "Obsidian Shadow Dagger",
            nameHi = "काला छाया खंजर",
            descriptionEn = "Forged from meteoric glass to break ancient locks.",
            descriptionHi = "उल्कापिंड के कांच से गढ़ा खंजर जो प्राचीन ताले खोलता है।",
            worldId = 3,
            isUnlocked = false
        ),
        RelicEntity(
            relicId = "relic_cosmic_core",
            nameEn = "Cosmic Heart Core",
            nameHi = "ब्रह्मांडीय महा-रत्न",
            descriptionEn = "The ultimate artifact holding the balance of wisdom.",
            descriptionHi = "सम्पूर्ण ज्ञान और बुद्धिमता का सर्वोच्च आकाशीय रत्न।",
            worldId = 4,
            isUnlocked = false
        )
    )

    val levels: List<LevelDefinition> = listOf(
        // WORLD 1: Whispering Forest
        LevelDefinition(
            id = 1,
            worldId = 1,
            titleEn = "The Mossy Gateway",
            titleHi = "काईदार प्रवेश द्वार",
            loreEn = "You stand before the colossal stone gates of the Whispering Forest. Three ancient runic rings guard the entrance. Align the runes to open the gate!",
            loreHi = "आप रहस्यमयी जंगल के विशाल पाषाण द्वार के सामने खड़े हैं। तीन प्राचीन रुन चक्र प्रवेश द्वार की रक्षा कर रहे हैं। द्वार खोलने के लिए रुनों को संरेखित करें!",
            puzzleType = PuzzleType.RUNIC_DIAL,
            parMoves = 4,
            rewardSparks = 20,
            hint1En = "Rotate each stone ring until its golden rune faces North (top).",
            hint1Hi = "प्रत्येक पाषाण चक्र को तब तक घुमाएं जब तक उसका स्वर्ण चिन्ह उत्तर (ऊपर) न आ जाए।",
            hint2En = "Start with the outermost ring, then work inward.",
            hint2Hi = "सबसे बाहरी चक्र से शुरुआत करें, फिर अंदर की ओर बढ़ें।",
            solutionExplanationEn = "Aligning all symbols to the 12 o'clock position channels the forest energy and unlocks the passage.",
            solutionExplanationHi = "सभी चिन्हों को 12 बजे की स्थिति में लाने से वन ऊर्जा सक्रिय होकर मार्ग खोल देती है।",
            runicConfig = RunicConfig(
                ringCount = 3,
                initialAngles = listOf(90, 180, 270),
                targetAngles = listOf(0, 0, 0),
                dialLabelsEn = listOf("Outer Foliage Ring", "Mid Vine Ring", "Inner Core Ring"),
                dialLabelsHi = listOf("बाहरी पत्र चक्र", "मध्य बेल चक्र", "आंतरिक केंद्र चक्र")
            )
        ),
        LevelDefinition(
            id = 2,
            worldId = 1,
            titleEn = "Riddle of the Ancient Oak",
            titleHi = "प्राचीन वटवृक्ष की पहेली",
            loreEn = "A massive speaking oak blocks your path. Its wooden face creaks as it tests your wisdom with a primordial riddle.",
            loreHi = "एक विशाल बोलने वाला वटवृक्ष आपका रास्ता रोकता है। वह प्राचीन पहेली से आपकी बुद्धि की परीक्षा लेता है।",
            puzzleType = PuzzleType.ANCIENT_RIDDLE,
            parMoves = 1,
            rewardSparks = 20,
            hint1En = "Think about something that moves without legs and whispers without a tongue.",
            hint1Hi = "ऐसी चीज़ के बारे में सोचें जो बिना पैरों के चलती है और बिना जीभ के फुसफुसाती है।",
            hint2En = "It rustles the leaves and howls in storms.",
            hint2Hi = "यह पत्तियों को हिलाती है और तूफानों में गूंजती है।",
            solutionExplanationEn = "The Wind has no voice yet roars; it breathes life through leaves yet cannot be caught.",
            solutionExplanationHi = "हवा (पवन) के पास मुख नहीं फिर भी वह गर्जना करती है और पत्तियों में सांस भरती है।",
            riddleConfig = RiddleConfig(
                questionEn = "I have no voice, yet I speak to thousand leaves. I have no wings, yet I travel across mountains. You feel me, but cannot grasp me. What am I?",
                questionHi = "मेरी कोई आवाज़ नहीं, फिर भी मैं हज़ारों पत्तियों से बोलती हूँ। मेरे कोई पंख नहीं, फिर भी पहाड़ों को लांघती हूँ। मुझे महसूस कर सकते हो, पर पकड़ नहीं सकते। मैं कौन हूँ?",
                optionsEn = listOf("The River", "The Wind", "A Shadow", "Echo"),
                optionsHi = listOf("नदी (The River)", "पवन / हवा (The Wind)", "छाया (A Shadow)", "प्रतिध्वनि (Echo)"),
                correctIndex = 1,
                loreFactEn = "The ancient forest spirits revere the North Wind as the first traveler of Earth.",
                loreFactHi = "प्राचीन वन देव उत्तरी पवन को पृथ्वी का प्रथम पथिक मानते हैं।"
            )
        ),
        LevelDefinition(
            id = 3,
            worldId = 1,
            titleEn = "Altar of the Four Elements",
            titleHi = "चार तत्वों की पावन वेदी",
            loreEn = "In a sacred clearing, a 3x3 grid of runic shrines must be activated. Tapping any shrine toggles itself and its direct neighbors. Ignite every shrine!",
            loreHi = "एक पवित्र उपवन में 3x3 रुन वेदियों का आव्यूह है। किसी भी वेदी को छूने पर वह और उसके पड़ोसी सक्रिय या निष्क्रिय हो जाते हैं। सभी वेदियों को प्रज्वलित करें!",
            puzzleType = PuzzleType.ELEMENTAL_MATRIX,
            parMoves = 5,
            rewardSparks = 25,
            hint1En = "Corner altars affect fewer neighbors than center or edge altars.",
            hint1Hi = "कोने की वेदियां मध्य या किनारे की वेदियों की तुलना में कम पड़ोसियों को प्रभावित करती हैं।",
            hint2En = "Try tapping corners first to balance the outer perimeter.",
            hint2Hi = "बाहरी घेरे को संतुलित करने के लिए पहले कोनों को छूने का प्रयास करें।",
            solutionExplanationEn = "Activating symmetrical opposing nodes clears conflicting magical currents.",
            solutionExplanationHi = "समान विपरीत कोनों को छूने से असंतुलित धाराएं शांत होकर सभी वेदियों को जला देती हैं।",
            matrixConfig = MatrixConfig(
                size = 3,
                initialGrid = listOf(
                    false, true, false,
                    true, false, true,
                    false, true, false
                )
            )
        ),
        LevelDefinition(
            id = 4,
            worldId = 1,
            titleEn = "The Overgrown Crypt",
            titleHi = "गुप्त पाषाण तहखाना",
            loreEn = "Damp corridors stretch into the subterranean maze. Grab the ancient Bronze Key, bypass the venomous thorn trap, and unlock the heavy iron exit door!",
            loreHi = "घने तहखाने के रास्ते भूलभुलैया जैसे हैं। प्राचीन कांस्य चाबी ढूंढें, विषैले कांटों से बचें, और भारी लोहे का दरवाजा खोलकर बाहर निकलें!",
            puzzleType = PuzzleType.CHAMBER_MAZE,
            parMoves = 8,
            rewardSparks = 25,
            hint1En = "Plan your route to collect the Bronze Key before heading to the locked door.",
            hint1Hi = "दरवाजे की ओर जाने से पहले कांस्य चाबी लेने का मार्ग तय करें।",
            hint2En = "Red tiles are deadly thorn traps—step around them!",
            hint2Hi = "लाल रंग की टाइलें विषैले कांटे हैं—उनके पास से घूम कर निकलें!",
            solutionExplanationEn = "Stepping sequentially: Down -> Right -> Key -> Up -> Door -> Exit portal.",
            solutionExplanationHi = "क्रमबद्ध कदम: नीचे -> दाएं -> चाबी -> ऊपर -> कपाट -> निकास द्वार।",
            mazeConfig = MazeConfig(
                width = 5,
                height = 5,
                grid = listOf(
                    TileType.START, TileType.EMPTY, TileType.WALL, TileType.EMPTY, TileType.KEY_BRONZE,
                    TileType.EMPTY, TileType.TRAP, TileType.EMPTY, TileType.EMPTY, TileType.EMPTY,
                    TileType.EMPTY, TileType.WALL, TileType.DOOR_BRONZE, TileType.WALL, TileType.EMPTY,
                    TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.TRAP, TileType.EMPTY,
                    TileType.WALL, TileType.WALL, TileType.EMPTY, TileType.EMPTY, TileType.EXIT
                ),
                maxEnergy = 22
            )
        ),
        LevelDefinition(
            id = 5,
            worldId = 1,
            titleEn = "Sanctuary of the Dryad",
            titleHi = "वनदेवी का गर्भगृह (बॉस परीक्षा)",
            loreEn = "The guardian Dryad appears in shimmering emerald light. She holds the sacred Emerald of the Forest! Solve her cipher to prove your worthiness.",
            loreHi = "वनदेवी जगमगाती हरी रोशनी में प्रकट होती हैं। उनके पास वन का पवित्र पन्ना है! अपनी योग्यता सिद्ध करने के लिए उनकी कूट-पहेली को सुलझाएं।",
            puzzleType = PuzzleType.CIPHER_DECODER,
            parMoves = 1,
            rewardSparks = 35,
            relicUnlockId = "relic_forest_emerald",
            hint1En = "Notice the sequence of leaf petals: 2, 4, 8, 16... each step multiplies by 2.",
            hint1Hi = "पत्तियों के क्रम को देखें: 2, 4, 8, 16... प्रत्येक चरण 2 से गुणा हो रहा है।",
            hint2En = "What comes after 16 in this sacred geometric doubling?",
            hint2Hi = "इस पवित्र दोहरे क्रम में 16 के बाद क्या आता है?",
            solutionExplanationEn = "2 * 2 = 4, 4 * 2 = 8, 8 * 2 = 16, 16 * 2 = 32. The sacred harmonic frequency is 32!",
            solutionExplanationHi = "2, 4, 8, 16 का अगला गुणज 32 है। यही वन का पवित्र अंक है!",
            cipherConfig = CipherConfig(
                promptEn = "The Tree of Life grows according to divine harmony: [ 2 -> 4 -> 8 -> 16 -> ? ]. Find the sacred number to awaken the Dryad's blessing.",
                promptHi = "जीवन का वृक्ष दिव्य अनुपात में बढ़ता है: [ 2 -> 4 -> 8 -> 16 -> ? ]। वनदेवी का आशीर्वाद पाने हेतु लुप्त पवित्र संख्या ज्ञात करें।",
                cipherText = "🌿 ᚱ(2) -> 🌿 ᚦ(4) -> 🌿 ᚨ(8) -> 🌿 ᚹ(16) -> 🌿 ???",
                cluesEn = listOf("Every season doubles the power of the grove", "Multiply the previous number by 2"),
                cluesHi = listOf("प्रत्येक मौसम में वन की शक्ति दुगनी हो जाती है", "पिछली संख्या को 2 से गुणा करें"),
                numericAnswer = 32,
                options = listOf("24", "32", "64", "48")
            )
        ),

        // WORLD 2: Sunken Temple of Enigma
        LevelDefinition(
            id = 6,
            worldId = 2,
            titleEn = "The Mechanical Gear Vault",
            titleHi = "चक्रव्यूह यंत्र तिजोरी",
            loreEn = "You descend into the Sunken Temple of Enigma. Massive bronze cogs grind. Here, rotating the outer dial also turns the middle dial! Align the solar markers.",
            loreHi = "आप सूर्य मन्दिर में उतरते हैं। भारी कांस्य के चक्र घूमते हैं। यहाँ बाहरी चक्र को घुमाने से मध्य चक्र भी आधा घूमता है! सूर्य चिन्हों को मिलाएं।",
            puzzleType = PuzzleType.RUNIC_DIAL,
            parMoves = 6,
            rewardSparks = 25,
            hint1En = "Because dial 1 turns dial 2, adjust dial 1 first until it reaches 0, then calibrate dial 2.",
            hint1Hi = "क्योंकि चक्र 1 चक्र 2 को भी घुमाता है, पहले चक्र 1 को 0 पर लाएं, फिर चक्र 2 को ठीक करें।",
            hint2En = "The inner dial moves independently. Set it last.",
            hint2Hi = "आंतरिक चक्र स्वतंत्र रूप से घूमता है। इसे सबसे अंत में सेट करें।",
            solutionExplanationEn = "Compensate for the gear coupling by positioning the driver dial before adjusting the follower dial.",
            solutionExplanationHi = "जुड़े हुए चक्रों में पहले मुख्य चक्र को सही स्थान पर लाकर फिर आश्रित चक्र को घुमाएं।",
            runicConfig = RunicConfig(
                ringCount = 3,
                initialAngles = listOf(180, 90, 270),
                targetAngles = listOf(0, 0, 0),
                dialLabelsEn = listOf("Solar Drive Gear", "Lunar Follower Gear", "Inner Core Spindle"),
                dialLabelsHi = listOf("सूर्य मुख्य चक्र", "चंद्र सहायक चक्र", "केंद्र चक्र"),
                dialConnections = mapOf(0 to listOf(1)) // Rotating 0 also rotates 1
            )
        ),
        LevelDefinition(
            id = 7,
            worldId = 2,
            titleEn = "Sphinx's Sundial",
            titleHi = "सूर्यघड़ी की चुनौती",
            loreEn = "A golden sphinx carved of sandstone poses a riddle carved into the temple wall beside an ancient sundial.",
            loreHi = "बलुआ पत्थर से बनी एक स्वर्ण स्फिंक्स दीवार पर खुदी सूर्यघड़ी के पास आपसे एक रहस्यमयी पहेली पूछती है।",
            puzzleType = PuzzleType.ANCIENT_RIDDLE,
            parMoves = 1,
            rewardSparks = 25,
            hint1En = "Think about something born in the light, but vanishing when complete darkness arrives.",
            hint1Hi = "ऐसी चीज़ जो प्रकाश में जन्म लेती है, किन्तु घने अंधेरे में विलुप्त हो जाती है।",
            hint2En = "It mimics your every step, but cannot speak or feel.",
            hint2Hi = "यह आपके हर कदम की नकल करती है, पर बोल या छू नहीं सकती।",
            solutionExplanationEn = "A shadow follows you everywhere in sunlight, grows long at dusk, yet disappears in pitch darkness.",
            solutionExplanationHi = "छाया धूप में आपके साथ चलती है और अंधेरे में विलीन हो जाती है।",
            riddleConfig = RiddleConfig(
                questionEn = "I am tallest when the sun is low; I vanish when darkness falls. I can mirror your every gesture, yet have no flesh or bones. What am I?",
                questionHi = "जब सूर्य ढलता है मैं सबसे लंबी होती हूँ, और अंधेरा होने पर गायब हो जाती हूँ। मैं आपके हर इशारे की नकल करती हूँ, फिर भी मेरा कोई शरीर नहीं। मैं क्या हूँ?",
                optionsEn = listOf("A Mirror", "A Shadow", "A Ghost", "The Horizon"),
                optionsHi = listOf("दर्पण (A Mirror)", "छाया (A Shadow)", "आत्मा (A Ghost)", "क्षितिज (The Horizon)"),
                correctIndex = 1,
                loreFactEn = "Ancient temple priests measured the hour of prayer strictly using the length of the Sphinx's shadow.",
                loreFactHi = "प्राचीन मंदिर के पुजारी स्फिंक्स की छाया की लंबाई देखकर प्रार्थना का समय तय करते थे।"
            )
        ),
        LevelDefinition(
            id = 8,
            worldId = 2,
            titleEn = "Jeweled Sun Altar",
            titleHi = "रत्नजड़ित सूर्य वेदी",
            loreEn = "A 3x3 gold-inlaid floor plate requires all 9 solar runes to be awakened simultaneously. The pattern starts in an inverted checkerboard.",
            loreHi = "3x3 सोने की फर्श पट्टिका पर सभी 9 सूर्य रुनों को एक साथ प्रज्वलित करना है। शुरुआत उल्टे शतरंज पैटर्न से होती है।",
            puzzleType = PuzzleType.ELEMENTAL_MATRIX,
            parMoves = 5,
            rewardSparks = 30,
            hint1En = "Tapping the exact center affects all 4 surrounding cardinal shrines.",
            hint1Hi = "बिल्कुल केंद्र को छूने से चारों ओर के 4 मंदिर प्रभावित होते हैं।",
            hint2En = "Look for symmetry. Solving one side often requires doing the identical move on the opposite side.",
            hint2Hi = "समरूपता देखें। एक तरफ की चाल के बाद ठीक सामने वाली तरफ वही चाल दोहराएं।",
            solutionExplanationEn = "Center press inverts the cross, leaving clean pairs easily resolved by edge touches.",
            solutionExplanationHi = "केंद्र को दबाने से क्रॉस उल्टा हो जाता है, फिर किनारों को छूकर सभी 9 वेदियों को रोशन किया जाता है।",
            matrixConfig = MatrixConfig(
                size = 3,
                initialGrid = listOf(
                    true, false, true,
                    false, true, false,
                    true, false, true
                )
            )
        ),
        LevelDefinition(
            id = 9,
            worldId = 2,
            titleEn = "Labyrinth of the Sun King",
            titleHi = "सूर्य सम्राट की भूलभुलैया",
            loreEn = "This chamber holds two distinct gates: a Bronze Gate and a Golden Gate. You must fetch the Bronze Key first to reach the Golden Key!",
            loreHi = "इस विशाल कक्ष में दो कपाट हैं: कांस्य कपाट और स्वर्ण कपाट। स्वर्ण चाबी तक पहुँचने के लिए पहले कांस्य चाबी पाना अनिवार्य है!",
            puzzleType = PuzzleType.CHAMBER_MAZE,
            parMoves = 14,
            rewardSparks = 30,
            hint1En = "Follow the outer perimeter corridor to locate the Bronze Key safely.",
            hint1Hi = "कांस्य चाबी को सुरक्षित रूप से पाने के लिए बाहरी गलियारे का अनुसरण करें।",
            hint2En = "Unlock the Bronze Door, which opens access to the Gold Key and the final portal.",
            hint2Hi = "कांस्य दरवाजा खोलें, जिससे स्वर्ण चाबी और अंतिम द्वार का रास्ता खुल जाएगा।",
            solutionExplanationEn = "Keys must be matched strictly to their corresponding colored gates.",
            solutionExplanationHi = "चाबियों को उनके संबंधित रंगीन द्वारों पर ही उपयोग किया जा सकता है।",
            mazeConfig = MazeConfig(
                width = 5,
                height = 5,
                grid = listOf(
                    TileType.START, TileType.EMPTY, TileType.EMPTY, TileType.WALL, TileType.KEY_BRONZE,
                    TileType.WALL, TileType.WALL, TileType.EMPTY, TileType.WALL, TileType.EMPTY,
                    TileType.KEY_GOLD, TileType.WALL, TileType.DOOR_BRONZE, TileType.EMPTY, TileType.EMPTY,
                    TileType.EMPTY, TileType.TRAP, TileType.WALL, TileType.WALL, TileType.DOOR_GOLD,
                    TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.EXIT
                ),
                maxEnergy = 28
            )
        ),
        LevelDefinition(
            id = 10,
            worldId = 2,
            titleEn = "The Solar Prism Lock",
            titleHi = "सौर प्रिज्म कपाट (बॉस परीक्षा)",
            loreEn = "The High Priest's solar vault is sealed by a mathematical prism equation. Solve the sun rays equation to claim the legendary Scarab of the Sun God!",
            loreHi = "महापुरोहित की सूर्य तिजोरी गणितीय प्रिज्म समीकरण द्वारा सील है। सूर्य देव का स्वर्ण भौंरा पाने हेतु सूर्य किरणों का मान ज्ञात करें!",
            puzzleType = PuzzleType.CIPHER_DECODER,
            parMoves = 1,
            rewardSparks = 40,
            relicUnlockId = "relic_sun_scarab",
            hint1En = "Look at the equations: Sun + Sun = 20 (so Sun = 10).",
            hint1Hi = "समीकरण देखें: सूर्य + सूर्य = 20 (अर्थात सूर्य = 10)।",
            hint2En = "Sun + Moon = 17 (so Moon = 7). What is Sun * Moon?",
            hint2Hi = "सूर्य + चंद्र = 17 (अर्थात चंद्र = 7)। तो सूर्य * चंद्र कितना होगा?",
            solutionExplanationEn = "Sun = 10. Moon = 7. 10 * 7 = 70. The solar harmonic resonance is 70!",
            solutionExplanationHi = "सूर्य = 10, चंद्र = 7, इसलिए 10 * 7 = 70। यही गुप्त कपाट का कोड है!",
            cipherConfig = CipherConfig(
                promptEn = "Solve the ancient solar equation to break the seal: [ ☀️ + ☀️ = 20 ] and [ ☀️ + 🌙 = 17 ]. Calculate: [ ☀️ * 🌙 = ? ]",
                promptHi = "सील तोड़ने के लिए प्राचीन सौर समीकरण हल करें: [ ☀️ + ☀️ = 20 ] और [ ☀️ + 🌙 = 17 ]। मान निकालें: [ ☀️ * 🌙 = ? ]",
                cipherText = "☀️ + ☀️ = 20\n☀️ + 🌙 = 17\n☀️ * 🌙 = ???",
                cluesEn = listOf("Find the value of the Sun (☀️) first", "Find the Moon (🌙), then multiply them"),
                cluesHi = listOf("पहले सूर्य (☀️) का मान निकालें", "फिर चंद्र (🌙) का मान निकालकर गुणा करें"),
                numericAnswer = 70,
                options = listOf("27", "60", "70", "170")
            )
        ),

        // WORLD 3: Citadel of Shadows
        LevelDefinition(
            id = 11,
            worldId = 3,
            titleEn = "Gate of Eclipse",
            titleHi = "ग्रहण द्वार",
            loreEn = "Perched on dark volcanic spires, the Citadel of Shadows welcomes no one. The Gate of Eclipse connects three interlocking dark-matter dials. Rotate them to alignment.",
            loreHi = "ज्वालामुखीय चोटियों पर स्थित छाया दुर्ग किसी का स्वागत नहीं करता। ग्रहण द्वार के तीनों चक्र एक-दूसरे के विपरीत घूमते हैं। उन्हें संतुलित करें।",
            puzzleType = PuzzleType.RUNIC_DIAL,
            parMoves = 8,
            rewardSparks = 30,
            hint1En = "Notice that dial 2 influences dial 0 when moved. Calibrate 2 first.",
            hint1Hi = "ध्यान दें कि चक्र 2 को घुमाने पर चक्र 0 भी हिलता है। पहले 2 को सही करें।",
            hint2En = "Work backwards from the interconnected dependencies.",
            hint2Hi = "आपस में जुड़े चक्रों को क्रम से हल करें।",
            solutionExplanationEn = "Aligning the void runes to 0 degrees breaks the shadow lock.",
            solutionExplanationHi = "शून्य डिग्री पर तीनों रुनों को संरेखित करने से छाया का ताला टूट जाता है।",
            runicConfig = RunicConfig(
                ringCount = 3,
                initialAngles = listOf(90, 270, 180),
                targetAngles = listOf(0, 0, 0),
                dialLabelsEn = listOf("Umbra Ring", "Penumbra Ring", "Antumbra Ring"),
                dialLabelsHi = listOf("पूर्ण छाया चक्र", "उपच्छाया चक्र", "प्रतिच्छाया चक्र"),
                dialConnections = mapOf(1 to listOf(0), 2 to listOf(1))
            )
        ),
        LevelDefinition(
            id = 12,
            worldId = 3,
            titleEn = "Whisper of the Ghost Scribe",
            titleHi = "अदृश्य मुनि की पहेली",
            loreEn = "A floating ethereal quill writes letters in violet dust upon an obsidian tomb. Read the phantom's riddle carefully.",
            loreHi = "एक हवा में तैरती जादुई कलम समाधि पर बैंगनी अक्षरों में लिखती है। मुनि की गुप्त पहेली का उत्तर दें।",
            puzzleType = PuzzleType.ANCIENT_RIDDLE,
            parMoves = 1,
            rewardSparks = 30,
            hint1En = "If you speak its name, you instantly destroy it.",
            hint1Hi = "यदि आप इसका नाम पुकारते हैं, तो यह उसी क्षण नष्ट हो जाती है।",
            hint2En = "Monks and sages seek it in meditation.",
            hint2Hi = "साधु और योगी ध्यान में इसी की खोज करते हैं।",
            solutionExplanationEn = "Silence is completely unbroken until a word is uttered.",
            solutionExplanationHi = "मौन (शांति) इतना नाजुक है कि नाम लेते ही टूट जाता है।",
            riddleConfig = RiddleConfig(
                questionEn = "I am so fragile that if you say my name, you break me. Kings cannot buy me with gold, yet the quietest cave is filled with me. What am I?",
                questionHi = "मैं इतनी नाजुक हूँ कि मेरा नाम लेते ही मैं टूट जाती हूँ। राजा सोने से मुझे खरीद नहीं सकते, पर शांत गुफा में मैं हर ओर होती हूँ। मैं क्या हूँ?",
                optionsEn = listOf("A Crystal Glass", "Silence", "A Secret", "Darkness"),
                optionsHi = listOf("कांच का प्याला (Crystal Glass)", "मौन / शांति (Silence)", "रहस्य (A Secret)", "अंधकार (Darkness)"),
                correctIndex = 1,
                loreFactEn = "The Ghost Scribes sealed their library with silence to protect knowledge from warmongers.",
                loreFactHi = "अदृश्य मुनियों ने अपने ज्ञान को युद्ध से बचाने के लिए पुस्तकालय को मौन से सील किया था।"
            )
        ),
        LevelDefinition(
            id = 13,
            worldId = 3,
            titleEn = "Matrix of Void Runes",
            titleHi = "शून्य रुन आव्यूह",
            loreEn = "A dark energy grid pulsates with negative charges. Light all 9 nodes to reverse the polarity and collapse the shadow barrier.",
            loreHi = "नकारात्मक ऊर्जा का यह 3x3 जाल स्पंदित हो रहा है। सभी 9 नोड्स को प्रकाशित कर छाया अवरोध को समाप्त करें।",
            puzzleType = PuzzleType.ELEMENTAL_MATRIX,
            parMoves = 6,
            rewardSparks = 35,
            hint1En = "The four corners are currently dark. Tapping corners flips only 3 adjacent nodes.",
            hint1Hi = "चारों कोने वर्तमान में बुझे हैं। कोनों को छूने से केवल 3 आस-पास के नोड बदलते हैं।",
            hint2En = "Work systematically around the edges.",
            hint2Hi = "किनारों के चारों ओर व्यवस्थित रूप से काम करें।",
            solutionExplanationEn = "Balancing the 4 corners in sequence inverts the central charge safely.",
            solutionExplanationHi = "क्रम से चारों कोनों को छूने से केंद्रीय ऊर्जा सुरक्षित रूप से संतुलित हो जाती है।",
            matrixConfig = MatrixConfig(
                size = 3,
                initialGrid = listOf(
                    false, false, false,
                    false, true, false,
                    false, false, false
                )
            )
        ),
        LevelDefinition(
            id = 14,
            worldId = 3,
            titleEn = "Dungeon of the Forgotten Knight",
            titleHi = "अनाम योद्धा का कारागार",
            loreEn = "Lethal blade traps line the corridor floor. You must retrieve the Gold Key, cross a perilous narrow bridge, and unlock the Knight's Gate.",
            loreHi = "गलियारे के फर्श पर घातक ब्लेड जाल बिछे हैं। आपको स्वर्ण चाबी प्राप्त करनी है, संकरे पुल को पार करना है, और योद्धा का कपाट खोलना है।",
            puzzleType = PuzzleType.CHAMBER_MAZE,
            parMoves = 12,
            rewardSparks = 35,
            hint1En = "There are multiple spike traps. Move deliberately one tile at a time.",
            hint1Hi = "यहाँ कई कांटेदार जाल हैं। एक समय में केवल एक कदम सोच-समझकर आगे बढ़ें।",
            hint2En = "Do not rush toward the exit before securing the Gold Key.",
            hint2Hi = "स्वर्ण चाबी लिए बिना निकास की ओर न भागें।",
            solutionExplanationEn = "Step zigzagging between traps to safely secure the key and reach the door.",
            solutionExplanationHi = "जालों के बीच से टेढ़े-मेढ़े कदमों से चलकर सुरक्षित चाबी लें और द्वार तक पहुंचें।",
            mazeConfig = MazeConfig(
                width = 5,
                height = 5,
                grid = listOf(
                    TileType.START, TileType.TRAP, TileType.EMPTY, TileType.EMPTY, TileType.KEY_GOLD,
                    TileType.EMPTY, TileType.WALL, TileType.WALL, TileType.TRAP, TileType.EMPTY,
                    TileType.EMPTY, TileType.EMPTY, TileType.DOOR_GOLD, TileType.EMPTY, TileType.EMPTY,
                    TileType.TRAP, TileType.WALL, TileType.WALL, TileType.EMPTY, TileType.TRAP,
                    TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.EMPTY, TileType.EXIT
                ),
                maxEnergy = 26
            )
        ),
        LevelDefinition(
            id = 15,
            worldId = 3,
            titleEn = "The Cryptographic Obelisk",
            titleHi = "कूट-स्तंभ का रहस्य (बॉस परीक्षा)",
            loreEn = "At the heart of the citadel stands an ominous Black Obelisk. Decipher the ancient Caesar shift cipher inscribed in runic letters to claim the Obsidian Shadow Blade!",
            loreHi = "दुर्ग के केंद्र में एक विशाल काला स्तम्भ खड़ा है। इस पर खुदी प्राचीन गुप्त लिपि को सुलझाएं और काला छाया खंजर प्राप्त करें!",
            puzzleType = PuzzleType.CIPHER_DECODER,
            parMoves = 1,
            rewardSparks = 45,
            relicUnlockId = "relic_shadow_dagger",
            hint1En = "The letters are shifted back by 1 step in the alphabet (B -> A, P -> O...).",
            hint1Hi = "वर्णमाला में अक्षर 1 कदम पीछे खिसके हैं (B -> A, P -> O...)।",
            hint2En = "Decode 'PQFO': P(-1)=O, Q(-1)=P, F(-1)=E, O(-1)=N -> 'OPEN'. What does 'MFWFM' decode to?",
            hint2Hi = "'MFWFM' को 1 कदम पीछे खिसकाएं: M(-1)=L, F(-1)=E, W(-1)=V, F(-1)=E, M(-1)=L -> 'LEVEL'!",
            solutionExplanationEn = "Shifting each letter of 'MFWFM' back by 1 gives 'LEVEL'. The magic password is LEVEL!",
            solutionExplanationHi = "प्रत्येक अक्षर को 1 कदम पीछे करने पर 'LEVEL' शब्द प्राप्त होता है। यही गुप्त पासवर्ड है!",
            cipherConfig = CipherConfig(
                promptEn = "The Obelisk code shifts each letter forward by +1. Decrypt the secret master key: [ M - F - W - F - M ]",
                promptHi = "स्तंभ का गुप्त नियम प्रत्येक अक्षर को +1 आगे बढ़ाता है। गुप्त मूल शब्द डिकोड करें: [ M - F - W - F - M ]",
                cipherText = "Cipher: [ M F W F M ]\nShift Rule: -1 step back",
                cluesEn = listOf("Example: B becomes A, E becomes D", "M - 1 = L, F - 1 = E..."),
                cluesHi = listOf("उदाहरण: B बन जाता है A, E बन जाता है D", "M - 1 = L, F - 1 = E..."),
                textAnswer = "LEVEL",
                options = listOf("MAGIC", "LEVEL", "SWORD", "POWER")
            )
        ),

        // WORLD 4: Astral Sanctuary
        LevelDefinition(
            id = 16,
            worldId = 4,
            titleEn = "Chrono-Dial of Destiny",
            titleHi = "कालचक्र का संरेखन",
            loreEn = "You have ascended into the Astral Sanctuary above the clouds. Stars float around you. Four concentric cosmic rings spin in celestial harmony. Align all four to the North Star!",
            loreHi = "आप बादलों के ऊपर नक्षत्र लोक में पहुँच चुके हैं। आपके चारों ओर तारे तैर रहे हैं। चार आकाशीय चक्र घूम रहे हैं। चारों को ध्रुव तारे की सीध में लाएं!",
            puzzleType = PuzzleType.RUNIC_DIAL,
            parMoves = 8,
            rewardSparks = 40,
            hint1En = "Ring 0 moves 1, Ring 1 moves 2, Ring 2 moves 3.",
            hint1Hi = "चक्र 0 चक्र 1 को, चक्र 1 चक्र 2 को, और चक्र 2 चक्र 3 को घुमाता है।",
            hint2En = "Calibrate from outer to inner sequentially.",
            hint2Hi = "बाहरी से भीतरी की ओर क्रम से संरेखित करें।",
            solutionExplanationEn = "Aligning the four celestial axes opens the portal to the stellar core.",
            solutionExplanationHi = "चारों आकाशीय अक्षों को संरेखित करने से नक्षत्र कोर का पोर्टल खुलता है।",
            runicConfig = RunicConfig(
                ringCount = 3,
                initialAngles = listOf(90, 180, 270),
                targetAngles = listOf(0, 0, 0),
                dialLabelsEn = listOf("Nebula Ring", "Stellar Ring", "Pulsar Ring"),
                dialLabelsHi = listOf("नीहारिका चक्र", "तारामंडल चक्र", "पल्सर चक्र"),
                dialConnections = mapOf(0 to listOf(1), 1 to listOf(2))
            )
        ),
        LevelDefinition(
            id = 17,
            worldId = 4,
            titleEn = "The Celestial Sphinx",
            titleHi = "नक्षत्र पहेली",
            loreEn = "A constellation of stars coalesces into an immortal astral spirit. It poses the riddle of the universe.",
            loreHi = "तारों का एक समूह मिलकर एक अमर नक्षत्र देव का रूप लेता है। वह आपसे ब्रह्मांड की सबसे गूढ़ पहेली पूछता है।",
            puzzleType = PuzzleType.ANCIENT_RIDDLE,
            parMoves = 1,
            rewardSparks = 40,
            hint1En = "It was here before the mountains, yet never ages.",
            hint1Hi = "यह पहाड़ों से भी पहले था, फिर भी कभी बूढ़ा नहीं होता।",
            hint2En = "Seconds, hours, and centuries are its heartbeat.",
            hint2Hi = "सेकंड, घंटे और शताब्दियां इसकी धड़कनें हैं।",
            solutionExplanationEn = "Time devours all empires, heals all wounds, yet cannot be touched or paused.",
            solutionExplanationHi = "समय (Time) सब कुछ बदल देता है, घावों को भरता है, पर इसे रोका नहीं जा सकता।",
            riddleConfig = RiddleConfig(
                questionEn = "This thing all things devours: birds, beasts, trees, flowers; gnaws iron, bites steel; grinds hard stones to meal; slays king, ruins town, and beats high mountain down. What is it?",
                questionHi = "यह सबको निगल जाता है: पक्षी, पशु, पेड़, फूल; लोहे को खा जाता है, पत्थर को पीसकर धूल बना देता है; राजाओं को नष्ट करता है और ऊंचे पहाड़ों को झुका देता है। यह क्या है?",
                optionsEn = listOf("Fire", "Time", "The Ocean", "Wind"),
                optionsHi = listOf("अग्नि (Fire)", "समय / काल (Time)", "महासागर (The Ocean)", "तूफान (Wind)"),
                correctIndex = 1,
                loreFactEn = "The astral elders recorded that Time is the fifth dimension holding reality together.",
                loreFactHi = "आकाशीय ऋषियों ने लिखा था कि समय ही वह तत्व है जो संपूर्ण सृष्टि को बांधे रखता है।"
            )
        ),
        LevelDefinition(
            id = 18,
            worldId = 4,
            titleEn = "Supernova Matrix",
            titleHi = "तारामंडल महा-संतुलन",
            loreEn = "Nine dying stars form a 3x3 cosmic grid. You must reignite all 9 stars simultaneously to ignite the constellation.",
            loreHi = "नौ मंद पड़ते तारे 3x3 के आकाशीय आव्यूह में हैं। तारामंडल को जगाने के लिए सभी 9 तारों को एक साथ प्रज्वलित करें।",
            puzzleType = PuzzleType.ELEMENTAL_MATRIX,
            parMoves = 6,
            rewardSparks = 45,
            hint1En = "Notice how the edge stars create a wave toward the opposite edge.",
            hint1Hi = "ध्यान दें कि किनारों के तारे विपरीत किनारे की ओर ऊर्जा तरंग भेजते हैं।",
            hint2En = "A diamond pattern tap (top, left, right, bottom) creates harmonious resonance.",
            hint2Hi = "हीरे के आकार में टैप करने से (ऊपर, बाएं, दाएं, नीचे) अद्भुत संतुलन बनता है।",
            solutionExplanationEn = "Activating the cross coordinates sets off a chain reaction across all 9 star nodes.",
            solutionExplanationHi = "क्रॉस निर्देशांकों को सक्रिय करने से सभी 9 तारों में ऊर्जा फैल जाती है।",
            matrixConfig = MatrixConfig(
                size = 3,
                initialGrid = listOf(
                    true, true, true,
                    true, false, true,
                    true, true, true
                )
            )
        ),
        LevelDefinition(
            id = 19,
            worldId = 4,
            titleEn = "The Cosmic Void Maze",
            titleHi = "अंतरिक्ष चक्रव्यूह",
            loreEn = "A labyrinth suspended over the endless abyss of cosmos. Collect both the Bronze and Gold Keys, evade astral rifts, and activate the Grand Gateway!",
            loreHi = "अनंत अंतरिक्ष के ऊपर तैरती हुई भूलभुलैया। कांस्य और स्वर्ण दोनों चाबियां जुटाएं, अंतरिक्षीय दरारों से बचें, और महा-द्वार खोलें!",
            puzzleType = PuzzleType.CHAMBER_MAZE,
            parMoves = 16,
            rewardSparks = 50,
            hint1En = "Map out your steps carefully. Traps in this void consume extra energy!",
            hint1Hi = "अपने कदमों की सावधानीपूर्वक योजना बनाएं। इस शून्य में जाल अधिक ऊर्जा सोखते हैं!",
            hint2En = "Gather Bronze Key -> Open Bronze Gate -> Gather Gold Key -> Open Final Gate.",
            hint2Hi = "कांस्य चाबी लें -> कांस्य द्वार खोलें -> स्वर्ण चाबी लें -> अंतिम द्वार खोलें।",
            solutionExplanationEn = "Follow the outer safe perimeter orbit to reach both keys without triggering the astral rifts.",
            solutionExplanationHi = "दरारों से बचते हुए बाहरी सुरक्षित कक्षा का पालन कर दोनों चाबियां प्राप्त करें।",
            mazeConfig = MazeConfig(
                width = 5,
                height = 5,
                grid = listOf(
                    TileType.START, TileType.EMPTY, TileType.WALL, TileType.KEY_BRONZE, TileType.EMPTY,
                    TileType.EMPTY, TileType.TRAP, TileType.EMPTY, TileType.WALL, TileType.EMPTY,
                    TileType.DOOR_BRONZE, TileType.WALL, TileType.EMPTY, TileType.KEY_GOLD, TileType.EMPTY,
                    TileType.EMPTY, TileType.EMPTY, TileType.WALL, TileType.WALL, TileType.DOOR_GOLD,
                    TileType.WALL, TileType.TRAP, TileType.EMPTY, TileType.EMPTY, TileType.EXIT
                ),
                maxEnergy = 32
            )
        ),
        LevelDefinition(
            id = 20,
            worldId = 4,
            titleEn = "Trial of the Grand Architect",
            titleHi = "महा-शिल्पी की अंतिम परीक्षा (भव्य अंत)",
            loreEn = "The Grand Architect of the Cosmos awaits at the throne of eternity. Solve the ultimate cipher of creation to awaken the Cosmic Heart Core and conquer Mystic Quest!",
            loreHi = "सृष्टि के महा-शिल्पी अनंत के सिंहासन पर आपकी प्रतीक्षा कर रहे हैं। ब्रह्मांडीय महा-रत्न प्राप्त करने और मिस्टिक क्वेस्ट को फतह करने हेतु अंतिम कूट-पहेली हल करें!",
            puzzleType = PuzzleType.CIPHER_DECODER,
            parMoves = 1,
            rewardSparks = 100,
            relicUnlockId = "relic_cosmic_core",
            hint1En = "Look at the sequence: 1, 1, 2, 3, 5, 8, 13... This is the famous Fibonacci golden spiral of nature!",
            hint1Hi = "संख्याओं का क्रम देखें: 1, 1, 2, 3, 5, 8, 13... यह प्रकृति का प्रसिद्ध फिबोनाची स्वर्णिम अनुपात है!",
            hint2En = "Each number is the sum of the two preceding numbers: 8 + 13 = ?",
            hint2Hi = "प्रत्येक संख्या पिछली दो संख्याओं का जोड़ है: 8 + 13 = ?",
            solutionExplanationEn = "8 + 13 = 21. The divine golden ratio frequency is 21! You have unlocked the Cosmic Heart Core!",
            solutionExplanationHi = "8 + 13 = 21! यही ब्रह्मांड का स्वर्णिम सूत्र है! आपने ब्रह्मांडीय महा-रत्न प्राप्त कर लिया है!",
            cipherConfig = CipherConfig(
                promptEn = "The Grand Architect whispers the code of universal creation: [ 1, 1, 2, 3, 5, 8, 13, ? ]. Enter the golden number to finish the quest!",
                promptHi = "महा-शिल्पी सृष्टि रचना का स्वर्णिम सूत्र दोहराते हैं: [ 1, 1, 2, 3, 5, 8, 13, ? ]। यात्रा पूर्ण करने हेतु लुप्त अंक चुनें!",
                cipherText = "Spiral of Life:\n[ 1, 1, 2, 3, 5, 8, 13, ❓ ]",
                cluesEn = listOf("Fibonacci sequence: add the two previous numbers together", "8 + 13 = ?"),
                cluesHi = listOf("फिबोनाची क्रम: पिछली दो संख्याओं को आपस में जोड़ें", "8 + 13 = ?"),
                numericAnswer = 21,
                options = listOf("18", "21", "24", "26")
            )
        )
    )

    suspend fun initializeIfEmpty() {
        val existingProgress = dao.getAllProgress().firstOrNull()
        if (existingProgress.isNullOrEmpty()) {
            val initialList = levels.map { level ->
                ProgressEntity(
                    levelId = level.id,
                    worldId = level.worldId,
                    isUnlocked = level.id == 1, // First level unlocked by default
                    isCompleted = false,
                    stars = 0,
                    bestMoves = 0,
                    bestTimeSeconds = 0
                )
            }
            dao.insertAllProgress(initialList)
            dao.insertAllRelics(defaultRelics)
            dao.savePlayerProfile(
                PlayerProfileEntity(
                    id = 1,
                    sparks = 60,
                    language = "hi",
                    soundEnabled = true,
                    hapticsEnabled = true
                )
            )
        }
    }

    suspend fun completeLevel(
        levelId: Int,
        starsEarned: Int,
        moves: Int,
        timeSeconds: Int
    ) {
        val current = dao.getProgressForLevel(levelId) ?: return
        val newStars = maxOf(current.stars, starsEarned)
        val bestMoves = if (current.bestMoves == 0) moves else minOf(current.bestMoves, moves)
        val bestTime = if (current.bestTimeSeconds == 0) timeSeconds else minOf(current.bestTimeSeconds, timeSeconds)

        dao.updateProgress(
            current.copy(
                isCompleted = true,
                stars = newStars,
                bestMoves = bestMoves,
                bestTimeSeconds = bestTime
            )
        )

        // Unlock next level if exists
        val nextLevelId = levelId + 1
        if (nextLevelId <= levels.size) {
            val nextProgress = dao.getProgressForLevel(nextLevelId)
            if (nextProgress != null && !nextProgress.isUnlocked) {
                dao.updateProgress(nextProgress.copy(isUnlocked = true))
            }
        }

        // Check if level unlocks a relic
        val levelDef = levels.find { it.id == levelId }
        levelDef?.relicUnlockId?.let { relicId ->
            dao.unlockRelic(relicId, System.currentTimeMillis())
        }

        // Add reward sparks
        levelDef?.rewardSparks?.let { sparks ->
            dao.addSparks(sparks)
        }
    }

    suspend fun useHintSparks(cost: Int): Boolean {
        dao.addSparks(-cost)
        return true
    }

    suspend fun setLanguage(lang: String) = dao.setLanguage(lang)
    suspend fun setSoundEnabled(enabled: Boolean) = dao.setSoundEnabled(enabled)
    suspend fun setHapticsEnabled(enabled: Boolean) = dao.setHapticsEnabled(enabled)
}
