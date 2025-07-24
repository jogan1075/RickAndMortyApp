package com.jmc.details.data

import com.jmc.details.data.model.DetailResult
import com.jmc.details.data.model.Location
import com.jmc.details.data.model.Origin
import com.jmc.details.data.repository.Remote
import com.jmc.details.data.source.Factory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTest {

    private lateinit var remote: Remote
    private lateinit var factory: Factory
    private lateinit var repository: DataRepository


    @Before
    fun setup() {
        remote = mock(Remote::class.java)
        factory = mock(Factory::class.java)
        repository = DataRepository(factory)
    }

    @Test
    fun `given valid id when getDetailCharacter is called then return DetailResult`() = runTest {

        val characterId = 1
        val expectedDetail = DetailResult(
            id = characterId,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = Origin(name = "Earth", url = ""),
            location = Location(name = "Citadel of Ricks", url = ""),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            created = "",
            episode = emptyList(),
            type = "",
            url = ""
        )

        whenever(factory.getRemote()).thenReturn(remote)
        whenever(remote.getCharacterInfo(characterId)).thenReturn(expectedDetail)

        val result = repository.getDetailCharacter(characterId)

        assertEquals(expectedDetail, result)
        verify(remote).getCharacterInfo(characterId)
    }

    @Test(expected = RuntimeException::class)
    fun `given remote throws error when getDetailCharacter is called then exception is thrown`() = runTest {

        val characterId = 999

        whenever(factory.getRemote()).thenReturn(remote)
        whenever(remote.getCharacterInfo(characterId)).thenThrow(RuntimeException("Not found"))

        repository.getDetailCharacter(characterId)
    }
}