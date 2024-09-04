package com.example.cyrusflashcards

import android.app.Application
import androidx.hilt.navigation.compose.hiltViewModel
import org.junit.Test
import org.junit.Assert.*


import org.junit.Assert.*
import org.junit.Before

import org.mockito.Mockito.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cyrusflashcards.data.CyrusCard
import com.example.cyrusflashcards.data.CyrusCardDao
import com.example.cyrusflashcards.data.CyrusDeckDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CyrusHiltViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mock all dependencies
    @Mock
    lateinit var application: Application

    @Mock
    lateinit var auth: Auth

    @Mock
    lateinit var cyrusCardDao: CyrusCardDao

    @Mock
    lateinit var cyrusDeckDao: CyrusDeckDao

    @Mock
    lateinit var cyrusRepository: CyrusRepository

    @Mock
    lateinit var supabaseClient: SupabaseClient

    @Mock
    lateinit var supabasePostgrest: Postgrest

    private lateinit var viewModel: CyrusHiltViewModel

    @Before
    fun setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this)

        // Create the ViewModel instance with mocked dependencies
        viewModel = CyrusHiltViewModel(
            application = application,
            auth = auth,
            cyrusCardDao = cyrusCardDao,
            cyrusDeckDao = cyrusDeckDao,
            cyrusRepository = cyrusRepository,
            supabaseClient = supabaseClient,
            supabasePostgrest = supabasePostgrest
        )
    }

    //Normal situations
    //Make sure interval sets up properly after first review
    @Test
    fun testFirstReview1DayInterval() {
        val interval = 0
        val reviewCount = 1
        val eFactor = 2.5
        val newInterval = viewModel.calculateNewInterval(interval, reviewCount, eFactor)
        assertEquals(1, newInterval)
    }

    //Make sure that interval is set up properly after second review
    @Test
    fun testSecondReview6DayInterval() {
        val interval = 1
        val reviewCount = 2
        val eFactor = 2.5
        val newInterval = viewModel.calculateNewInterval(interval, reviewCount, eFactor)
        assertEquals(6, newInterval)
    }

    //Make sure that the qfactor entered by user works properly over multimple reviews.
    @Test
    fun testThirdAndLaterReviewWithQFactor() {
        var eFactor = 2.5
        val qFactor1 = 5

        //review 1
        eFactor = viewModel.calculateNewEFactor(eFactor, qFactor1)
        assertTrue(eFactor > 2.5)

        // review 2
        val qFactor2 = 3
        val eFactorAfterSecondReview = viewModel.calculateNewEFactor(eFactor, qFactor2)
        assertTrue(eFactorAfterSecondReview < eFactor)

        // review 3
        val qFactor3 = 1
        val eFactorAfterThirdReview =
            viewModel.calculateNewEFactor(eFactorAfterSecondReview, qFactor3)
        assertTrue(eFactorAfterThirdReview < eFactorAfterSecondReview)

    }

    ///Edge Cases

    //Make sure E-factor cannot go below 1.3
    @Test
    fun testEFactorFloor() {
        val eFactor = 1.4
        val qFactor = 1
        val newEFactor = viewModel.calculateNewEFactor(eFactor, qFactor)
        assertEquals(1.3, newEFactor, 0.0)
    }


//test large number of reviews

    @Test
    fun testManyReviews() {
        val interval = 30
        val reviewCount = 100
        val eFactor = 2.5
        val newInterval = viewModel.calculateNewInterval(interval, reviewCount, eFactor)
        assertTrue(newInterval > 30)
    }
}










//
//class CyrustHiltViewModelTests {
//
//    val viewModel: CyrusHiltViewModel = hiltViewModel()
//
//    @Test
//    fun testCalculateNewEFactor() {
//        val eFactor = 2.5
//        val qFactor = 4
//        val newEFactor = calculateNewEFactor(eFactor, qFactor)
//        assertTrue(newEFactor >= 1.3)
//        assertEquals(2.36, newEFactor, 0.01) // Expected value should be pre-calculated
//    }
//
//
//
//}