package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: GameRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = GameRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Mystic Quest", appName)
    }

    @Test
    fun `verify 20 levels defined across 4 worlds`() {
        assertEquals(20, repository.levels.size)
        assertEquals(4, repository.worlds.size)
        assertEquals(5, repository.levels.filter { it.worldId == 1 }.size)
        assertEquals(5, repository.levels.filter { it.worldId == 2 }.size)
        assertEquals(5, repository.levels.filter { it.worldId == 3 }.size)
        assertEquals(5, repository.levels.filter { it.worldId == 4 }.size)
    }

    @Test
    fun `verify database initialization and level completion progression`() = runBlocking {
        repository.initializeIfEmpty()

        val initialProgress = repository.allProgress.first()
        assertEquals(20, initialProgress.size)

        // Level 1 should be unlocked, Level 2 should be locked
        val level1 = initialProgress.find { it.levelId == 1 }
        val level2 = initialProgress.find { it.levelId == 2 }
        assertNotNull(level1)
        assertNotNull(level2)
        assertTrue(level1!!.isUnlocked)
        assertFalse(level1.isCompleted)
        assertFalse(level2!!.isUnlocked)

        // Complete Level 1 with 3 stars
        repository.completeLevel(levelId = 1, starsEarned = 3, moves = 3, timeSeconds = 45)

        val updatedProgress = repository.allProgress.first()
        val updatedLevel1 = updatedProgress.find { it.levelId == 1 }
        val updatedLevel2 = updatedProgress.find { it.levelId == 2 }

        assertTrue(updatedLevel1!!.isCompleted)
        assertEquals(3, updatedLevel1.stars)
        assertTrue("Level 2 should now be unlocked", updatedLevel2!!.isUnlocked)

        // Complete World 1 Boss (Level 5) to unlock Emerald Relic
        repository.completeLevel(levelId = 5, starsEarned = 3, moves = 8, timeSeconds = 90)
        val relics = repository.allRelics.first()
        val relic1 = relics.find { it.relicId == "relic_forest_emerald" }
        assertNotNull(relic1)
        assertTrue("Emerald relic should be unlocked", relic1!!.isUnlocked)
    }
}

