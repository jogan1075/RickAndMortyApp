package com.jmc.details.data.source

import com.jmc.details.data.repository.Remote
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock

class FactoryTest {

    private val remote = mock<Remote>()
    private val factory = Factory(remote)

    @Test
    fun `when getCache, then Remote instance not null`() {
        Assert.assertNotNull(factory.getRemote())
    }

}