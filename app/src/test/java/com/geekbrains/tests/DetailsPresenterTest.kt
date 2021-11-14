package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenterImpl
import com.geekbrains.tests.view.details.ViewDetailsContract
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DetailsPresenterTest {

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    private val detailsPresenter by lazy { DetailsPresenterImpl() }


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        detailsPresenter.attachView(viewContract)
    }

    @Test
    fun onIncrement_test() {
        var initCounterValues = 100
        detailsPresenter.setCounter(initCounterValues)
        detailsPresenter.onIncrement()
        verify(viewContract, times(1)).setCount(++initCounterValues)
    }

    @Test
    fun onDecrement_test() {
        var initCounterValues = 100
        detailsPresenter.setCounter(initCounterValues)
        detailsPresenter.onDecrement()
        verify(viewContract, times(1)).setCount(--initCounterValues)
    }

    @Test
    fun detachView_onIncrement_noSetCount() {
        detailsPresenter.detachView(viewContract)
        detailsPresenter.onIncrement()
        verify(viewContract, never()).setCount(any())
    }

    @Test
    fun detachView_onDecrement_noSetCount() {
        detailsPresenter.detachView(viewContract)
        detailsPresenter.onDecrement()
        verify(viewContract, never()).setCount(any())
    }
}
